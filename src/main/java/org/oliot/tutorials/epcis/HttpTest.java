//package org.oliot.tutorials.epcis;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//import java.util.Map;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//
//
//
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.net.URL;
//import java.net.URLConnection;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import org.apache.commons.io.IOUtils;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.X509Certificate;
//
//public class HttpTest {
//	
//	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException   {
//		
//		System.out.println("Wellcome To EPCIS Query Translator!");
//		String data=HttpTest.query("https://mu.tlmat.unican.es:8443/v2/entities?limit=1&type=BusStop");
//		HttpTest.test(data); 
//		System.out.println("Program end!");
//	}
//	public static String query(String StrUrl) throws IOException, NoSuchAlgorithmException, KeyManagementException {
//
//		 /* Start of Fix */
//        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
//            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
//            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
//
//        } };
//
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, trustAllCerts, new java.security.SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//        // Create all-trusting host name verifier
//        HostnameVerifier allHostsValid = new HostnameVerifier() {
//            public boolean verify(String hostname, SSLSession session) { return true; }
//        };
//        // Install the all-trusting host verifier
//        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//        /* End of the fix*/
//        URL url = new URL(StrUrl);
//        URLConnection con = url.openConnection();
//        String result = IOUtils.toString(con.getInputStream());
//        System.out.println(result);
//        return result;
//
//	}
//	
//	static void test(String data) {
//		JSONArray jsonarray;
//		try {
//			jsonarray = new JSONArray(data);
//			for (int i = 0; i < jsonarray.length(); i++) {
//			    JSONObject jsonobject = jsonarray.getJSONObject(i);
//			    String id = jsonobject.getString("id");
//			    System.out.println("id = " +id);
//			    String type = jsonobject.getString("type");
//			    System.out.println("type = " +type);
//			    
//			    
//			    String address =jsonobject.getString("address");
//			    JSONObject addressObject = new JSONObject(address);
//			    String addresValue=addressObject.getString("value");
//			    System.out.println("adresstype = " +addresValue);
//			    
//			    String name =jsonobject.getString("name");
//			    JSONObject nameObject = new JSONObject(name);
//			    String nameValue=nameObject.getString("value");
//			    System.out.println("nameValue = " +nameValue);
//			    
//			    String direction =jsonobject.getString("direction");
//			    JSONObject directionObject = new JSONObject(direction);
//			    String directionValue=directionObject.getString("value");
//			    System.out.println("nameValue = " +directionValue);
//			    
//			    String dateModified =jsonobject.getString("dateModified");
//			    JSONObject dateModifiedObject = new JSONObject(dateModified);
//			    String dateModifiedValue=dateModifiedObject.getString("value");
//			    System.out.println("dateModifiedValue = " +dateModifiedValue);
//			    
//			    String location =jsonobject.getString("location");
//			    JSONObject locationObject = new JSONObject(location);
//			    String locationValue=locationObject.getString("value");
//			    JSONObject coordinates = new JSONObject(locationValue);
//			    String coordinatesValue=coordinates.getString("coordinates");
//			    System.out.println("coordinatesValue = " +coordinatesValue);
//			    
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//
//}
