package com.ztilde.client;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.orm.StringUtil;

public class ZTilde {
	public static String apiKey;
	private String name;
	private String[] header;
	private static final String predictUrl = "";

	public ZTilde(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public ZTilde() {
		this.apiKey = "";
	}

	/**
	 * Predict using this model
	 * 
	 * @param pattern
	 * @return
	 * @throws ClientProtocolException
	 * @throws JSONException
	 * @throws UnauthorizedException
	 * @throws IOException
	 */
	public String predict(Object[] pattern) throws ClientProtocolException,
			JSONException, UnauthorizedException, IOException {
		String parm = "";
		for (int i = 0; i < pattern.length; i++) {
			if (i != 0) {
				parm += ",";
			}
			parm += pattern[i].toString();
		}

		String url = String.format(this.predictUrl, this.name);
		HttpResponse resp = HttpClient.post(url, parm);
		JSONObject json = HttpClient.toJSON(resp);
		return json.getString("value");
	}

	public void hydrate(JSONObject json, String apiKey) throws JSONException {
		this.name = json.getString("name");
		this.apiKey = apiKey;
	}

	public static ZTilde[] getList(String apiKey) throws ClientProtocolException, IOException,
			UnauthorizedException, IllegalStateException, JSONException {
		HttpClient.setApiKey(apiKey);
		HttpResponse resp = HttpClient.get("/api/model/");
		
		JSONObject objects = HttpClient.toJSON(resp);

		JSONArray classifiers = objects.getJSONArray("classifiers");
		JSONArray clustering = objects.getJSONArray("clustering");
		
		ZTilde[] ret = new ZTilde[classifiers.length() + clustering.length()];
		int pointer = 0;
		
		Classifier zr;
		for (int i = 0; i < classifiers.length(); i++) {
			zr = new Classifier();
			zr.hydrate(classifiers.getJSONObject(i), apiKey);
			ret[pointer++] = zr;
		}
		
		Clustering zg;
		for (int i = 0; i < clustering.length(); i++) {
			zg = new Clustering();
			zg.hydrate(clustering.getJSONObject(i), apiKey);
			ret[pointer++] = zg;
		}
		
		return ret;
	}
	
	public String getName() {
		return this.name;
	}
}
