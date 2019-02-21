package org.oliot.epcis.converter.model;

public class QuantityElement {

	// epcClass : mandatory field
	private String epcClass;
	private Double quantity;
	private String uom;

	public String getEpcClass() {
		return epcClass;
	}

	public void setEpcClass(String epcClass) {
		this.epcClass = epcClass;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

}
