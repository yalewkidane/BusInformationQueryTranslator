package org.oliot.epcis.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.oliot.epcis.service.subscription.Subscription;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import redis.clients.jedis.Jedis;

public class Configuration {
	public static Logger logger;
	public static String webInfoPath;
	public static String wsdlPath;
	public static boolean isCaptureVerfificationOn;
	public static String facebookAppID;
	public static String adminID;
	public static String adminScope;
	public static boolean isQueryAccessControlOn;
	public static boolean isTriggerSupported;
	public static MongoClient mongoClient;
	public static Jedis jedisClient;
	public static MongoDatabase mongoDatabase;
	public static String backend_ip;
	public static int backend_port;
	public static String databaseName;
	public static JSONObject json;
	public static String ac_api_address;
	public static String epcis_id;
	public static String serviceKey_busan;
	
	public Configuration(){
		// Set Logger
		setLogger();

		// Set Basic Configuration with Configuration.json
		setBasicConfiguration();

		// load existing subscription
		//loadExistingSubscription();
	}
	
	private void setLogger() {
		// Log4j Setting
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		Configuration.logger = Logger.getRootLogger();
	}
	
	private void setBasicConfiguration() {
		serviceKey_busan="OQX6Z00oyK1KwR40XLTdxvrSq90gpAsadkdL8Tgn48uArTQIBJzn7UnkUi%2FW7%2BD%2BwwEDyqkM%2Byo%2B2dm7iDocMA%3D%3D";
		 ClassLoader classLoader = getClass().getClassLoader();
		   // File file = new File(classLoader.getResource("Configuration.json").getFile());
		 String jsonString=""; 
		 
//		    File file = new File(classLoader.getResource("Configuration.json").getFile());
//		   // File file = new File(getClass().getResource("text.txt").getFile());
//		   // JsonNode mySchema = JsonLoader.fromFile(file);
//		    String jsonString="";
//		    BufferedReader br = null;
//			FileReader fr = null;
		    try {

//				//br = new BufferedReader(new FileReader(FILENAME));
//				fr = new FileReader(file);
//				br = new BufferedReader(fr);
//
//				String sCurrentLine;
//
//				while ((sCurrentLine = br.readLine()) != null) {
//					jsonString+=sCurrentLine;
//				}
		    	jsonString = IOUtils.toString(classLoader.getResourceAsStream("Configuration.json"));
				json = new JSONObject(jsonString);
				
				// Set up capture_verification
				String captureVerification = json.getString("capture_verification");
				if (captureVerification == null) {
					Configuration.logger.error(
							"capture_verification is null, please make sure Configuration.json is correct, and restart.");
				}
				
				captureVerification = captureVerification.trim();
				if (captureVerification.equals("on")) {
					Configuration.isCaptureVerfificationOn = true;
					Configuration.logger.info("Capture_Verification - ON ");
				} else if (captureVerification.equals("off")) {
					Configuration.isCaptureVerfificationOn = false;
					Configuration.logger.info("Capture_Verification - OFF ");
				} else {
					Configuration.logger.error(
							"capture_verification should be (on|off), please make sure Configuration.json is correct, and restart.");
				}
				
				// Query Access Control
				// Set up capture_verification
				String queryAC = json.getString("query_access_control");
				if (queryAC == null) {
					Configuration.logger
							.error("query_access_control, please make sure Configuration.json is correct, and restart.");
				}
				queryAC = queryAC.trim();
				if (queryAC.equals("on")) {
					Configuration.isQueryAccessControlOn = true;
					Configuration.logger.info("Query_AccessControl - ON ");
				} else if (queryAC.equals("off")) {
					Configuration.isQueryAccessControlOn = false;
					Configuration.logger.info("Query_AccessControl - OFF ");
				} else {
					Configuration.logger.error(
							"query_access_control should be (on|off), please make sure Configuration.json is correct, and restart.");
				}
				
				// Facebook Application ID
				String fai = json.getString("facebook_app_id");
				if (fai == null) {
					Configuration.logger
							.error("facebook_app_id, please make sure Configuration.json is correct, and restart.");
				}
				facebookAppID = fai.trim();
				
				
				// Admin Facebook ID
				String aID = json.getString("admin_facebook_id");
				if (aID == null) {
					Configuration.logger
							.error("admin_facebook_id, please make sure Configuration.json is correct, and restart.");
				}
				adminID = aID.trim();
				
				// Admin Scope
				String aScope = json.getString("admin_scope");
				if (aScope == null) {
					Configuration.logger.error("admin_scope, please make sure Configuration.json is correct, and restart.");
				}
				adminScope = aScope.trim();

				setMongoDB(json);
				
				// Trigger Support
				String triggerSupport = json.getString("trigger_support");
				if (triggerSupport == null || triggerSupport.trim().equals("on")) {
					isTriggerSupported = true;
				} else {
					isTriggerSupported = false;
				}
				
				
				// Set IP Address of Access Control API

				String address = json.getString("ac_api_address");
				if (address == null) {
					Configuration.logger
							.error("ac_api_address is null, please make sure Configuration.json is correct, and restart.");
				}

				ac_api_address = address.trim();

				String id = json.getString("epcis_id");
				if (id == null) {
					Configuration.logger
							.error("ac_api_address is null, please make sure Configuration.json is correct, and restart.");
				}

				epcis_id = id.trim();
				
				// Set Redis Database for caching
				//11. (Yalew Cache)
				jedisClient = new Jedis("localhost"); 
			    System.out.println("Connection to server sucessfully"); 
			    //set the data in redis string 
				
				

			} catch (IOException e) {

				e.printStackTrace();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Configuration.logger.info(jsonString);
		    
	}
	
	
	private void setMongoDB(JSONObject json) throws JSONException {
		if (json.isNull("backend_ip")) {
			backend_ip = "localhost";
		} else {
			backend_ip = json.getString("backend_ip");
		}
		if (json.isNull("backend_port")) {
			backend_port = 27017;
		} else {
			backend_port = json.getInt("backend_port");
		}
		if (json.isNull("backend_database_name")) {
			databaseName = "epcis";
		} else {
			databaseName = json.getString("backend_database_name");
		}
		mongoClient = new MongoClient(backend_ip, backend_port);
		mongoDatabase = mongoClient.getDatabase(databaseName);
	}
	
	
	/**
	 * dropMongoDB Jaehee created lovesm135@kaist.ac.kr 2016.11.04
	 * 
	 * @param dbname
	 */
	public static void dropMongoDB() {
		mongoClient.dropDatabase(databaseName);
	}

	private void loadExistingSubscription() {

		Subscription ms = new Subscription();
		ms.init();
	}

	/**
	 * query_access_relation created 2017.02.07
	 * 
	 * @param quri
	 * @param qtoken
	 * @param qurlParameters
	 * @return result
	 */
	public static String query_access_relation(String quri, String qtoken, String qurlParameters) {
		Configuration.logger.info(" Client Token retrieve");
		StringBuffer response = null;
		String result = null;

		try {
			String url = quri;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add request header
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "Bearer " + qtoken);
			con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

			// Send get request
			System.out.println(con.getRequestMethod());

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// print result

		if (response != null) {
			result = response.toString();
		}

		return result;
	}
	
}
