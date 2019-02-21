package org.oliot.epcis.service.capture.BIS;


import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.epcis.converter.service.ObjectEventWriteConverter;
import org.oliot.epcis.service.capture.CaptureService;
import org.oliot.model.BIS.BusEstimationSemantic;
import org.oliot.model.BIS.BusLineSematic;
import org.oliot.model.BIS.BusStopSemantic;
import org.oliot.model.epcis.ActionType;
import org.oliot.model.epcis.AttributeType;
import org.oliot.model.epcis.BusinessLocationType;
import org.oliot.model.epcis.BusinessTransactionListType;
import org.oliot.model.epcis.BusinessTransactionType;
import org.oliot.model.epcis.EPC;
import org.oliot.model.epcis.EPCISEventExtensionType;
import org.oliot.model.epcis.EPCListType;
import org.oliot.model.epcis.ObjectEventType;
import org.oliot.model.epcis.ReadPointType;
import org.oliot.model.epcis.VocabularyElementListType;
import org.oliot.model.epcis.VocabularyElementType;
import org.oliot.model.epcis.VocabularyType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mongodb.client.MongoCollection;

public class CaptureEPCIS {

	public String captureBusStop(List<BusStopSemantic> busStopSemanticList, String city) {
		String result="";
		try {
			VocabularyType voc= new VocabularyType();
			if(city.equals("santander")) {
				voc.setType("urn:gs1:epcis:santander:bus:stop:info");
			}else if(city.equals("busan")) {
				voc.setType("urn:gs1:epcis:busan:bus:stop:info");
			}else {
				voc.setType("urn:gs1:epcis:app:bus:stop:info");
			}
			
			VocabularyElementListType vocElementList = new VocabularyElementListType();
			for(int i = 0; i < busStopSemanticList.size(); i++) {
				BusStopSemantic busStopSemantic=busStopSemanticList.get(i);
				VocabularyElementType vocElement = new VocabularyElementType();
				
				if(city.equals("santander")) {
					try {
						vocElement.setId("urn:epc:id:sgtin:88000269.200."+busStopSemantic.getId().substring(43));
					}catch(Exception e) {
						vocElement.setId(busStopSemantic.getId());
					}
				}else if(city.equals("busan")) {
					vocElement.setId(busStopSemantic.getId());
				}else {
					vocElement.setId("urn:epc:id:gsrn:busStopXXXXXx");
				}
				//id
				if(busStopSemantic.getId()!=null){
					AttributeType id = new AttributeType();
					id.setId("http://epcis.example.com/bus/stop/id");
					id.getContent().add(busStopSemantic.getId());
					vocElement.getAttribute().add(id);
				}
				
				//refBuses
				if(busStopSemantic.getRefBuses() != null) {
					AttributeType refBuses = new AttributeType();
					refBuses.setId("http://epcis.example.com/bus/stop/refBuses");
					refBuses.getContent().add(busStopSemantic.getRefBuses());
					vocElement.getAttribute().add(refBuses);
				}
				
				//shortID
				if(busStopSemantic.getShortID() != null) {
					AttributeType shortID = new AttributeType();
					shortID.setId("http://epcis.example.com/bus/stop/shortID");
					shortID.getContent().add(busStopSemantic.getShortID());
					vocElement.getAttribute().add(shortID);
				}
				
				//busStopCount
				if(busStopSemantic.getBusStopCount() != null) {
					AttributeType busStopCount = new AttributeType();
					busStopCount.setId("http://epcis.example.com/bus/stop/busStopCount");
					busStopCount.getContent().add(busStopSemantic.getBusStopCount());
					vocElement.getAttribute().add(busStopCount);
				}
				
				//name
				if(busStopSemantic.getName() != null) {
					AttributeType name = new AttributeType();
					name.setId("http://epcis.example.com/bus/stop/name");
					name.getContent().add(busStopSemantic.getName());
					vocElement.getAttribute().add(name);
				}
				
				//location
				if(busStopSemantic.getLocation() != null) {
					AttributeType location = new AttributeType();
					location.setId("http://epcis.example.com/bus/stop/location");
					location.getContent().add(busStopSemantic.getLocation());
					vocElement.getAttribute().add(location);
				}
				if(city.equals("santander")) {
					
					try {
						AttributeType location = new AttributeType();
						location.setId("http://epcis.example.com/bus/stop/sgln");
						location.getContent().add("urn:epc:id:sgln:8800026900016.200."+busStopSemantic.getId().substring(43));
						vocElement.getAttribute().add(location);
					}catch(Exception e) {
						AttributeType location = new AttributeType();
						location.setId("http://epcis.example.com/bus/stop/sgln");
						location.getContent().add("urn:epc:id:sgln:8800026900016.200."+busStopSemantic.getId());
						vocElement.getAttribute().add(location);
					}
				}else if(city.equals("busan")) {
					try {
						AttributeType location = new AttributeType();
						location.setId("http://epcis.example.com/bus/stop/sgln");
						location.getContent().add("urn:epc:id:sgln:8800026900016.100."+busStopSemantic.getId().substring(30));
						vocElement.getAttribute().add(location);
					}catch(Exception e) {
						AttributeType location = new AttributeType();
						location.setId("http://epcis.example.com/bus/stop/sgln");
						location.getContent().add("urn:epc:id:sgln:8800026900016.100."+busStopSemantic.getId());
						vocElement.getAttribute().add(location);
					}
				}else {
					vocElement.setId("urn:epc:id:sgln:busStopXXXXXx");
				}
				
				//refBusLines
				if(busStopSemantic.getRefBusLines() != null) {
					AttributeType refBusLines = new AttributeType();
					refBusLines.setId("http://epcis.example.com/bus/stop/refBusLines");
					refBusLines.getContent().add(busStopSemantic.getRefBusLines());
					vocElement.getAttribute().add(refBusLines);
				}
				
				//datemodified
				if(busStopSemantic.getDateModified() != null) {
					AttributeType datemodified = new AttributeType();
					datemodified.setId("http://epcis.example.com/bus/stop/datemodified");
					datemodified.getContent().add(busStopSemantic.getDateModified());
					vocElement.getAttribute().add(datemodified);
				}
				
				//address
				if(busStopSemantic.getAddress() !=null) {
					AttributeType address = new AttributeType();
					address.setId("http://epcis.example.com/bus/stop/address");
					address.getContent().add(busStopSemantic.getAddress());
					vocElement.getAttribute().add(address);
				}
				
				//direction
				if(busStopSemantic.getDirection() !=null) {
					AttributeType direction = new AttributeType();
					direction.setId("http://epcis.example.com/bus/stop/direction");
					direction.getContent().add(busStopSemantic.getDirection());
					vocElement.getAttribute().add(direction);
				}
				
				vocElementList.getVocabularyElement().add(vocElement);
			}
			
			voc.setVocabularyElementList(vocElementList);
			
			List<VocabularyType> vocabularyTypeList =new ArrayList<>();
			vocabularyTypeList.add(voc);
			CaptureService captureService=new CaptureService();
			HashMap<String, Object> retMsg = new HashMap<String, Object>();
			retMsg=captureService.captureVocabularies(vocabularyTypeList, null);
			
			result=retMsg.toString();
		}catch(Exception e) {
			result=e.toString();
		}
		
		
		return result;
	}
	
	
	public String captureBusLine(List<BusLineSematic> busLineSematicList, String city) {
		
		String result="";
		try {
			VocabularyType voc= new VocabularyType();
			if(city.equals("santander")) {
				voc.setType("urn:gs1:epcis:santander:bus:line:info");
			}else if(city.equals("busan")) {
				voc.setType("urn:gs1:epcis:busan:bus:line:info");
			}else {
				voc.setType("urn:gs1:epcisapp:bus:line:info");
			}
			
			VocabularyElementListType vocElementList = new VocabularyElementListType();
			for(int i = 0; i < busLineSematicList.size(); i++) {
				BusLineSematic busLineSematic=busLineSematicList.get(i);
				VocabularyElementType vocElement = new VocabularyElementType();
				
				
				
				
				if(city.equals("santander")) {
					try {
						vocElement.setId("urn:epc:id:sgtin:88000269.201."+busLineSematic.getId().substring(43));
					}catch(Exception e) {
						vocElement.setId(busLineSematic.getId());
					}
				}else if(city.equals("busan")) {
					vocElement.setId(busLineSematic.getId());
				}else {
					vocElement.setId("urn:epc:id:gsrn:busStopXXXXXx");
				}
				//id
				if(busLineSematic.getId()!=null){
					AttributeType id = new AttributeType();
					id.setId("http://epcis.example.com/bus/stop/id");
					id.getContent().add(busLineSematic.getId());
					vocElement.getAttribute().add(id);
				}
				
			
				//localID
				if(busLineSematic.getLocalID() != null) {
					AttributeType localID = new AttributeType();
					localID.setId("http://epcis.example.com/bus/line/localID");
					localID.getContent().add(busLineSematic.getLocalID());
					vocElement.getAttribute().add(localID);
				}
				//shortID
				if(busLineSematic.getShortID() != null) {
					AttributeType shortID = new AttributeType();
					shortID.setId("http://epcis.example.com/bus/line/shortID");
					shortID.getContent().add(busLineSematic.getShortID());
					vocElement.getAttribute().add(shortID);
				}
				//name
				if(busLineSematic.getName() != null) {
					AttributeType name = new AttributeType();
					name.setId("http://epcis.example.com/bus/line/name");
					name.getContent().add(busLineSematic.getName());
					vocElement.getAttribute().add(name);
				}
				//refBusStops
				if(busLineSematic.getRefBusStops() != null) {
					AttributeType refBusStops = new AttributeType();
					refBusStops.setId("http://epcis.example.com/bus/line/refBusStops");
					refBusStops.getContent().add(busLineSematic.getRefBusStops());
					vocElement.getAttribute().add(refBusStops);
				}
				//refStartBusStop
				if(busLineSematic.getRefStartBusStop() != null) {
					AttributeType refStartBusStop = new AttributeType();
					refStartBusStop.setId("http://epcis.example.com/bus/line/refStartBusStop");
					refStartBusStop.getContent().add(busLineSematic.getRefStartBusStop());
					vocElement.getAttribute().add(refStartBusStop);
				}
				//refEndBusStop
				if(busLineSematic.getRefEndBusStop() != null) {
					AttributeType refEndBusStop = new AttributeType();
					refEndBusStop.setId("http://epcis.example.com/bus/line/refEndBusStop");
					refEndBusStop.getContent().add(busLineSematic.getRefEndBusStop());
					vocElement.getAttribute().add(refEndBusStop);
				}
				//busLineType
				if(busLineSematic.getBusLineType() != null) {
					AttributeType busLineType = new AttributeType();
					busLineType.setId("http://epcis.example.com/bus/line/busLineType");
					busLineType.getContent().add(busLineSematic.getBusLineType());
					vocElement.getAttribute().add(busLineType);
				}
				//startTime
				if(busLineSematic.getStartTime() != null) {
					AttributeType startTime = new AttributeType();
					startTime.setId("http://epcis.example.com/bus/line/startTime");
					startTime.getContent().add(busLineSematic.getStartTime());
					vocElement.getAttribute().add(startTime);
				}
				//endTime
				if(busLineSematic.getEndTime() != null) {
					AttributeType endTime = new AttributeType();
					endTime.setId("http://epcis.example.com/bus/line/endTime");
					endTime.getContent().add(busLineSematic.getEndTime());
					vocElement.getAttribute().add(endTime);
				}
				//intervalNorm
				if(busLineSematic.getIntervalNorm() != null) {
					AttributeType intervalNorm = new AttributeType();
					intervalNorm.setId("http://epcis.example.com/bus/line/intervalNorm");
					intervalNorm.getContent().add(busLineSematic.getIntervalNorm());
					vocElement.getAttribute().add(intervalNorm);
				}
				//intervalHoli
				if(busLineSematic.getIntervalHoli() != null) {
					AttributeType intervalHoli = new AttributeType();
					intervalHoli.setId("http://epcis.example.com/bus/line/intervalHoli");
					intervalHoli.getContent().add(busLineSematic.getIntervalHoli());
					vocElement.getAttribute().add(intervalHoli);
				}
				//intervalPeak
				if(busLineSematic.getIntervalPeak() != null) {
					AttributeType intervalPeak = new AttributeType();
					intervalPeak.setId("http://epcis.example.com/bus/line/intervalPeak");
					intervalPeak.getContent().add(busLineSematic.getIntervalPeak());
					vocElement.getAttribute().add(intervalPeak);
				}
				//dateModified
				if(busLineSematic.getDateModified() != null) {
					AttributeType datemodified = new AttributeType();
					datemodified.setId("http://epcis.example.com/bus/line/datemodified");
					datemodified.getContent().add(busLineSematic.getDateModified());
					vocElement.getAttribute().add(datemodified);
				}
				vocElementList.getVocabularyElement().add(vocElement);
			}
			
			voc.setVocabularyElementList(vocElementList);
			
			List<VocabularyType> vocabularyTypeList =new ArrayList<>();
			vocabularyTypeList.add(voc);
			CaptureService captureService=new CaptureService();
			HashMap<String, Object> retMsg = new HashMap<String, Object>();
			retMsg=captureService.captureVocabularies(vocabularyTypeList, null);
			
			result=retMsg.toString();
			
		}catch(Exception e) {
			result=e.toString();
		}
		return result;
	}
	
	
	
	
	//Bus Estimation 
	public String capturBusEstimation(List<BusEstimationSemantic> busEstimations, String city) {
		String result="";
		for(int i=0; i<busEstimations.size(); i++ ) {
			BusEstimationSemantic busEstimation=busEstimations.get(i);
			ObjectEventType objectEvent=translate(busEstimation, city);
			result=captureEvents(objectEvent,busEstimation.getId());
		}
		
		
		return result;
	}
	
	
	public static ObjectEventType translate(BusEstimationSemantic busEstimation, String city) {
		ObjectEventType objectEventType=new ObjectEventType();
		
		String FormatedID="";
		if(city.equals("santander")) {
			FormatedID=busEstimation.getId().substring(56).replace(":", ".");
		}else if(city.equals("busan")) {
			try {
				FormatedID=busEstimation.getId().substring(30);
			}catch(Exception e) {
				FormatedID=busEstimation.getId();
			}
		}else {
			FormatedID="ID_from_other_city";
		}
		
		
		GregorianCalendar gRecordTime = new GregorianCalendar();
		XMLGregorianCalendar recordTime;
		try {
			recordTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gRecordTime);
			objectEventType.setEventTime(recordTime);
			objectEventType.setRecordTime(recordTime);
			objectEventType.setEventTimeZoneOffset((recordTime.getTimezone()/60)+":00");
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//objectEventType.setEventTimeZoneOffset("-06:00");

		EPCISEventExtensionType epcisEventExtension = new EPCISEventExtensionType();
		epcisEventExtension.setEventID(UUID.randomUUID().toString());
		objectEventType.setBaseExtension(epcisEventExtension);

		EPCListType objectEventEPCs = new EPCListType();
		EPC epc1 = new EPC();
		//urn:epc:id:sgtin:CompanyPrefix.ItemReference.SerialNumber 
		epc1.setValue("urn:epc:id:sgtin:88000269." + FormatedID);	
		objectEventEPCs.getEpc().add(epc1);
		objectEventType.setEpcList(objectEventEPCs);

		
		objectEventType.setAction(ActionType.fromValue("OBSERVE"));
		
		objectEventType.setBizStep("urn:epcglobal:cbv:bizstep:driving");
		
		objectEventType.setDisposition("urn:epcglobal:cbv:disp:on_the line");

		ReadPointType readPoint = new ReadPointType();
		//urn:epc:id:sgtin:CompanyPrefix.ItemReference.SerialNumber 
		readPoint.setId("urn:epc:id:sgln:8800026900016." + FormatedID);
		objectEventType.setReadPoint(readPoint);

		BusinessLocationType businessLocation = new BusinessLocationType();
		//urn:epc:id:sgln:CompanyPrefix.ItemReference.SerialNumber
		businessLocation.setId("urn:epc:id:sgln:8800026900016.103" + FormatedID);
		objectEventType.setBizLocation(businessLocation);

		BusinessTransactionListType businessTransactionList = new BusinessTransactionListType();
		BusinessTransactionType businessTransaction1 = new BusinessTransactionType();
		businessTransaction1.setType("urn:epcglobal:cbv:Bus:status");
		businessTransaction1.setValue("http://transaction.acme.com/po/urn:epcglobal:cbv:bizstep:driving");
		businessTransactionList.getBizTransaction().add(businessTransaction1);
		objectEventType.setBizTransactionList(businessTransactionList);

		List<Object> elementList = new ArrayList<Object>();

		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.newDocument();
//			Element leafElemtStr = doc.createElementNS("http://ns.example.com/StringExample", "BIS:StringExample");
//			leafElemtStr.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
//			leafElemtStr.setTextContent("Sample string");
//			elementList.add(leafElemtStr);

			// id
			Element idElement = doc.createElementNS("http://ns.example.com/id", "BIS:id");
			idElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			idElement.setTextContent(busEstimation.getId());
			elementList.add(idElement);

			// refBusStop
			Element refBusStopElement = doc.createElementNS("http://ns.example.com/refBusStop", "BIS:refBusStop");
			//refBusStopElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			refBusStopElement.setTextContent(busEstimation.getRefBusStop());
			elementList.add(refBusStopElement);

			// refBusLine
			Element refBusLineElement = doc.createElementNS("http://ns.example.com/refBusLine", "BIS:refBusLine");
			refBusLineElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			refBusLineElement.setTextContent(busEstimation.getRefBusLine());
			elementList.add(refBusLineElement);

			// remainingDistances
			Element remainingDistancesElement = doc.createElementNS("http://ns.example.com/remainingDistances",
					"BIS:remainingDistances");
			remainingDistancesElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			remainingDistancesElement.setTextContent(busEstimation.getRemainingDistances());
			elementList.add(remainingDistancesElement);

			// remainingTimes
			Element remainingTimesElement = doc.createElementNS("http://ns.example.com/remainingTimes",
					"BIS:remainingTimes");
			remainingTimesElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			remainingTimesElement.setTextContent(busEstimation.getRemainingTimes());
			elementList.add(remainingTimesElement);

			// destinationBusLines
			Element destinationBusLinesElement = doc.createElementNS("http://ns.example.com/destinationBusLines",
					"BIS:destinationBusLines");
			destinationBusLinesElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			destinationBusLinesElement.setTextContent(busEstimation.getDestinationBusLines());
			elementList.add(destinationBusLinesElement);

			// shortID
			Element shortIDElement = doc.createElementNS("http://ns.example.com/shortID", "BIS:shortID");
			shortIDElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			shortIDElement.setTextContent(busEstimation.getShortID());
			elementList.add(shortIDElement);

			// remainingStations
			Element remainingStationsElement = doc.createElementNS("http://ns.example.com/remainingStations",
					"BIS:remainingStations");
			remainingStationsElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			remainingStationsElement.setTextContent(busEstimation.getRemainingStations());
			elementList.add(remainingStationsElement);

			// companyName
			Element companyNameElement = doc.createElementNS("http://ns.example.com/companyName", "BIS:companyName");
			companyNameElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			companyNameElement.setTextContent(busEstimation.getCompanyName());
			elementList.add(companyNameElement);

			// location
			Element locationElement = doc.createElementNS("http://ns.example.com/location", "BIS:location");
			locationElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			locationElement.setTextContent(busEstimation.getLocation());
			elementList.add(companyNameElement);

			// dateModified
			Element dateModifiedElement = doc.createElementNS("http://ns.example.com/dateModified", "BIS:dateModified");
			dateModifiedElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type", "xs:string");
			dateModifiedElement.setTextContent(busEstimation.getDateModified());
			elementList.add(dateModifiedElement);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		objectEventType.setAny(elementList);

		return objectEventType;
	}
	
	
	public  String  captureEvents(ObjectEventType objectEvent, String id ) {
		ObjectEventWriteConverter objConv=new ObjectEventWriteConverter();
		BsonDocument bsonDocument=objConv.convert(objectEvent, null, id);
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
}
