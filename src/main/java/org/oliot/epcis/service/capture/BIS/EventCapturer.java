package org.oliot.epcis.service.capture.BIS;

import java.util.TimerTask;

public class EventCapturer extends TimerTask{
	String city;
	static int counter=1;
	
	public EventCapturer(String city){
		this.city=city;
	}
	
	public void run() {
		System.out.println("Update : "+counter+"   Started");
		long startTime = System.currentTimeMillis();
		CaptureBusEstimation captureBusEstimation=new CaptureBusEstimation();
		captureBusEstimation.capture(city);
		System.out.println("estimatedTime   : "+(System.currentTimeMillis() - startTime)+"   Millis");
		System.out.println("Update : "+counter+"   Ended");
		counter++;
	}

}
