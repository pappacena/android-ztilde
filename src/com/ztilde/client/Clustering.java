package com.ztilde.client;

import org.json.JSONException;
import org.json.JSONObject;

public class Clustering extends ZTilde {
	private static final String predictUrl = "/api/clustering/%s/predict";
	private int clusters;

	public Clustering(String apiKey, int clusters) {
		super(apiKey);
		this.clusters = clusters;
	}

	public static Clustering fromJSON(JSONObject json, String apiKey) throws JSONException {
		Clustering z = (Clustering) ZTilde.fromJSON(json, apiKey);
		z.clusters = json.getInt("clusters");
		return z;
	}
	
}
