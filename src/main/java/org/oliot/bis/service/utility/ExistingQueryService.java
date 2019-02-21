package org.oliot.bis.service.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

public class ExistingQueryService {

	
	public static String SecureQuery(String StrUrl) throws IOException, NoSuchAlgorithmException, KeyManagementException {

		 /* Start of Fix */
       TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
           public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
           public void checkClientTrusted(X509Certificate[] certs, String authType) { }
           public void checkServerTrusted(X509Certificate[] certs, String authType) { }

       } };

       SSLContext sc = SSLContext.getInstance("SSL");
       sc.init(null, trustAllCerts, new java.security.SecureRandom());
       HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

       // Create all-trusting host name verifier
       HostnameVerifier allHostsValid = new HostnameVerifier() {
           public boolean verify(String hostname, SSLSession session) { return true; }
       };
       // Install the all-trusting host verifier
       HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
       /* End of the fix*/
       URL url = new URL(StrUrl);
       URLConnection con = url.openConnection();
       
       String result = IOUtils.toString(con.getInputStream());

       
       return result;

	}
	
	public static String normalquery(String url) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.connect();

		if(con.getResponseCode() != 200) {
			System.out.println("Failed : HTTP error code : " + con.getResponseCode());
		}

		//Print response headers
		//System.out.println("\nResponse Headers:\n");
		Map<String, List<String>> map = con.getHeaderFields();
		for(Map.Entry<String, List<String>> entry : map.entrySet()) {
			//System.out.println(entry.getKey() + ": " + entry.getValue());
		}

		//Get response body and print it
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String output;
		StringBuffer response = new StringBuffer();


		while ((output = br.readLine()) != null) {
			response.append(output);
			if(output.compareTo("") != 0) {
				response.append("\n");
			}
		}
		br.close();

		con.disconnect();

		return response.toString();

	}
	
	public static String normalqueryPOST(String url, String body) throws IOException {

		URL object=new URL(url);

		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");


		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(body);
		wr.flush();

		//display what returns the POST request

		StringBuilder sb = new StringBuilder();  
		int HttpResult = con.getResponseCode(); 
		if (HttpResult == HttpURLConnection.HTTP_OK) {
		    BufferedReader br = new BufferedReader(
		            new InputStreamReader(con.getInputStream(), "utf-8"));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }
		    br.close();
		    System.out.println("" + sb.toString());  
		} else {
		    System.out.println(con.getResponseMessage());  
		}  
		return sb.toString();

	}
	
	public static String SecureQuery(String StrUrl, String body) throws IOException, NoSuchAlgorithmException, KeyManagementException {

		 /* Start of Fix */
      TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
          public void checkClientTrusted(X509Certificate[] certs, String authType) { }
          public void checkServerTrusted(X509Certificate[] certs, String authType) { }

      } };

      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      // Create all-trusting host name verifier
      HostnameVerifier allHostsValid = new HostnameVerifier() {
          public boolean verify(String hostname, SSLSession session) { return true; }
      };
      // Install the all-trusting host verifier
      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      /* End of the fix*/
      URL url = new URL(StrUrl);
      URLConnection con = url.openConnection();
      
      con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		((HttpURLConnection) con).setRequestMethod("POST");


		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(body);
		System.out.println(body);
		wr.flush();
      
      String result = IOUtils.toString(con.getInputStream());

      
      return result;

	}
}
