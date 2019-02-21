package org.oliot.tutorials.epcis;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.oliot.bis.service.utility.ExistingQueryService;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.epcis.converter.service.ObjectEventWriteConverter;
import org.oliot.epcis.security.OAuthUtil;
import org.oliot.epcis.service.capture.BIS.CaptureBusEstimation;
import org.oliot.epcis.service.capture.BIS.CaptureBusLine;
import org.oliot.epcis.service.capture.BIS.CaptureEPCIS;
import org.oliot.epcis.service.capture.BIS.Translator;
import org.oliot.model.BIS.BusEstimationSemantic;
import org.oliot.model.BIS.BusLineSematic;
import org.oliot.model.BIS.BusStopSemantic;
import org.oliot.model.epcis.ActionType;
import org.oliot.model.epcis.BusinessLocationType;
import org.oliot.model.epcis.BusinessTransactionListType;
import org.oliot.model.epcis.BusinessTransactionType;
import org.oliot.model.epcis.DestinationListType;
import org.oliot.model.epcis.EPC;
import org.oliot.model.epcis.EPCISDocumentType;
import org.oliot.model.epcis.EPCISEventExtensionType;
import org.oliot.model.epcis.EPCListType;
import org.oliot.model.epcis.ILMDType;
import org.oliot.model.epcis.ObjectEventExtensionType;
import org.oliot.model.epcis.ObjectEventType;
import org.oliot.model.epcis.QuantityElementType;
import org.oliot.model.epcis.QuantityListType;
import org.oliot.model.epcis.ReadPointType;
import org.oliot.model.epcis.SourceDestType;
import org.oliot.model.epcis.SourceListType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Test extends Translator{

//	public static void main(String[] args)  {
//		new Configuration();
//		MongoCollection<BsonDocument> collection = Configuration.mongoDatabase.getCollection("MasterData",
//				BsonDocument.class);
//		FindIterable<BsonDocument> cursor = collection.find(new BsonDocument(
//				"type", new BsonString("urn:gs1:epcis:santander:bus:line:info")));
//		
//		JSONArray retArray = new JSONArray();
//
//		MongoCursor<BsonDocument> slCursor = cursor.iterator();
//		
//
//		HashSet<BsonDocument> dbObjectSet = new HashSet<BsonDocument>();
//		while (slCursor.hasNext()) {
//			BsonDocument dbObject = slCursor.next();
//
//			dbObjectSet.add(dbObject);
//		}
//		List<JSONObject> objList = dbObjectSet.parallelStream().map(obj -> {
//			try {
//			return new JSONObject(obj.toJson());
//			}
//			catch(JSONException e) {
//				return null;
//			}
//		}).collect(Collectors.toList());
//		retArray = new JSONArray(objList);
//		List<String> lineIDs=new ArrayList<>();
//		for(int i=0; i<retArray.length(); i++) {
//			try {
//				JSONObject objec=(JSONObject) retArray.get(i);
//				//System.out.println(objec);
//				
//				
//				
//				String refBusStops=objec.getJSONObject("attributes").getString("http://epcis．example．com/bus/line/refBusStops");
//				String busLineID=objec.getJSONObject("attributes").getString("http://epcis．example．com/bus/stop/id");
//				
//				if(refBusStops.contains("urn:entity:santander:transport:bus:busStop:448")) {
//					try {
//						lineIDs.add(busLineID.substring(45,busLineID.length()-2));
//					}catch(Exception e) {
//						
//					}
//					
//				}
//				
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		System.out.println(lineIDs);
//	
//		
//	}
	
	
	List<BusLineSematic> translateBusan(String data){
		List<BusLineSematic> busLines=new ArrayList<>();
		String serviceKey = "OQX6Z00oyK1KwR40XLTdxvrSq90gpAsadkdL8Tgn48uArTQIBJzn7UnkUi%2FW7%2BD%2BwwEDyqkM%2Byo%2B2dm7iDocMA%3D%3D";
		
		//id refBusStops localID shortID name refStartBusStop refEndBusStop busLineType startTime endTime intervalNorm intervalHoli intervalPeak dateModified 
		try {
			document = builder.parse(new InputSource(new StringReader(data)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		NodeList nList = document.getElementsByTagName("item");
		System.out.println(nList.getLength());
		// TODO   i < nList.getLength();
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			Element element = (Element) nNode;
			BusLineSematic busLine=new BusLineSematic();
			
			//id 
			String lineId="0000";
			if(element.getElementsByTagName("lineId").item(0) != null) {
				lineId=element.getElementsByTagName("lineId").item(0).getFirstChild().getNodeValue();
			}
			busLine.setId("urn:epc:id:sgtin:88000269.101."+lineId);
			
			//refBusStops 
			List<String> refBusStops=new ArrayList<>();
			try {
				String url_withLineId="http://data.busan.go.kr/openBus/service/busanBIMS2/busInfoRoute?lineid="+lineId+"&serviceKey="+serviceKey;
				String data_refBusStops = ExistingQueryService.normalquery(url_withLineId);
				document = builder.parse(new InputSource(new StringReader(data_refBusStops)));
				NodeList nList_refBusStops = document.getElementsByTagName("item");
				
				String nodeId="00000";
				Element element_0=(Element)nList_refBusStops.item(0);
				
				if(element_0.getElementsByTagName("nodeId").item(0) != null) {
					nodeId=element_0.getElementsByTagName("nodeId").item(0).getFirstChild().getNodeValue();
				}
				busLine.setRefStartBusStop("urn:epc:id:sgtin:88000269.100."+nodeId);
				refBusStops.add("urn:epc:id:sgtin:88000269.100."+nodeId);
				for (int j = 1; j < nList_refBusStops.getLength(); j++) {
					Node nNodet_refBusStops = nList_refBusStops.item(j);
					Element elementt_refBusStops = (Element) nNodet_refBusStops;
					if(elementt_refBusStops.getElementsByTagName("nodeId").item(0) != null) {
						nodeId=elementt_refBusStops.getElementsByTagName("nodeId").item(0).getFirstChild().getNodeValue();
					}
					refBusStops.add("urn:epc:id:sgtin:88000269.100."+nodeId);
				}
				busLine.setRefEndBusStop("urn:epc:id:sgtin:88000269.100."+nodeId);
				
			} catch (IOException  |SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			busLine.setRefBusStops(refBusStops.toString());
			
			//localID 
			String buslinenum="0";
			if(element.getElementsByTagName("buslinenum").item(0) != null) {
				buslinenum=element.getElementsByTagName("buslinenum").item(0).getFirstChild().getNodeValue();
			}
			busLine.setLocalID(buslinenum);
			
			//shortID 
			busLine.setShortID(lineId);
			
			//name 
			String bustype="bustype-Defoult";
			if(element.getElementsByTagName("bustype").item(0) != null) {
				bustype=element.getElementsByTagName("bustype").item(0).getFirstChild().getNodeValue();
			}
			busLine.setName(bustype);
			
			//refStartBusStop 
			
			//refEndBusStop 
			
			//busLineType 
			String companyid="companyid-Defoult";
			if(element.getElementsByTagName("companyid").item(0) != null) {
				companyid=element.getElementsByTagName("companyid").item(0).getFirstChild().getNodeValue();
			}
			busLine.setBusLineType(companyid);
			
			//startTime 
			String firsttime="00:00";
			if(element.getElementsByTagName("firsttime").item(0) != null) {
				firsttime=element.getElementsByTagName("firsttime").item(0).getFirstChild().getNodeValue();
			}
			busLine.setStartTime(firsttime);
			//endTime 
			String endtime="00:00";
			if(element.getElementsByTagName("endtime").item(0) != null) {
				endtime=element.getElementsByTagName("endtime").item(0).getFirstChild().getNodeValue();
			}
			busLine.setEndTime(endtime);
			
			//intervalNorm 
			String headwayNorm="0";
			if(element.getElementsByTagName("headwayNorm").item(0) != null) {
				headwayNorm=element.getElementsByTagName("headwayNorm").item(0).getFirstChild().getNodeValue();
			}
			busLine.setIntervalNorm(headwayNorm);
			
			//intervalHoli 
			String headwayHoli="0";
			if(element.getElementsByTagName("headwayHoli").item(0) != null) {
				headwayHoli=element.getElementsByTagName("headwayHoli").item(0).getFirstChild().getNodeValue();
			}
			busLine.setIntervalHoli(headwayHoli);
			//intervalPeak 
			String headwayPeak="0";
			if(element.getElementsByTagName("headwayPeak").item(0) != null) {
				headwayPeak=element.getElementsByTagName("headwayPeak").item(0).getFirstChild().getNodeValue();
			}
			busLine.setIntervalPeak(headwayPeak);
			
			//dateModified
			busLine.setDateModified("NA");
			
			busLines.add(busLine);
			
		}
		
		
		return busLines;
	}
	public void printBusEstimationSemantic(BusEstimationSemantic busEstimation) {
		
		System.out.println("id : "+busEstimation.getId());
		System.out.println("refBusStop : "+busEstimation.getRefBusStop());
		System.out.println("refBusLine : "+busEstimation.getRefBusLine());
		System.out.println("remainingDistances : "+busEstimation.getRemainingDistances());
		System.out.println("remainingTimes : "+busEstimation.getRemainingTimes());
		System.out.println("destinationBusLines : "+busEstimation.getDestinationBusLines());
		System.out.println("shortID : "+busEstimation.getShortID());
		System.out.println("remainingStations : "+busEstimation.getRemainingStations());
		System.out.println("companyName : "+busEstimation.getCompanyName());
		System.out.println("location : "+busEstimation.getLocation());
		System.out.println("dateModified : "+busEstimation.getDateModified());
		System.out.println("---------------------------------------------------------");
		
	}
	
	public void printBusLineSemantic(BusLineSematic busLine) {
	          
		System.out.println("id : "+busLine.getId());
		System.out.println("refBusStops : "+busLine.getRefBusStops());
		System.out.println("localID : "+busLine.getLocalID());
		System.out.println("shortID : "+busLine.getShortID());
		System.out.println("name : "+busLine.getName());
		System.out.println("refStartBusStop : "+busLine.getRefStartBusStop());
		System.out.println("refEndBusStop : "+busLine.getRefEndBusStop());
		System.out.println("busLineType : "+busLine.getBusLineType());
		System.out.println("startTime : "+busLine.getStartTime());
		System.out.println("endTime : "+busLine.getEndTime());
		System.out.println("intervalNorm : "+busLine.getIntervalNorm());
		System.out.println("intervalHoli : "+busLine.getIntervalHoli());
		System.out.println("intervalPeak : "+busLine.getIntervalPeak());
		System.out.println("dateModified : "+busLine.getDateModified());
	}
	
	private int getNumberofIteration(String data, double numOfRows) {
		
		int iteration=0;
		try {
			document = builder.parse(new InputSource(new StringReader(data)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String totalCountStr = document.getElementsByTagName("totalCount").item(0).getFirstChild().getNodeValue();
		
		int totalCount = Integer.parseInt(totalCountStr);
		
		iteration=(int) Math.ceil(totalCount/numOfRows);
		
		return iteration;
	}
	
	public void printBusStopSemantic(BusStopSemantic busStop) {
		
		System.out.println("id : "+busStop.getId());
		System.out.println("refBuses : "+busStop.getRefBuses());
		System.out.println("shortID : "+busStop.getShortID());
		System.out.println("busStopCount : "+busStop.getBusStopCount());
		System.out.println("name : "+busStop.getName());
		System.out.println("location : "+busStop.getLocation());
		System.out.println("address : "+busStop.getAddress());
		System.out.println("direction : "+busStop.getDirection());
		System.out.println("refBusLines : "+busStop.getRefBusLines());
		System.out.println("dateModified : "+busStop.getDateModified());
	}
	
	public  String  captureEvents(ObjectEventType objectEvent, String id ) {
		ObjectEventWriteConverter objConv=new ObjectEventWriteConverter();
		BsonDocument bsonDocument=objConv.convert(objectEvent, null, "id");
		MongoCollection<BsonDocument> collection = Configuration.mongoDatabase.getCollection("EventData",
				BsonDocument.class);
		BsonDocument event = collection.find(new BsonDocument("id", new BsonString(id))).first();

		boolean isExist = false;
		if (event != null) {
			isExist = true;
		} else {
			event = bsonDocument;
		}
		String result="";
		if (isExist == false) {
			collection.insertOne(event);
			result="document inserted";
		} else {
			result=collection.findOneAndReplace(new BsonDocument("id", new BsonString(id)), event).toString();
		}
		return result;
		
	}
	
	
	
	
	public  ObjectEventType getObjectEventTypeSample(int count) {
		ObjectEventType objectEventType = new ObjectEventType();

		GregorianCalendar gRecordTime = new GregorianCalendar();
		XMLGregorianCalendar recordTime;
		try {
			recordTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gRecordTime);
			objectEventType.setEventTime(recordTime);
			objectEventType.setRecordTime(recordTime);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		objectEventType.setEventTimeZoneOffset("-06:00");

		EPCISEventExtensionType epcisEventExtension = new EPCISEventExtensionType();
		epcisEventExtension.setEventID("5722d7e1deab322596705" + count);
		objectEventType.setBaseExtension(epcisEventExtension);

		EPCListType objectEventEPCs = new EPCListType();
		EPC epc1 = new EPC();
		epc1.setValue("urn:epc:id:sgtin:0614141.107346." + count);
		EPC epc2 = new EPC();
		epc2.setValue("urn:epc:id:sgtin:0614141.107347." + count);

		objectEventEPCs.getEpc().add(epc1);
		objectEventEPCs.getEpc().add(epc2);
		objectEventType.setEpcList(objectEventEPCs);

		objectEventType.setAction(ActionType.fromValue("OBSERVE"));
		objectEventType.setBizStep("urn:epcglobal:cbv:bizstep:receiving");
		objectEventType.setDisposition("urn:epcglobal:cbv:disp:in_progress");

		ReadPointType readPoint = new ReadPointType();
		readPoint.setId("urn:epc:id:sgln:0012345.11111." + count);

		// ReadPointExtensionType readPointExtension=new ReadPointExtensionType();
		// readPoint.setExtension(readPointExtension);

		objectEventType.setReadPoint(readPoint);

		BusinessLocationType businessLocation = new BusinessLocationType();
		businessLocation.setId("urn:epc:id:sgln:0012345.11111." + count);
		// BusinessLocationExtensionType businessLocationExtension= new
		// BusinessLocationExtensionType();
		// businessLocation.setExtension(businessLocationExtension);

		objectEventType.setBizLocation(businessLocation);

		BusinessTransactionListType businessTransactionList = new BusinessTransactionListType();
		BusinessTransactionType businessTransaction1 = new BusinessTransactionType();
		businessTransaction1.setType("urn:epcglobal:cbv:btt:po");
		businessTransaction1.setValue("http://transaction.acme.com/po/" + count);
		BusinessTransactionType businessTransaction2 = new BusinessTransactionType();
		businessTransaction2.setType("urn:epcglobal:cbv:btt:desadv");
		businessTransaction2.setValue("urn:epcglobal:cbv:bt:0614141073467:" + count);
		businessTransactionList.getBizTransaction().add(businessTransaction1);
		businessTransactionList.getBizTransaction().add(businessTransaction2);
		objectEventType.setBizTransactionList(businessTransactionList);

		// ILMDType iLMDType =new ILMDType ();
		// ILMDExtensionType iLMDExtensionType =new ILMDExtensionType();
		// iLMDType.setExtension(iLMDExtensionType);
		// objectEventType.setIlmd(iLMDType);

		ObjectEventExtensionType ObjectEventExtension = new ObjectEventExtensionType();

		QuantityListType quantityList = new QuantityListType();
		QuantityElementType quantityElement1 = new QuantityElementType();
		quantityElement1.setEpcClass("urn:epc:class:lgtin:4012345.012345." + count);
		quantityElement1.setQuantity(new BigDecimal(count));
		quantityElement1.setUom("KGM");
		QuantityElementType quantityElement2 = new QuantityElementType();
		quantityElement2.setEpcClass("urn:epc:class:lgtin:4012345.012346." + count);
		quantityElement2.setQuantity(new BigDecimal(count));
		quantityElement2.setUom("KGM");
		quantityList.getQuantityElement().add(quantityElement1);
		quantityList.getQuantityElement().add(quantityElement2);

		ObjectEventExtension.setQuantityList(quantityList);

		SourceListType sourceList = new SourceListType();
		SourceDestType sourceDest1 = new SourceDestType();
		SourceDestType sourceDest2 = new SourceDestType();
		sourceDest1.setType("urn:epcglobal:cbv:sdt:possessing_party");
		sourceDest1.setValue("urn:epc:id:sgln:4012345.00001." + count);
		sourceDest2.setType("urn:epcglobal:cbv:sdt:location");
		sourceDest2.setValue("urn:epc:id:sgln:4012345.00225." + count);

		sourceList.getSource().add(sourceDest1);
		sourceList.getSource().add(sourceDest2);
		ObjectEventExtension.setSourceList(sourceList);

		DestinationListType destinationList = new DestinationListType();
		SourceDestType sourceDest3 = new SourceDestType();
		SourceDestType sourceDest4 = new SourceDestType();
		sourceDest3.setValue("urn:epc:id:sgln:0614141.00001." + count);
		sourceDest3.setType("urn:epcglobal:cbv:sdt:owning_party");
		sourceDest4.setValue("urn:epc:id:sgln:0614141.00777." + count);
		sourceDest4.setType("urn:epcglobal:cbv:sdt:location");

		destinationList.getDestination().add(sourceDest3);
		destinationList.getDestination().add(sourceDest4);
		ObjectEventExtension.setDestinationList(destinationList);

		ILMDType iLMDType2 = new ILMDType();
		List<Object> ilmdelEmentList = new ArrayList<Object>();

		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.newDocument();
			Element leafElemtInt = doc.createElementNS("http://ns.example.com/epcis1", "example0");
			leafElemtInt.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:int");
			leafElemtInt.setTextContent(Integer.toString(count));
			ilmdelEmentList.add(leafElemtInt);

			Element leafElemtDouble = doc.createElementNS("http://ns.example.com/epcis1", "example1");

			Element leafElemtDouble1 = doc.createElementNS("http://ns.example.com/epcis1", "example12");
			leafElemtDouble1.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:double");
			leafElemtDouble1.setTextContent(Double.toString(count + 10.0));
			// ilmdelEmentList.add(leafElemtDouble1);

			leafElemtDouble.appendChild(leafElemtDouble1);
			ilmdelEmentList.add(leafElemtDouble);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iLMDType2.setAny(ilmdelEmentList);
		ObjectEventExtension.setIlmd(iLMDType2);

		List<Object> elementList = new ArrayList<Object>();
		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.newDocument();
			Element leafElemtInt = doc.createElementNS("http://ns.example.com/integerExample", "BSI:integerExample");
			leafElemtInt.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:int");
			leafElemtInt.setTextContent(Integer.toString(count));
			elementList.add(leafElemtInt);
			
			Element leafElemtStr = doc.createElementNS("http://ns.example.com/StringExample", "BSI:StringExample");
			leafElemtStr.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			leafElemtStr.setTextContent("Sample string");
			elementList.add(leafElemtStr);

			//Element leafElemtDouble = doc.createElementNS("http://ns.example.com/epcis1", "epcis1:example1");

			Element leafElemtDouble1 = doc.createElementNS("http://ns.example.com/DoubleExample", "BIS:DoubleExample");
			leafElemtDouble1.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:double");
			leafElemtDouble1.setTextContent(Double.toString(count + 10.0));
			elementList.add(leafElemtDouble1);

//			leafElemtDouble.appendChild(leafElemtDouble1);
//			elementList.add(leafElemtDouble);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		objectEventType.setAny(elementList);

		// ObjectEventExtension2Type objectEventExtension2Type =
		// new ObjectEventExtension2Type();

		// ObjectEventExtension.setExtension(objectEventExtension2Type);

		objectEventType.setExtension(ObjectEventExtension);

		return objectEventType;
	}




}
