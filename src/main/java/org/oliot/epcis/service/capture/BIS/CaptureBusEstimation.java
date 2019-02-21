package org.oliot.epcis.service.capture.BIS;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.oliot.bis.service.utility.ExistingQueryService;
import org.oliot.model.BIS.BusEstimationSemantic;


public class CaptureBusEstimation {
	
	public String capture(String city) {
		String result="Error occured";
		if(city.equals("busan")) {
			result="result from busan";
			//List<BusStopSemantic> busStops=translateBusan(result);
		}else if(city.equals("santander")) {
			
			try {
				String data=ExistingQueryService.SecureQuery("https://mu.tlmat.unican.es:8443/v2/entities?limit=1000&type=BusArrivalEstimation");
				List<BusEstimationSemantic> busEstimations=translateSantadar(data);
				data=ExistingQueryService.SecureQuery("https://mu.tlmat.unican.es:8443/v2/entities?limit=470&offset=1000&type=BusArrivalEstimation");
				busEstimations.addAll(translateSantadar(data));
				System.out.println("Bus Estimation number : "+ busEstimations.size());
				CaptureEPCIS captureEPCIS=new CaptureEPCIS();
				result=captureEPCIS.capturBusEstimation(busEstimations, "santander");
			} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			result="city needs to be one of the following [busan, santander]";
		}
		return result;
	}
	
	public List<BusEstimationSemantic> translateSantadar(String data) {

		List<BusEstimationSemantic> busEstimations = new ArrayList<>();
		JSONArray jsonarray;
		try {
			jsonarray = new JSONArray(data);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				BusEstimationSemantic busEstimation=new BusEstimationSemantic();
				//id 
				try {
					String id = jsonobject.getString("id");
					busEstimation.setId(id);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//refBusStop 
				try {
					String refBusStop =jsonobject.getString("refBusStop");
				    JSONObject refBusStopObject = new JSONObject(refBusStop);
				    String refBusStopValue=refBusStopObject.getString("value");
				    busEstimation.setRefBusStop(refBusStopValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//refBusLine 
				try {
					String refBusLine =jsonobject.getString("refBusLine");
				    JSONObject refBusLineObject = new JSONObject(refBusLine);
				    String refBusLineValue=refBusLineObject.getString("value");
				    busEstimation.setRefBusLine(refBusLineValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//remainingDistances 
				try {
					String remainingDistances =jsonobject.getString("remainingDistances");
				    JSONObject remainingDistancesObject = new JSONObject(remainingDistances);
				    String remainingDistancesValue=remainingDistancesObject.getString("value");
				    busEstimation.setRemainingDistances(remainingDistancesValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//remainingTimes 
				try {
					String remainingTimes =jsonobject.getString("remainingTimes");
				    JSONObject remainingTimesObject = new JSONObject(remainingTimes);
				    String remainingTimesValue=remainingTimesObject.getString("value");
				    busEstimation.setRemainingTimes(remainingTimesValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//destinationBusLines 
				try {
					String destinationBusLines =jsonobject.getString("destinationBusLines");
				    JSONObject destinationBusLinesObject = new JSONObject(destinationBusLines);
				    String destinationBusLinesValue=destinationBusLinesObject.getString("value");
				    busEstimation.setDestinationBusLines(destinationBusLinesValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				//shortID 
				busEstimation.setShortID("NA");
				
				//remainingStations 
				busEstimation.setRemainingStations("NA");
				
				//companyName 
				busEstimation.setCompanyName("NA");
				
				
				//location 
				busEstimation.setLocation("NA");
				
				//dateModified
				try {
					String dateModified =jsonobject.getString("dateModified");
				    JSONObject dateModifiedObject = new JSONObject(dateModified);
				    String dateModifiedValue=dateModifiedObject.getString("value");
				    busEstimation.setDateModified(dateModifiedValue);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				busEstimations.add(busEstimation);
			}
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		

		return busEstimations;
	}
	
	
	public List<BusEstimationSemantic> translateBusan(String data) {

		List<BusEstimationSemantic> busStops = new ArrayList<>();

		return busStops;
	}
	
	

}
