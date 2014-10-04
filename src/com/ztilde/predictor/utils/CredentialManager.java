package com.ztilde.predictor.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import com.ztilde.predictor.R;

import com.ztilde.client.HttpClient;
import com.ztilde.client.UnauthorizedException;

public class CredentialManager {
	private String apiKey;
	private Context context;
	private static CredentialManager singleton;
	
	private CredentialManager(Context context) {
		this.context = context;
		
		SharedPreferences sharedPref = context.getSharedPreferences(
				context.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);
		this.apiKey = sharedPref.getString(
				context.getString(R.string.saved_api_key), "");
	}
	
	public static CredentialManager getInstance(Context context) {
		if (CredentialManager.singleton == null) {
			CredentialManager.singleton = new CredentialManager(context);
		}
		return CredentialManager.singleton;
	}
	
	public void updateApiKey(String email, String password)
			throws IllegalStateException, IOException, JSONException,
			UnauthorizedException {
		String credentials = "email=" + email;
		credentials += "&password=" + password;

		HttpResponse response = HttpClient.get("/api/userkey/?" + credentials);
		if(response.getStatusLine().getStatusCode() == 403) {
			throw new UnauthorizedException("Invalid username or password");
		}
		this.storeApiKey(HttpClient.toString(response).toString());
	}
	
	private void storeApiKey(String apiKey) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				context.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		editor.putString(context.getString(R.string.saved_api_key),
				apiKey);
		editor.commit();
		this.apiKey = apiKey;
	}
	
	public boolean isLoggedIn() {
		return this.apiKey != "";
	}
	
	public String getApiKey() {
		return this.apiKey;
	}
}
