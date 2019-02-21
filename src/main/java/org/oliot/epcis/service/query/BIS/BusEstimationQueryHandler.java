package org.oliot.epcis.service.query.BIS;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.oliot.bis.service.utility.ExistingQueryService;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.epcis.service.capture.BIS.CaptureBusEstimation;
import org.oliot.epcis.service.capture.BIS.Translator;
import org.oliot.epcis.service.utility.BIS.ISO8601;
import org.oliot.model.BIS.BusEstimationSemantic;
import org.oliot.model.epcis.ObjectEventType;
import org.oliot.tutorials.epcis.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class BusEstimationQueryHandler extends Translator{
	
	public  List<ObjectEventType> getObjectEventType(String busStop, String busLine, String city){
		List<ObjectEventType> objectEvents=new ArrayList<>();
		
		if(city.equals("busan")) {
			try {
				try {
				busStop=busStop.substring(30);
				busLine=busLine.substring(30);
				}catch(Exception e) {
					
				}
				String url="http://data.busan.go.kr/openBus/service/busanBIMS2/busStopArr?bstopid="+busStop+"&lineid="+busLine+""
						+ "&serviceKey="+Configuration.serviceKey_busan;
				String data = ExistingQueryService.normalquery(url);
				List<BusEstimationSemantic> busEstimations=translateBusan(data);
				System.out.println("Bus Estimation converted");
				objectEvents=BusEstimationConverter.getObjectEventType(busEstimations, "busan");
				return objectEvents;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(city.equals("santander")) {
			try {
				busStop=busStop.substring(30);
				busLine=busLine.substring(30);
			}catch(Exception e) {
				
			}
			
			String url="https://mu.tlmat.unican.es:8443/v2/op/query";
			String data="[]";
			JSONObject body = new JSONObject();
			JSONArray entities = new JSONArray();
			JSONObject item = new JSONObject();
			try {
				item.put("idPattern", "urn:entity:santander:transport:bus:busArrivalEstimation:"+busStop+":"+busLine);
				item.put("type", "BusArrivalEstimation");
				
				entities.put(item);
				body.put("entities",entities);
				
				System.out.println(body.toString());
				
				data=ExistingQueryService.SecureQuery(url, body.toString());
				System.out.println(data);
				List<BusEstimationSemantic> busEstimations=(new CaptureBusEstimation()).translateSantadar(data);
				System.out.println(busEstimations.size());
				for(int i=0; i<busEstimations.size(); i++) {
					objectEvents.add(BusEstimationConverter.translate(busEstimations.get(i), "santander"));
				}
				return objectEvents;
			} catch (IOException | JSONException |KeyManagementException |NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			Configuration.logger.info("Data from "+city+" is under construction");
			objectEvents.add(new Test().getObjectEventTypeSample(5641));
		}

		return objectEvents;
		
	}
	
	
	public  List<ObjectEventType> getObjectEventTypeBusLine(String busLine, String city){
		List<ObjectEventType> objectEvents=new ArrayList<>();
		
		if(city.equals("busan")) {
			try {
				try {
				//busStop=busStop.substring(30);
				busLine=busLine.substring(30);
				}catch(Exception e) {
					
				}
				String url="http://data.busan.go.kr/openBus/service/busanBIMS2/busStopArr?lineid="+busLine+""
						+ "&serviceKey="+Configuration.serviceKey_busan;
				String data = ExistingQueryService.normalquery(url);
				List<BusEstimationSemantic> busEstimations=translateBusan(data);
				System.out.println("Bus Estimation converted");
				objectEvents=BusEstimationConverter.getObjectEventType(busEstimations, "busan");
				return objectEvents;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(city.equals("santander")) {
			try {
				//busStop=busStop.substring(30);
				busLine=busLine.substring(30);
			}catch(Exception e) {
				
			}
			
			String url="https://mu.tlmat.unican.es:8443/v2/op/query";
			String data="[]";
			JSONObject body = new JSONObject();
			JSONArray entities = new JSONArray();
			JSONObject item = new JSONObject();
			try {
				item.put("idPattern", "urn:entity:santander:transport:bus:busArrivalEstimation:*.:"+busLine);
				item.put("type", "BusArrivalEstimation");
				
				entities.put(item);
				body.put("entities",entities);
				
				//System.out.println(body.toString());
				
				data=ExistingQueryService.SecureQuery(url, body.toString());
				System.out.println(data);
				List<BusEstimationSemantic> busEstimations=(new CaptureBusEstimation()).translateSantadar(data);
				System.out.println(busEstimations.size());
				for(int i=0; i<busEstimations.size(); i++) {
					objectEvents.add(BusEstimationConverter.translate(busEstimations.get(i), "santander"));
				}
				return objectEvents;
			} catch (IOException | JSONException |KeyManagementException |NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			Configuration.logger.info("Data from "+city+" is under construction");
			objectEvents.add(new Test().getObjectEventTypeSample(5641));
		}

		return objectEvents;
		
	}
	
	public  List<ObjectEventType> getObjectEventType(String busStop, String city){
		List<ObjectEventType> objectEvents=new ArrayList<>();
		
		if(city.equals("busan")) {
			try {
				busStop=busStop.substring(30);
			}catch(Exception e) {
				
			}
			try {
				String url = "http://data.busan.go.kr/openBus/service/busanBIMS2/stopArr?bstopid=" + busStop
						+ "&serviceKey=" + Configuration.serviceKey_busan;
				String data = ExistingQueryService.normalquery(url);
				List<BusEstimationSemantic> busEstimations = translateBusan(data);
				System.out.println("Bus Estimation  [busan] [busStop] converted");
				objectEvents = BusEstimationConverter.getObjectEventType(busEstimations, "busan");
				return objectEvents;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(city.equals("santander")) {
			try {
				busStop=busStop.substring(30);
				//busLine=busLine.substring(30);
			}catch(Exception e) {
				
			}
			
			String url="https://mu.tlmat.unican.es:8443/v2/op/query";
			String data="[]";
			JSONObject body = new JSONObject();
			JSONArray entities = new JSONArray();
			JSONObject item = new JSONObject();
			try {
				item.put("idPattern", "urn:entity:santander:transport:bus:busArrivalEstimation:"+busStop);
				item.put("type", "BusArrivalEstimation");
				
				entities.put(item);
				body.put("entities",entities);
				
				//System.out.println(body.toString());
				
				data=ExistingQueryService.SecureQuery(url, body.toString());
				System.out.println(data);
				List<BusEstimationSemantic> busEstimations=(new CaptureBusEstimation()).translateSantadar(data);
				System.out.println(busEstimations.size());
				for(int i=0; i<busEstimations.size(); i++) {
					objectEvents.add(BusEstimationConverter.translate(busEstimations.get(i), "santander"));
				}
				return objectEvents;
			} catch (IOException | JSONException |KeyManagementException |NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else {
			Configuration.logger.info("Data from "+city+" is under construction");
		}
		
		return objectEvents;
		
	}
	
	public  List<BusEstimationSemantic> translateBusan(String data) {
		
		List<BusEstimationSemantic> busEstimations=new ArrayList<>();
		
		try {
			document = builder.parse(new InputSource(new StringReader(data)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NodeList nList = document.getElementsByTagName("item");
		System.out.println(nList.getLength()+ "  lines");
		// TODO   i < nList.getLength();
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			Element element = (Element) nNode;
			
			boolean hasNext=true;
			int counter=0;
			
			while(hasNext) {
				counter++;
				String carNo="carNo"+counter;
				if(element.getElementsByTagName(carNo).item(0) == null) {
					hasNext=false;
					continue;
				}
				BusEstimationSemantic busEstimation=new BusEstimationSemantic();
				//id 
				String bstopId="0000";
				if(element.getElementsByTagName("bstopId").item(0) != null) {
					bstopId=element.getElementsByTagName("bstopId").item(0).getFirstChild().getNodeValue();
				}
				busEstimation.setId("urn:epc:id:sgtin:88000269.102."+bstopId);
				
				//refBusStop 
				busEstimation.setRefBusStop("urn:epc:id:sgtin:88000269.100."+bstopId);
				
				//refBusLine 
				String lineid="0000";
				if(element.getElementsByTagName("lineid").item(0) != null) {
					lineid=element.getElementsByTagName("lineid").item(0).getFirstChild().getNodeValue();
				}
				busEstimation.setRefBusLine("urn:epc:id:sgtin:88000269.101."+lineid);
				
				//remainingDistances
				busEstimation.setRemainingDistances("NA");
				
				//remainingTimes
				String min_counter="min"+counter;
				String min_val="P0Y0M0DT0H0M0S";
				if(element.getElementsByTagName(min_counter).item(0) != null) {
					String val=element.getElementsByTagName(min_counter).item(0).getFirstChild().getNodeValue();
					min_val=ISO8601.IntervalToString("0", "0", "0", "0", val, "0");
				}
				busEstimation.setRemainingTimes(min_val);
				
				//destinationBusLines 
				busEstimation.setDestinationBusLines("NA");
				
				//shortID 
				String arsNo="arsNo";
				if(element.getElementsByTagName("arsNo").item(0) != null) {
					arsNo=element.getElementsByTagName("arsNo").item(0).getFirstChild().getNodeValue();
				}
				busEstimation.setShortID(arsNo);
				
				//remainingStations 
				String station_counter="station"+counter;
				String station_val="0";
				if(element.getElementsByTagName(station_counter).item(0) != null) {
					station_val=element.getElementsByTagName(station_counter).item(0).getFirstChild().getNodeValue();
				}
				busEstimation.setRemainingStations(station_val);
				
				//companyName 
				busEstimation.setCompanyName("NA");
				
				//location 
				String gpsX="0.0";
				if(element.getElementsByTagName("gpsX").item(0) != null) {
					gpsX=element.getElementsByTagName("gpsX").item(0).getFirstChild().getNodeValue();
				}
				
				String gpsY="0.0";
				if(element.getElementsByTagName("gpsY").item(0) != null) {
					gpsY=element.getElementsByTagName("gpsY").item(0).getFirstChild().getNodeValue();
				}
				busEstimation.setLocation("["+gpsX+","+gpsY+"]");
				
				//dateModified 
				busEstimation.setDateModified("NA");
				
				busEstimations.add(busEstimation);
				
				new Test().printBusEstimationSemantic(busEstimation);
			}
			
			
		}
		
		return busEstimations;
		
	}
	
	public static List<BusEstimationSemantic> translateSantander(String data) {

		List<BusEstimationSemantic> busEstimations = new ArrayList<>();

		return busEstimations;

	}

}
