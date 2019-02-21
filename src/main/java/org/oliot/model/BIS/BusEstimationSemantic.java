package org.oliot.model.BIS;

public class BusEstimationSemantic {
	
																				//Busan	Santander
	private String id;    //"urn:entity:busan:transport:bus:busId:<busStopId>",
	private String refBusStop;    //	Text	String								0	0
	private String refBusLine;    //	Text	String								0	0
	private String remainingDistances;    //	StructuredValue	Array<Integer>		x	0	
	private String remainingTimes;    //	StructuredValue	Array<Duration>			0	0
	private String destinationBusLines;    //	StructuredValue	Array<String>		x	0
	private String shortID;    //	Text	String									0
	private String remainingStations;    //	Number	Integer							0
	private String companyName;    //	Text	String								0
	private String location;    //	Text	String									0
	private String dateModified;    //	ISO8601	DateTime							0
	
	
	//id refBusStop refBusLine remainingDistances remainingTimes destinationBusLines shortID remainingStations companyName location dateModified 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefBusStop() {
		return refBusStop;
	}
	public void setRefBusStop(String refBusStop) {
		this.refBusStop = refBusStop;
	}
	public String getRefBusLine() {
		return refBusLine;
	}
	public void setRefBusLine(String refBusLine) {
		this.refBusLine = refBusLine;
	}
	public String getRemainingDistances() {
		return remainingDistances;
	}
	public void setRemainingDistances(String remainingDistances) {
		this.remainingDistances = remainingDistances;
	}
	public String getRemainingTimes() {
		return remainingTimes;
	}
	public void setRemainingTimes(String remainingTimes) {
		this.remainingTimes = remainingTimes;
	}
	public String getDestinationBusLines() {
		return destinationBusLines;
	}
	public void setDestinationBusLines(String destinationBusLines) {
		this.destinationBusLines = destinationBusLines;
	}
	public String getShortID() {
		return shortID;
	}
	public void setShortID(String shortID) {
		this.shortID = shortID;
	}
	public String getRemainingStations() {
		return remainingStations;
	}
	public void setRemainingStations(String remainingStations) {
		this.remainingStations = remainingStations;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	
	
	

}
