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

	public Clustering() {
		super("");
	}

	public void hydrate(JSONObject json, String apiKey) throws JSONException {
		super.hydrate(json, apiKey);
		this.clusters = json.getInt("clusters");
	}

}
