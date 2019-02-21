package org.oliot.epcis.converter.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BsonDocument;


public class MasterData {

	private VocabularyType type;
	private String id;
	private Map<String, String> attributes;
	private List<String> children;

	public MasterData(VocabularyType type, String id) {
		this.type = type;
		this.id = id;
		attributes = new HashMap<String, String>();
		children = new ArrayList<String>();
	}

	public VocabularyType getType() {
		return type;
	}

	public void setType(VocabularyType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}

	public BsonDocument asBsonDocument() {
		CaptureUtil util = new CaptureUtil();

		BsonDocument masterData = new BsonDocument();
		masterData = util.putType(masterData, type);
		masterData = util.putID(masterData, id);
		
		if (this.attributes != null && this.attributes.isEmpty() == false) {
			masterData = util.putAttributes(masterData, attributes);
		}
		if (this.children != null && this.children.isEmpty() == false) {
			masterData = util.putChildren(masterData, children);
		}
		return masterData;
	}
}
