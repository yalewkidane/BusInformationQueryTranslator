package org.oliot.model.BIS;

public class BusStopSemantic {
												//Busan	Santander
	private String id;    //"urn:entity:busan:transport:bus:busId:<busStopId>",
	private String refBuses;//	StructuredValue	List<String>		0	x
	private String shortID;//	Text	String							0	0
	private String busStopCount;//	StructuredValue	List<Integer>	
	private String name;//	Text	String							0	0
	private String location;//	geo:json	GeoJSON					0	0
	private String address;//	StructuredValue	Structured Value		x	0
	private String direction;//	Text	String						x	0
	private String refBusLines;//	StructuredValue	List<String>		0	x
	private String dateModified;//	ISO8601	ISO 8601 (DateTime)		0	0
	
	
	//id  refBuses  shortID  busStopCount  name  location  address  direction  refBusLines   dateModified
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefBuses() {
		return refBuses;
	}
	public void setRefBuses(String refBuses) {
		this.refBuses = refBuses;
	}
	public String getShortID() {
		return shortID;
	}
	public void setShortID(String shortID) {
		this.shortID = shortID;
	}
	public String getBusStopCount() {
		return busStopCount;
	}
	public void setBusStopCount(String busStopCount) {
		this.busStopCount = busStopCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getRefBusLines() {
		return refBusLines;
	}
	public void setRefBusLines(String refBusLines) {
		this.refBusLines = refBusLines;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}
	
	
	
}
