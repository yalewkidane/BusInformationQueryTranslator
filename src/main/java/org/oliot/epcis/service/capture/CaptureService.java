package org.oliot.epcis.service.capture;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.bson.BsonDocument;
import org.json.JSONObject;
import org.oliot.epcis.service.capture.MongoCaptureUtil;

import org.oliot.model.epcis.EPCISDocumentType;

import org.oliot.model.epcis.EPCISMasterDataDocumentType;

import org.oliot.model.epcis.VocabularyElementType;
import org.oliot.model.epcis.VocabularyType;



public class CaptureService {

	// Return null -> Succeed, not null --> error message
	public JSONObject capture(EPCISDocumentType epcisDocument, String userID, String accessModifier,
			Integer gcpLength) {
		HashMap<String, Object> retMsg = new HashMap<String, Object>();
		// Capture EPCIS Events
		//retMsg.putAll(captureEvents(epcisDocument, userID, accessModifier, gcpLength));
		// Capture EPCIS Vocabularies
		retMsg.putAll(captureVocabularies(epcisDocument, userID, accessModifier, gcpLength));
		return new JSONObject(retMsg);
	}

	public JSONObject capture(EPCISMasterDataDocumentType epcisMasterDataDocument, Integer gcpLength) {

		HashMap<String, Object> retMsg = new HashMap<String, Object>();
		try {
			List<VocabularyType> vocabularyTypeList = epcisMasterDataDocument.getEPCISBody().getVocabularyList()
					.getVocabulary();
			retMsg.putAll(captureVocabularies(vocabularyTypeList, gcpLength));
		} catch (NullPointerException ex) {

		}
		return new JSONObject(retMsg);
	}

//	@SuppressWarnings("rawtypes")
//	private BsonDocument prepareEvent(Object jaxbEvent, String userID, String accessModifier, Integer gcpLength) {
//		JAXBElement eventElement = (JAXBElement) jaxbEvent;
//		Object event = eventElement.getValue();
//		CaptureUtil.isCorrectEvent(event);
//		MongoCaptureUtil m = new MongoCaptureUtil();
//		BsonDocument doc = m.convert(event, userID, accessModifier, gcpLength);
//		return doc;
//	}

//	private HashMap<String, Object> captureEvents(EPCISDocumentType epcisDocument, String userID, String accessModifier,
//			Integer gcpLength) {
//		try {
//			List<Object> eventList = epcisDocument.getEPCISBody().getEventList()
//					.getObjectEventOrAggregationEventOrQuantityEvent();
//			List<BsonDocument> bsonDocumentList = eventList.parallelStream().parallel()
//					.map(jaxbEvent -> prepareEvent(jaxbEvent, userID, accessModifier, gcpLength))
//					.filter(doc -> doc != null).collect(Collectors.toList());
//			MongoCaptureUtil util = new MongoCaptureUtil();
//			if (bsonDocumentList != null && bsonDocumentList.size() != 0)
//				return util.capture(bsonDocumentList);
//		} catch (NullPointerException ex) {
//			// No Event
//		}
//		return new HashMap<String, Object>();
//	}

	private HashMap<String, Object> captureVocabularies(EPCISDocumentType epcisDocument, String userID,
			String accessModifier, Integer gcpLength) {
		HashMap<String, Object> retMsg = new HashMap<String, Object>();
		try {
			// Master Data in the document
			List<VocabularyType> vocabularyTypeList = epcisDocument.getEPCISHeader().getExtension().getEPCISMasterData()
					.getVocabularyList().getVocabulary();
			retMsg = captureVocabularies(vocabularyTypeList, gcpLength);
		} catch (NullPointerException ex) {
			// No vocabulary in the document
		}
		return retMsg;
	}

	public HashMap<String, Object> captureVocabularies(List<VocabularyType> vocabularyTypeList, Integer gcpLength) {

		HashMap<String, Object> retMsg = new HashMap<String, Object>();

		AtomicInteger cntVoc = new AtomicInteger(0);

		vocabularyTypeList.parallelStream().forEach(vocabulary -> {
			try {
				List<VocabularyElementType> vetList = vocabulary.getVocabularyElementList().getVocabularyElement();
				List<VocabularyElementType> vetTempList = vetList.parallelStream().collect(Collectors.toList());

				for (int j = 0; j < vetTempList.size(); j++) {
					vocabulary.getVocabularyElementList().getVocabularyElement().clear();
					vocabulary.getVocabularyElementList().getVocabularyElement().add(vetTempList.get(j));
					String message = capture(vocabulary, gcpLength);
					if (message != null) {
						retMsg.put("error", message);
					} else {
						cntVoc.incrementAndGet();
					}
				}
			} catch (NullPointerException ex) {

			}
		});

		retMsg.put("vocabularyCaptured", cntVoc);
		return retMsg;
	}

	private String capture(VocabularyType vocabulary, Integer gcpLength) {
		MongoCaptureUtil m = new MongoCaptureUtil();
		return m.capture(vocabulary, null, null, gcpLength);
	}

	
	public void capture(EPCISDocumentType epcisDocument) {
		capture(epcisDocument, null, null, null);
	}

	
	public void capture(EPCISMasterDataDocumentType epcisMasterDataDocument) {
		capture(epcisMasterDataDocument, null);
	}
}

