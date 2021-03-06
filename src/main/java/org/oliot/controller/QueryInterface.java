package org.oliot.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.epcis.service.query.QueryService;
import org.oliot.model.epcis.DuplicateSubscriptionException;
import org.oliot.model.epcis.ImplementationException;
import org.oliot.model.epcis.InvalidURIException;
import org.oliot.model.epcis.NoSuchNameException;
import org.oliot.model.epcis.NoSuchSubscriptionException;
import org.oliot.model.epcis.PollParameters;
import org.oliot.model.epcis.QueryParameterException;
import org.oliot.model.epcis.QueryTooComplexException;
import org.oliot.model.epcis.QueryTooLargeException;
import org.oliot.model.epcis.SubscribeNotPermittedException;
import org.oliot.model.epcis.SubscriptionControlsException;
import org.oliot.model.epcis.SubscriptionType;
import org.oliot.model.epcis.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

@Controller
public class QueryInterface {

	@RequestMapping("/")
    public String home() {
		//System.out.println("Home is being processed");
        return "Home";
    }
	
	
	@RequestMapping("/login")
    public String login() {
		//System.out.println("Home is being processed");
        return "login";
    }
	
	
	
	@ResponseBody
	@RequestMapping("/test")
    public String index() {
		System.out.println("wellcome");
        return "Welcome to query Translator ";
    }
	
