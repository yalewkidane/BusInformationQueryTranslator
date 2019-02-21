package org.oliot.epcis.service.capture.BIS;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.oliot.bis.service.utility.ExistingQueryService;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.model.BIS.BusStopSemantic;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CaptureBusStop extends Translator{
	
	
	public String capture(String city) {
		String result="";
		if(city.equals("busan")) {
			Configuration.logger.info("Busan Bus Stop capturing started");
			try {
				String serviceKey = "OQX6Z00oyK1KwR40XLTdxvrSq90gpAsadkdL8Tgn48uArTQIBJzn7UnkUi%2FW7%2BD%2BwwEDyqkM%2Byo%2B2dm7iDocMA%3D%3D";
				String ur1_1 = "http://data.busan.go.kr/openBus/service/busanBIMS2/busStop?serviceKey=" + serviceKey
						+ "&numOfRows=1&pageNo=1";
				String data;

				data = ExistingQueryService.normalquery(ur1_1);

				double numOfRows = 100;
				int iterCount = getNumberofIteration(data, numOfRows);
				System.out.println("iterCount : " + iterCount);
				for (int i = 1; i < iterCount + 1; i++) {
					String ur1_2 = "http://data.busan.go.kr/openBus/service/busanBIMS2/busStop?serviceKey=" + serviceKey
							+ "&numOfRows=" + (int)numOfRows + "&pageNo=" + i;
					//System.out.println("pageNo : " + i+"  captured");
					data = ExistingQueryService.normalquery(ur1_2);
					CaptureEPCIS captureEPCIS = new CaptureEPCIS();
					List<BusStopSemantic> busStops = translateBusan(data);
					result += captureEPCIS.captureBusStop(busStops, "busan");
					System.out.println("pageNo : " + i+"  captured");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Configuration.logger.info("Busan Bus Stop capturing ended");
		}else if(city.equals("santander")) {
			
			try {
				String data=ExistingQueryService.SecureQuery("https://mu.tlmat.unican.es:8443/v2/entities?limit=1&type=BusStop");//445
				System.out.println(data);
				List<BusStopSemantic> busStops=translateSantadar(data);
				CaptureEPCIS captureEPCIS=new CaptureEPCIS();
				result=captureEPCIS.captureBusStop(busStops, "santander");
			} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(city.equals("daejeon")){
			result="daejeon is under constraction";
		}else {
		
			result="city needs to be one of the following [busan, santander, daejeon]";
		}
		return result;
	}
	
	
	public List<BusStopSemantic> translateSantadar(String data) {
		
		List<BusStopSemantic> busStops=new ArrayList<>();
		JSONArray jsonarray;
		try {
			jsonarray = new JSONArray(data);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				BusStopSemantic busStop=new BusStopSemantic();
				//id
				try {
					String id = jsonobject.getString("id");
					busStop.setId(id);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//refBuses	NA
				busStop.setRefBuses("NA");
				//shortID
				try {
					String shortId =jsonobject.getString("shortID");
				    JSONObject shortIDObject = new JSONObject(shortId);
				    String shortIdValue=shortIDObject.getString("value");
				    busStop.setShortID(shortIdValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//busStopCount
				busStop.setBusStopCount("NA");
				//name
				try {
					String name =jsonobject.getString("name");
				    JSONObject nameObject = new JSONObject(name);
				    String nameValue=nameObject.getString("value");
				    busStop.setName(nameValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//location
				try {
					String location =jsonobject.getString("location");
				    JSONObject locationObject = new JSONObject(location);
				    String locationValue=locationObject.getString("value");
				    JSONObject coordinates = new JSONObject(locationValue);
				    String coordinatesValue=coordinates.getString("coordinates");
				    busStop.setLocation(coordinatesValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//address
				try {
					String address =jsonobject.getString("address");
				    JSONObject addressObject = new JSONObject(address);
				    String addresValue=addressObject.getString("value");
				    busStop.setAddress(addresValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//direction
				try {
					String direction =jsonobject.getString("direction");
				    JSONObject directionObject = new JSONObject(direction);
				    String directionValue=directionObject.getString("value");
				    busStop.setDirection(directionValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//refBusLines
				//dateModified
				try {
					String dateModified =jsonobject.getString("dateModified");
				    JSONObject dateModifiedObject = new JSONObject(dateModified);
				    String dateModifiedValue=dateModifiedObject.getString("value");
				    busStop.setDateModified(dateModifiedValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				busStops.add(busStop);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return busStops;
	}
	
	//Busan 
	
	
public List<BusStopSemantic> translateBusan(String data) {
		
		List<BusStopSemantic> busStops= new ArrayList<>();
		
		try {
			document = builder.parse(new InputSource(new StringReader(data)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		NodeList nList = document.getElementsByTagName("item");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			Element element = (Element) nNode;
			BusStopSemantic busStop=new BusStopSemantic();
			
			//id  refBuses  shortID  busStopCount  name  location  address  direction  refBusLines   dateModified
			String bstopId="0000";
			if(element.getElementsByTagName("bstopId").item(0) != null) {
				bstopId=element.getElementsByTagName("bstopId").item(0).getFirstChild().getNodeValue();
			}
			busStop.setId("urn:epc:id:sgtin:88000269.100."+bstopId);
			
			//refBuses
			busStop.setRefBuses("NA");
			
			//shortID
			String bstopArsno="0000";
			if(element.getElementsByTagName("bstopArsno").item(0) != null) {
				bstopArsno=element.getElementsByTagName("bstopArsno").item(0).getFirstChild().getNodeValue();
			}
			busStop.setShortID(bstopArsno);
			
			//busStopCount
			busStop.setBusStopCount("NA");
			
			//name
			String stoptype="Default";
			if(element.getElementsByTagName("stoptype").item(0) != null) {
				stoptype=element.getElementsByTagName("stoptype").item(0).getFirstChild().getNodeValue();
			}
			busStop.setName(stoptype);
			
			//location
			String gpsX="0.0";
			if(element.getElementsByTagName("gpsX").item(0) != null) {
				gpsX=element.getElementsByTagName("gpsX").item(0).getFirstChild().getNodeValue();
			}
			String gpsY="0.0";
			if(element.getElementsByTagName("gpsY").item(0) != null) {
				gpsY=element.getElementsByTagName("gpsY").item(0).getFirstChild().getNodeValue();
			}
			busStop.setLocation("["+gpsX+","+gpsY+"]");
			
			//address
			busStop.setAddress("NA");
			
			//direction  
			busStop.setDirection("NA");
			
			//refBusLines   
			busStop.setRefBusLines("NA");
			
			//dateModified
			busStop.setDateModified("NA");
			
			busStops.add(busStop);
		}
		
		return busStops;
		
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
}
