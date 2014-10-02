package com.ztilde.client;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.orm.StringUtil;

public class ZTilde {
	public static String apiKey;
	private String name;
	private String[] header;
	private static final String predictUrl = "";

	public ZTilde(String apiKey) {
		this.apiKey = apiKey;
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

	public static ZTilde fromJSON(JSONObject json, String apiKey) throws JSONException {
		ZTilde z = new ZTilde(apiKey);
		z.name = json.getString("name");
		return z;
	}

	public static ZTilde[] getList(String apiKey) throws ClientProtocolException, IOException,
			UnauthorizedException, IllegalStateException, JSONException {
		HttpResponse resp = HttpClient.get("/api/model/");
		JSONObject objects = HttpClient.toJSON(resp);

		JSONArray classifiers = objects.getJSONArray("classifiers");
		JSONArray clustering = objects.getJSONArray("clustering");
		
		ZTilde[] ret = new ZTilde[classifiers.length() + clustering.length()];
		int pointer = 0;
		
		Classifier zr;
		for (int i = 0; i < classifiers.length(); i++) {
			zr = Classifier.fromJSON(classifiers.getJSONObject(i), apiKey);
			ret[pointer++] = zr;
		}
		
		Clustering zg;
		for (int i = 0; i < clustering.length(); i++) {
			zr = Classifier.fromJSON(clustering.getJSONObject(i), apiKey);
			ret[pointer++] = zr;
		}
		
		return ret;
	}
}
