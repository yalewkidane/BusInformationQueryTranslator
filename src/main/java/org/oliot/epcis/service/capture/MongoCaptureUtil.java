package org.oliot.epcis.service.capture;

import org.oliot.epcis.converter.service.MasterDataWriteConverter;
import org.oliot.model.epcis.VocabularyType;

public class MongoCaptureUtil {
	public String capture(VocabularyType vocabulary, String userID, String accessModifier, Integer gcpLength) {
		MasterDataWriteConverter mdConverter = new MasterDataWriteConverter();
		if (mdConverter.capture(vocabulary, gcpLength) != 0) {
			return "[ERROR] Vocabulary Capture Failed";
		} else {
			return null;
		}
	}
}
