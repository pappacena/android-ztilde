package com.ztilde.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.json.JSONException;

public class Login {
	public static String getApiKey(String email, String password)
			throws IllegalStateException, IOException, JSONException,
			UnauthorizedException {
		String credentials = "email=" + email;
		credentials += "&password=" + password;

		HttpResponse response = HttpClient.get("/api/userkey/?" + credentials);
		if(response.getStatusLine().getStatusCode() == 403) {
			throw new UnauthorizedException("Invalid username or password");
		}
		return HttpClient.toString(response);
	}
}