	/**
	 * Registers a subscriber for a previously defined query having the
	 * specified name. The params argument provides the values to be used for
	 * any named parameters defined by the query. The dest parameter specifies a
	 * destination where results from the query are to be delivered, via the
	 * Query Callback Interface. The dest parameter is a URI that both
	 * identifies a specific binding of the Query Callback Interface to use and
	 * specifies addressing information. The controls parameter controls how the
	 * subscription is to be processed; in particular, it specifies the
	 * conditions under which the query is to be invoked (e.g., specifying a
	 * periodic schedule). The subscriptionID is an arbitrary string that is
	 * copied into every response delivered to the specified destination, and
	 * otherwise not interpreted by the EPCIS service. The client may use the
	 * subscriptionID to identify from which subscription a given result was
	 * generated, especially when several subscriptions are made to the same
	 * destination. The dest argument MAY be null or empty, in which case
	 * results are delivered to a pre-arranged destination based on the
	 * authenticated identity of the caller. If the EPCIS implementation does
	 * not have a destination pre-arranged for the caller, or does not permit
	 * this usage, it SHALL raise an InvalidURIException.
	 * 
	 * @throws QueryParameterException
	 */
	@RequestMapping(value = "/Subscribe/{queryName}/{subscriptionID}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> subscribe(@PathVariable String queryName, @PathVariable String subscriptionID,
			@RequestParam String dest, @RequestParam(required = false) String schedule,
			@RequestParam(required = false) String trigger, @RequestParam(required = false) String initialRecordTime,
			@RequestParam Boolean reportIfEmpty, @RequestParam(required = false) String eventType,
			@RequestParam(required = false) String GE_eventTime, @RequestParam(required = false) String LT_eventTime,
			@RequestParam(required = false) String GE_recordTime, @RequestParam(required = false) String LT_recordTime,
			@RequestParam(required = false) String EQ_action, @RequestParam(required = false) String EQ_bizStep,
			@RequestParam(required = false) String EQ_disposition, @RequestParam(required = false) String EQ_readPoint,
			@RequestParam(required = false) String WD_readPoint, @RequestParam(required = false) String EQ_bizLocation,
			@RequestParam(required = false) String WD_bizLocation,
			@RequestParam(required = false) String EQ_transformationID,
			@RequestParam(required = false) String MATCH_epc, @RequestParam(required = false) String MATCH_parentID,
			@RequestParam(required = false) String MATCH_inputEPC,
			@RequestParam(required = false) String MATCH_outputEPC, @RequestParam(required = false) String MATCH_anyEPC,
			@RequestParam(required = false) String MATCH_epcClass,
			@RequestParam(required = false) String MATCH_inputEPCClass,
			@RequestParam(required = false) String MATCH_outputEPCClass,
			@RequestParam(required = false) String MATCH_anyEPCClass,
			@RequestParam(required = false) Integer EQ_quantity, @RequestParam(required = false) Integer GT_quantity,
			@RequestParam(required = false) Integer GE_quantity, @RequestParam(required = false) Integer LT_quantity,
			@RequestParam(required = false) Integer LE_quantity, @RequestParam(required = false) String EQ_eventID,
			@RequestParam(required = false) Boolean EXISTS_errorDeclaration,
			@RequestParam(required = false) String GE_errorDeclarationTime,
			@RequestParam(required = false) String LT_errorDeclarationTime,
			@RequestParam(required = false) String EQ_errorReason,
			@RequestParam(required = false) String EQ_correctiveEventID, @RequestParam(required = false) String orderBy,
			@RequestParam(required = false) String orderDirection,
			@RequestParam(required = false) Integer eventCountLimit,
			@RequestParam(required = false) Integer maxEventCount,
			@RequestParam(required = false) String vocabularyName,
			@RequestParam(required = false) Boolean includeAttributes,
			@RequestParam(required = false) Boolean includeChildren,
			@RequestParam(required = false) String attributeNames, @RequestParam(required = false) String EQ_name,
			@RequestParam(required = false) String WD_name, @RequestParam(required = false) String HASATTR,
			@RequestParam(required = false) Integer maxElementCount, @RequestParam(required = false) String format,
			@RequestParam(required = false) String userID, @RequestParam(required = false) String accessToken,
			@RequestParam Map<String, String> params)
			throws NoSuchNameException, InvalidURIException, DuplicateSubscriptionException, QueryParameterException,
			QueryTooComplexException, SubscriptionControlsException, SubscribeNotPermittedException, SecurityException,
			ValidationException, ImplementationException {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		if (initialRecordTime != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				sdf.parse(initialRecordTime);
			} catch (ParseException e) {
				String error = e.toString();
				return new ResponseEntity<>(error, responseHeaders, HttpStatus.BAD_REQUEST);
			}
		}
		// Access Control is not mandatory
		// However, if fid and accessToken provided, more information provided
		/*
		FacebookClient fc = null;
		List<String> friendList = null;
		if (userID != null) {
			// Check accessToken
			fc = OAuthUtil.isValidatedFacebookClient(accessToken, userID);
			if (fc == null) {
				return new ResponseEntity<>(new String("Unauthorized Token"), responseHeaders, HttpStatus.UNAUTHORIZED);
			}
			friendList = new ArrayList<String>();

			Connection<User> friendConnection = fc.fetchConnection("me/friends", User.class);
			for (List<User> friends : friendConnection) {
				for (User friend : friends) {
					friendList.add(friend.getId());
				}
			}
		}
*/
		
		
		
		
		// reportIfEmpty
		PollParameters p = new PollParameters(queryName, eventType, GE_eventTime, LT_eventTime, GE_recordTime,
				LT_recordTime, EQ_action, EQ_bizStep, EQ_disposition, EQ_readPoint, WD_readPoint, EQ_bizLocation,
				WD_bizLocation, EQ_transformationID, MATCH_epc, MATCH_parentID, MATCH_inputEPC, MATCH_outputEPC,
				MATCH_anyEPC, MATCH_epcClass, MATCH_inputEPCClass, MATCH_outputEPCClass, MATCH_anyEPCClass, EQ_quantity,
				GT_quantity, GE_quantity, LT_quantity, LE_quantity, EQ_eventID, EXISTS_errorDeclaration,
				GE_errorDeclarationTime, LT_errorDeclarationTime, EQ_errorReason, EQ_correctiveEventID, orderBy,
				orderDirection, eventCountLimit, maxEventCount, vocabularyName, includeAttributes, includeChildren,
				attributeNames, EQ_name, WD_name, HASATTR, maxElementCount, format, params);
		SubscriptionType s = new SubscriptionType(subscriptionID, dest, schedule, trigger, initialRecordTime,
				reportIfEmpty, p);

