package org.oliot.model.BIS;

public class BusLineSematic {
	
																	//Busan	Santander
	private String id;    //"urn:entity:busan:transport:bus:busId:<busStopId>",
	private String refBusStops;    //	StructuredValue	List<String>		0	0
	private String localID;    //	Text	String							0	0
	private String shortID;    //	Text	String							0	0
	private String name;    //	Text	String								x	0
	private String refStartBusStop;    //	Text	String					0
	private String refEndBusStop;    //	Text	String						0
	private String busLineType;    //	Text	String						0
	private String startTime;    //	ISO8601	ISO 8601 (DateTime)				0
	private String endTime;    //	ISO8601	ISO 8601 (DateTime)				0
	private String intervalNorm;    //	ISO8601	ISO 8601 (Duration)			0
	private String intervalHoli;    //	ISO8601	ISO 8601 (Duration)			0
	private String intervalPeak;    //	ISO8601	ISO 8601 (Duration)			0
	private String dateModified;    //	ISO8601	ISO 8601 (DateTime)			0
	
	
	//id refBusStops localID shortID name refStartBusStop refEndBusStop busLineType startTime endTime intervalNorm intervalHoli intervalPeak dateModified 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefBusStops() {
		return refBusStops;
	}
	public void setRefBusStops(String refBusStops) {
		this.refBusStops = refBusStops;
	}
	public String getLocalID() {
		return localID;
	}
	public void setLocalID(String localID) {
		this.localID = localID;
	}
	public String getShortID() {
		return shortID;
	}
	public void setShortID(String shortID) {
		this.shortID = shortID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRefStartBusStop() {
		return refStartBusStop;
	}
	public void setRefStartBusStop(String refStartBusStop) {
		this.refStartBusStop = refStartBusStop;
	}
	public String getRefEndBusStop() {
		return refEndBusStop;
	}
	public void setRefEndBusStop(String refEndBusStop) {
		this.refEndBusStop = refEndBusStop;
	}
	public String getBusLineType() {
		return busLineType;
	}
	public void setBusLineType(String busLineType) {
		this.busLineType = busLineType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getIntervalNorm() {
		return intervalNorm;
	}
	public void setIntervalNorm(String intervalNorm) {
		this.intervalNorm = intervalNorm;
	}
	public String getIntervalHoli() {
		return intervalHoli;
	}
	public void setIntervalHoli(String intervalHoli) {
		this.intervalHoli = intervalHoli;
	}
	public String getIntervalPeak() {
		return intervalPeak;
	}
	public void setIntervalPeak(String intervalPeak) {
		this.intervalPeak = intervalPeak;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}
	
	
	
	
}
