package com.ztilde.client;

import org.json.JSONException;
import org.json.JSONObject;

public class Classifier extends ZTilde {
	private static final String predictUrl = "/api/classifier/%s/predict"; 
	
	public Classifier(String apiKey) {
		super(apiKey);
	}

	public static Classifier fromJSON(JSONObject json, String apiKey) throws JSONException {
		Classifier z = (Classifier) ZTilde.fromJSON(json, apiKey);
		return z;
	}
}