		//Need implementation 
		String result="under constraction";
		/*
		MongoQueryService mongoQueryService = new MongoQueryService();
		String result = mongoQueryService.subscribe(s, userID, friendList);
		*/
		return new ResponseEntity<>(result, responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value = "/Unsubscribe/{subscriptionID}", method = RequestMethod.GET)
	public ResponseEntity<?> unsubscribe(@PathVariable String subscriptionID)
			throws NoSuchSubscriptionException, ValidationException, ImplementationException {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		//Need implementation 
		/*
		MongoQueryService mongoQueryService = new MongoQueryService();
		mongoQueryService.unsubscribe(subscriptionID);
		*/
		return new ResponseEntity<>(new String("Subscription " + subscriptionID + " : Unsubscribed"), responseHeaders,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/GetSubscriptionIDs/{queryName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSubscriptionIDsREST(@PathVariable String queryName)
			throws NoSuchNameException, SecurityException, ValidationException, ImplementationException {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		//Need implementation 
		String result="Error";
		/*
		MongoQueryService mongoQueryService = new MongoQueryService();
		String result = mongoQueryService.getSubscriptionIDsREST(queryName);
		*/
		return new ResponseEntity<>(result, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Returns a list of all query names available for use with the subscribe
	 * and poll methods. This includes all pre-defined queries provided by the
	 * implementation, including those specified in Section 8.2.7.
	 * 
	 * No Dependency with Backend
	 * 
	 * @return JSONArray of query names ( String )
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/GetQueryNames", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getQueryNamesREST()
			throws SecurityException, ValidationException, ImplementationException, JSONException {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");

		JSONArray jsonArray = new JSONArray();
		List<String> queryNames = getQueryNames();
		for (int i = 0; i < queryNames.size(); i++) {
			jsonArray.put(queryNames.get(i));
		}

		return new ResponseEntity<>(jsonArray.toString(1), responseHeaders, HttpStatus.OK);
	}

	/**
	 * Returns a list of all query names available for use with the subscribe
	 * and poll methods. This includes all pre-defined queries provided by the
	 * implementation, including those specified in Section 8.2.7.
	 * 
	 * @return a list of all query names
	 */
	public List<String> getQueryNames() throws SecurityException, ValidationException, ImplementationException {
		List<String> queryNames = new ArrayList<String>();
		queryNames.add("SimpleEventQuery");
		queryNames.add("SimpleMasterDataQuery");
		return queryNames;
	}

	/**
	 * Returns a string that identifies what version of the specification this
	 * implementation complies with. The possible values for this string are
	 * defined by GS1. An implementation SHALL return a string corresponding to
	 * a version of this specification to which the implementation fully
	 * complies, and SHOULD return the string corresponding to the latest
	 * version to which it complies. To indicate compliance with this Version
	 * 1.2 of the EPCIS specification, the implementation SHALL return the
	 * string 1.2 .
	 * 
	 * @return 1.2
	 */
	@RequestMapping(value = "/GetStandardVersion", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getStandardVersion()
			throws SecurityException, ValidationException, ImplementationException {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<>(new String("1.2"), responseHeaders, HttpStatus.OK);
	}

	/**
	 * Returns a string that identifies what vendor extensions this
	 * implementation provides. The possible values of this string and their
	 * meanings are vendor-defined, except that the empty string SHALL indicate
	 * that the implementation implements only standard functionality with no
	 * vendor extensions. When an implementation chooses to return a non-empty
	 * string, the value returned SHALL be a URI where the vendor is the owning
	 * authority. For example, this may be an HTTP URL whose authority portion
	 * is a domain name owned by the vendor, a URN having a URN namespace
	 * identifier issued to the vendor by IANA, an OID URN whose initial path is
	 * a Private Enterprise Number assigned to the vendor, etc.
	 * 
	 * @return a string of vendor version
	 */
	@RequestMapping(value = "/GetVendorVersion", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getVendorVersion() throws SecurityException, ValidationException, ImplementationException {
		// It is not a version of Vendor
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		return new ResponseEntity<>(new String("org.oliot.epcis-1.2.10"), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value = "/Poll/{queryName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> poll(@PathVariable String queryName, @RequestParam(required = false) String eventType,
			@RequestParam(required = false) String GE_eventTime, @RequestParam(required = false) String LT_eventTime,
			@RequestParam(required = false) String GE_recordTime, @RequestParam(required = false) String LT_recordTime,
			@RequestParam(required = false) String EQ_action, @RequestParam(required = false) String EQ_bizStep,
			@RequestParam(required = false) String EQ_disposition, @RequestParam(required = false) String EQ_readPoint,
			@RequestParam(required = false) String WD_readPoint, @RequestParam(required = false) String EQ_bizLocation,
			@RequestParam(required = false) String WD_bizLocation,
			@RequestParam(required = false) String EQ_transformationID,
			@RequestParam(required = false) String MATCH_epc, @RequestParam(required = false) String MATCH_parentID,
			@RequestParam(required = false) String MATCH_inputEPC,
			@RequestParam(required = false) String MATCH_outputEPC, @RequestParam(required = false) String MATCH_anyEPC,
			@RequestParam(required = false) String MATCH_epcClass,
			@RequestParam(required = false) String MATCH_inputEPCClass,
			@RequestParam(required = false) String MATCH_outputEPCClass,
			@RequestParam(required = false) String MATCH_anyEPCClass,
			@RequestParam(required = false) Integer EQ_quantity, @RequestParam(required = false) Integer GT_quantity,
			@RequestParam(required = false) Integer GE_quantity, @RequestParam(required = false) Integer LT_quantity,
			@RequestParam(required = false) Integer LE_quantity,

			@RequestParam(required = false) String EQ_eventID,
			@RequestParam(required = false) Boolean EXISTS_errorDeclaration,
			@RequestParam(required = false) String GE_errorDeclarationTime,
			@RequestParam(required = false) String LT_errorDeclarationTime,
			@RequestParam(required = false) String EQ_errorReason,
			@RequestParam(required = false) String EQ_correctiveEventID,

			@RequestParam(required = false) String orderBy, @RequestParam(required = false) String orderDirection,
			@RequestParam(required = false) Integer eventCountLimit,
			@RequestParam(required = false) Integer maxEventCount,

			@RequestParam(required = false) String vocabularyName,
			@RequestParam(required = false) Boolean includeAttributes,
			@RequestParam(required = false) Boolean includeChildren,
			@RequestParam(required = false) String attributeNames, @RequestParam(required = false) String EQ_name,
			@RequestParam(required = false) String WD_name, @RequestParam(required = false) String HASATTR,
			@RequestParam(required = false) Integer maxElementCount,

			@RequestParam(required = false) String format, @RequestParam(required = false) String userID,
			@RequestParam(required = false) String accessToken, @RequestParam(required = false) String accessMode,
			@RequestParam Map<String, String> params)
			throws QueryParameterException, QueryTooLargeException, QueryTooComplexException, NoSuchNameException,
			SecurityException, ValidationException, ImplementationException, JSONException {

		HttpHeaders responseHeaders = new HttpHeaders();
		if (format != null && format.equals("JSON")) {
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		} else {
			responseHeaders.add("Content-Type", "application/xml; charset=utf-8");
		}
		// Access Control is not mandatory
		// However, if fid and accessToken provided, more information provided
	//	FacebookClient fc = null;
		List<String> friendList = null;

		if (accessMode != null && accessMode.equals("facebook")) {
//			if (userID != null) {
//				// Check accessToken
//				fc = OAuthUtil.isValidatedFacebookClient(accessToken, userID);
//				if (fc == null) {
//					return new ResponseEntity<>(new String("Unauthorized Token"), responseHeaders,
//							HttpStatus.UNAUTHORIZED);
//				}
//				friendList = new ArrayList<String>();
//
//				Connection<User> friendConnection = fc.fetchConnection("me/friends", User.class);
//				for (List<User> friends : friendConnection) {
//					for (User friend : friends) {
//						friendList.add(friend.getId());
//					}
//				}
//			}
		} else if (accessMode != null && accessMode.equals("custom")) {
			/* jaeheeHa2 AC_query service */

			// 1. first check the subscribing authorization
			// url of ac_api server

			// Checking subscribe authorization

			// If there is no subscription right
			// pop up this . return new ResponseEntity<>("No accessRight",
			// HttpStatus.BAD_REQUEST);

			// url of ac_api server
			
			boolean pass = false;
			String quri = null;
			String qurlParameters = null;
			String query_result = null;
			
			//(Yalew Cache)11. check subscribing authorization using token
			
			//if redis have user and token
			
			Jedis RedisCL = Configuration.jedisClient;
			String result = RedisCL.get(userID+"-subscribe");	
					
			if(result == null || !(result.equals(accessToken))){
				quri = "http://" + Configuration.ac_api_address + "/user/" + userID + "/epcis/"
						+ Configuration.epcis_id + "/subscribe";

				// query to ac_api server
				qurlParameters = "";
				query_result = Configuration.query_access_relation(quri, accessToken, qurlParameters);

				// for debug, erase after implementing.
				Configuration.logger.info(query_result);
				query_result = query_result.replaceAll("[\"{} ]", "").split(":")[1];

				pass = (query_result.equals("yes")) ? true : false;
				if(pass){
					RedisCL.set(userID+"-subscribe", accessToken);
				}
			}
			else{
				pass = true;
			}
			
			if (!pass) {
				return new ResponseEntity<>(new String("no subscribe auth"), HttpStatus.BAD_REQUEST);
			}
			/* end of example for querying ac_api */

			
			//(Yalew Cache)22. check 
			//if get the list of user-friend's value from redis.
			
			
			List<String> result_list;
					
			result_list = RedisCL.lrange(userID+"-list", 0 ,-1);
			
			if(result_list.size() != 0){
				friendList = result_list;
			}
			else{
			quri = "http://" + Configuration.ac_api_address + "/user/" + userID + "/access";

			// query to ac_api server
			qurlParameters = "";
			query_result = Configuration.query_access_relation(quri, accessToken, qurlParameters);

			// for debug, erase after implementing.
			Configuration.logger.info(query_result);

			query_result = query_result.replaceAll("[\"{}\\[\\] ]", "").split(":")[1];
			String[] accessusers = query_result.split(",");

			// 2. if pass the subscribing test, then get all event owner list
			friendList = new ArrayList<String>();
			for (String accessuser : accessusers) {
				Configuration.logger.info("Friends:" + accessuser);
				friendList.add(accessuser);
				RedisCL.lpush(userID+"-list", accessuser);
			}
			}

			// 3. and then make restricted query_list
		} else {

		}

		PollParameters pollParams = new PollParameters(queryName, eventType, GE_eventTime, LT_eventTime, GE_recordTime,
				LT_recordTime, EQ_action, EQ_bizStep, EQ_disposition, EQ_readPoint, WD_readPoint, EQ_bizLocation,
				WD_bizLocation, EQ_transformationID, MATCH_epc, MATCH_parentID, MATCH_inputEPC, MATCH_outputEPC,
				MATCH_anyEPC, MATCH_epcClass, MATCH_inputEPCClass, MATCH_outputEPCClass, MATCH_anyEPCClass, EQ_quantity,
				GT_quantity, GE_quantity, LT_quantity, LE_quantity, EQ_eventID, EXISTS_errorDeclaration,
				GE_errorDeclarationTime, LT_errorDeclarationTime, EQ_errorReason, EQ_correctiveEventID, orderBy,
				orderDirection, eventCountLimit, maxEventCount, vocabularyName, includeAttributes, includeChildren,
				attributeNames, EQ_name, WD_name, HASATTR, maxElementCount, format, params);
		
		
		QueryService queryService = new QueryService();
		String result = queryService.poll(pollParams, userID, friendList, null);
		return new ResponseEntity<>(result, responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value = "/Poll/NamedEventQuery/{name}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> poll(@PathVariable String name, @RequestParam(required = false) String userID,
			@RequestParam(required = false) String accessToken)
			throws QueryParameterException, QueryTooLargeException, QueryTooComplexException, NoSuchNameException,
			SecurityException, ValidationException, ImplementationException {

		HttpHeaders responseHeaders = new HttpHeaders();
		/*
		MongoCollection<BsonDocument> collection = Configuration.mongoDatabase.getCollection("NamedEventQuery",
				BsonDocument.class);
		BsonDocument namedEventQuery = collection.find(new BsonDocument("name", new BsonString(name))).first();
		PollParameters pollParams = new PollParameters(namedEventQuery);

		if (pollParams.getFormat() != null && pollParams.getFormat().equals("JSON")) {
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		} else {
			responseHeaders.add("Content-Type", "application/xml; charset=utf-8");
		}

		// Access Control is not mandatory
		// However, if fid and accessToken provided, more information provided
		
		FacebookClient fc = null;
		List<String> friendList = null;

		if (userID != null) {
			// Check accessToken
			fc = OAuthUtil.isValidatedFacebookClient(accessToken, userID);
			if (fc == null) {
				return new ResponseEntity<>(new String("Unauthorized Token"), responseHeaders, HttpStatus.UNAUTHORIZED);
			}
			friendList = new ArrayList<String>();

			Connection<User> friendConnection = fc.fetchConnection("me/friends", User.class);
			for (List<User> friends : friendConnection) {
				for (User friend : friends) {
					friendList.add(friend.getId());
				}
			}
		}

		MongoQueryService mongoQueryService = new MongoQueryService();
		String result = mongoQueryService.poll(pollParams, userID, friendList, null);
		*/
		String result="/Poll/NamedEventQuery/{name}";
		return new ResponseEntity<>(result, responseHeaders, HttpStatus.OK);
	}

	
	
}
