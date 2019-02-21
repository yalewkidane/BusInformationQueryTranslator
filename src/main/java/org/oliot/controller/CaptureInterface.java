package org.oliot.controller;

import java.io.InputStream;

import javax.xml.bind.JAXB;

import org.json.JSONObject;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.epcis.service.capture.CaptureService;
import org.oliot.epcis.service.capture.CaptureUtil;
import org.oliot.model.epcis.EPCISDocumentType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CaptureInterface {

	
	@ResponseBody
	@RequestMapping(value ="/EventCapture",method = RequestMethod.GET )
    public String test() {
		System.out.println("wellcome");
        return "Welcome to capture master data Translator ";
    }
	
	@ResponseBody
	@RequestMapping(value ="/EventCapture",method = RequestMethod.POST )
	public ResponseEntity<?> post(@RequestBody String inputString, @RequestParam(required = false) String userID,
			@RequestParam(required = false) String accessToken, @RequestParam(required = false) String accessModifier,
			@RequestParam(required = false) Integer gcpLength) {
		JSONObject retMsg = new JSONObject();

		// Facebook Auth.: Access Token Validation
		if (userID != null) {
			ResponseEntity<?> isError = CaptureUtil.checkAccessToken(userID, accessToken, accessModifier);
			if (isError != null)
				return isError;
		}

		Configuration.logger.info(" EPCIS Document Capture Started.... ");

		// XSD based Validation
		if (Configuration.isCaptureVerfificationOn == true) {
			InputStream validateStream = CaptureUtil.getXMLDocumentInputStream(inputString);
			boolean isValidated = CaptureUtil.validate(validateStream,
					Configuration.wsdlPath + "/EPCglobal-epcis-1_2.xsd");
//			if (isValidated == false) {
//				return new ResponseEntity<>(
//						new String("[Error] Input EPCIS Document does not comply the standard schema"),
//						HttpStatus.BAD_REQUEST);
//			}
			Configuration.logger.info(" EPCIS Document : Validated ");

		}

		InputStream epcisStream = CaptureUtil.getXMLDocumentInputStream(inputString);
		EPCISDocumentType epcisDocument = JAXB.unmarshal(epcisStream, EPCISDocumentType.class);

		if (Configuration.isCaptureVerfificationOn == true) {
			ResponseEntity<?> error = CaptureUtil.checkDocumentHeader(epcisDocument);
			if (error != null)
				return error;
		}

		CaptureService cs = new CaptureService();
		retMsg = cs.capture(epcisDocument, userID, accessModifier, gcpLength);
		Configuration.logger.info(" EPCIS Document : Captured ");

		if (retMsg.isNull("error") == true)
			return new ResponseEntity<>(retMsg.toString(), HttpStatus.OK);
		else
			return new ResponseEntity<>(retMsg.toString(), HttpStatus.BAD_REQUEST);
	}
}
