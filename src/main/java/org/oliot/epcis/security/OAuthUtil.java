package org.oliot.epcis.security;

import java.util.List;

import org.bson.BsonDocument;
import org.oliot.epcis.configuration.Configuration;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;



public class OAuthUtil {
	public static boolean isValidated(String accessToken, String userID) {
		try {
			FacebookClient fc = new DefaultFacebookClient(accessToken, Version.VERSION_2_4);
			String id = fc.fetchObject("me", User.class).getId();
			if (!id.equals(userID)) {
				return false;
			}
			return true;
		} catch (FacebookOAuthException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static FacebookClient isValidatedFacebookClient(String accessToken, String userID) {
		try {
			FacebookClient fc = new DefaultFacebookClient(accessToken, Version.VERSION_2_4);
			String id = fc.fetchObject("me", User.class).getId();
			if (!id.equals(userID)) {
				return null;
			}
			return fc;
		} catch (FacebookOAuthException e) {
			e.printStackTrace();
			return null;
		}
	}

	// For MongoDB Document
	public static boolean isAccessible(String userID, List<String> friendList, BsonDocument doc) {

		if (!doc.containsKey("accessModifier") || !doc.containsKey("userID"))
			return true;
		String am = doc.get("accessModifier").asString().getValue();
		String providerID = doc.get("userID").asString().getValue();

		// Non-public document && No authorization
		if (userID == null) {
			return false;
		}

		// If Owner, accessible
		if (providerID.equals(userID)) {
			return true;
		}

		// If Not Owner
		if (am.equals("Friend")) {
			if (friendList.contains(providerID)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public static boolean isAdministratable(String userID, List<String> friendList) {
		String am = Configuration.adminScope;
		String providerID = Configuration.adminID;

		// If Owner, accessible
		if (providerID.equals(userID)) {
			return true;
		}

		// If Not Owner
		if (am.equals("Friend")) {
			if (friendList.contains(providerID)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}
}
