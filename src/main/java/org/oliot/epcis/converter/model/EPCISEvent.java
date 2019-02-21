package org.oliot.epcis.converter.model;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.BsonDocument;

public abstract class EPCISEvent {
	private long eventTime;
	private String eventTimeZoneOffset;

	public EPCISEvent() {
		eventTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("XXX");
		eventTimeZoneOffset = format.format(new Date());
	}

	public EPCISEvent(long eventTime, String eventTimeZoneOffset) {
		this.eventTime = eventTime;
		this.eventTimeZoneOffset = eventTimeZoneOffset;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventTimeZoneOffset() {
		return eventTimeZoneOffset;
	}

	public void setEventTimeZoneOffset() {
		SimpleDateFormat format = new SimpleDateFormat("XXX");
		eventTimeZoneOffset = format.format(new Date());
	}

	public void setEventTimeZoneOffset(String eventTimeZoneOffset) {
		this.eventTimeZoneOffset = eventTimeZoneOffset;
	}

	public BsonDocument asBsonDocument() {
		CaptureUtil util = new CaptureUtil();

		BsonDocument baseEvent = new BsonDocument();
		// Required Fields
		baseEvent = util.putEventTime(baseEvent, eventTime);
		baseEvent = util.putEventTimeZoneOffset(baseEvent, eventTimeZoneOffset);

		return baseEvent;
	}

}

