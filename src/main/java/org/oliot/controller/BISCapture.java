package org.oliot.controller;


import java.util.Timer;

import org.oliot.epcis.service.capture.BIS.CaptureBusLine;
import org.oliot.epcis.service.capture.BIS.CaptureBusStop;
import org.oliot.epcis.service.capture.BIS.EventCapturer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class BISCapture {
	
	private Timer timer;
	/*
	 * /BIS/CaptureMasterData
	 * city=busan/santander
	 * type=busStop/busLine
	 */
	@ResponseBody
	@RequestMapping(value = "/BIS", method = RequestMethod.GET)
    public String index() {
        return "List of services: [/BIS/CaptureMasterData] \n "
        		+ "/BIS/CaptureMasterData?city=&type=  \n  "
        		+ "city=busan/santander	\n type=busStop/busLine";
    }
	
	@ResponseBody
	@RequestMapping(value = "/BIS/test", method = RequestMethod.GET)
    public String Test() {
		
		
		return "Test";
    }
	
	
	
	
	@RequestMapping(value = "/BIS/CaptureMasterData", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> startMastereCapture(@RequestParam(value = "city") final String city,
			@RequestParam(value = "type") final String type){
		
		String result="";
		
		if(type.equals("busStop")) {
			
			CaptureBusStop captureBusStop=new CaptureBusStop();
			result=captureBusStop.capture(city);
			
		}else if(type.equals("busLine")) {
			CaptureBusLine captureBusLine= new CaptureBusLine();
			result=captureBusLine.capture(city);
		}else {
			result="type needs to be one of the following [busStop, busLine]";
		}
		
		return new ResponseEntity<String>(new String(result), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/BIS/startEventCapture", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> startEventCapture(@RequestParam(value = "city") final  String city){
		
		 String result="Event capturing started";
		
		if(city.equals("busan") || city.equals("santander") || city.equals("daejeon")) {
			EventCapturer eventCapturer=new EventCapturer(city);
			timer = new Timer();
			long delay=0;
			long period=60000;
			timer.schedule(eventCapturer, delay, period);
			
		}else {
			result="type needs to be one of the following [busStop, busLine]";
		}
		
		return new ResponseEntity<String>(new String(result), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/stopEventCapture", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> stopEventCapture() {
		
		timer.cancel();
		timer.purge();
		
		return new ResponseEntity<String>(new String("Event capturing is stoped."), HttpStatus.OK);
	}

}
