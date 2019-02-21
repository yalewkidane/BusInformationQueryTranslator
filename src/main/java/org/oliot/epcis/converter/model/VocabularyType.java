package org.oliot.epcis.converter.model;

public enum VocabularyType {
	ReadPointID("urn:epcglobal:epcis:vtype:ReadPoint"), BusinessLocationID(
			"urn:epcglobal:epcis:vtype:BusinessLocation"), BusinessStepID(
					"urn:epcglobal:epcis:vtype:BusinessStep"), DispositionID(
							"urn:epcglobal:epcis:vtype:Disposition"), BusinessTransaction(
									"urn:epcglobal:epcis:vtype:BusinessTransaction"), BusinessTrasactionTypeID(
											"urn:epcglobal:epcis:vtype:BusinessTransactionType"), SourceDestTypeID(
													"urn:epcglobal:epcis:vtype:SourceDestType"), SourceDestID(
															"urn:epcglobal:epcis:vtype:SourceDest"), EPCClass(
																	"urn:epcglobal:epcis:vtype:EPCClass"), EPCInstance(
																			"urn:epcglobal:epcis:vtype:EPCInstance");
	private String vocabularyType;

	private VocabularyType(String vocabularyType) {
		this.vocabularyType = vocabularyType;
	}

	public String getVocabularyType() {
		return vocabularyType;
	}
}

