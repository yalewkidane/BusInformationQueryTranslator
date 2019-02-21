package org.oliot.configuration;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.oliot.epcis.configuration.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.oliot.configuration", "org.oliot.controller"})
public class BusInformationQueryTranslatorApplication {

	public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		System.out.println("Wellcome To EPCIS Query Translator!");
		//HttpTest.query("https://mu.tlmat.unican.es:8443/v2/entities?limit=1&type= BusStop");
		//HttpTest.query("http://mu.tlmat.unican.es:8443/v2/entities?limit=1&type=busLine");
		new Configuration();
		SpringApplication.run(BusInformationQueryTranslatorApplication.class, args);
	}
}
