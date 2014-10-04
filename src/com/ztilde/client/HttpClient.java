package com.ztilde.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class HttpClient extends DefaultHttpClient {
	private static String apiKey = "";
	private static String baseUrl = "http://192.168.0.102:8000";

	public static void setApiKey(String apiKey) {
		HttpClient.apiKey = apiKey;
	}

	public HttpClient() {
		// TODO Auto-generated constructor stub
	}

	public HttpClient(HttpParams params) {
		super(params);
		// TODO Auto-generated constructor stub
	}

	public static HttpResponse get(String url) throws ClientProtocolException,
			IOException, UnauthorizedException {
		url = HttpClient.baseUrl + url;

		HttpClient httpclient = new HttpClient();
		HttpGet get = new HttpGet(url);
		get.setHeader("X-API-KEY", HttpClient.apiKey);

		HttpResponse response = httpclient.execute(get);
		
		if (response.getStatusLine().getStatusCode() == 401) {
			throw new UnauthorizedException("401 - Unauthorized: " + url);
		}
		return response;
	}

	public static HttpResponse delete(String url)
			throws ClientProtocolException, IOException, UnauthorizedException {
		url = HttpClient.baseUrl + url;

		HttpClient httpclient = new HttpClient();
		HttpDelete delete = new HttpDelete(url);
		delete.setHeader("X-API-KEY", HttpClient.apiKey);

		HttpResponse response = httpclient.execute(delete);
		if (response.getStatusLine().getStatusCode() == 401) {
			throw new UnauthorizedException("401 - Unauthorized: " + url);
		}
		return response;
	}

	public static JSONObject getJSON(String url)
			throws ClientProtocolException, IOException, UnauthorizedException,
			JSONException {
		HttpResponse response = HttpClient.get(url);

		return HttpClient.toJSON(response);
	}

	public static HttpResponse post(String url, String params)
			throws JSONException, UnauthorizedException,
			ClientProtocolException, IOException {
		url = HttpClient.baseUrl + url;

		HttpClient httpclient = new HttpClient();
		HttpPost post = new HttpPost(url);

		if (HttpClient.apiKey != null) {
			post.setHeader("X-API-KEY", HttpClient.apiKey);
		}
		StringEntity se = new StringEntity(params, HTTP.UTF_8);
		post.setEntity(se);
		// post.setHeader("Accept", "application/json");
		// post.setHeader("Content-type", "application/json;charset=UTF-8");

		HttpResponse response = httpclient.execute(post);
		if (response.getStatusLine().getStatusCode() == 401) {
			throw new UnauthorizedException("401 - Unauthorized: " + url);
		}
		return response;
	}

	/**
	 * Get the JSON from an Http response object
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws JSONException
	 */
	public static JSONObject toJSON(HttpResponse response)
			throws IllegalStateException, IOException, JSONException {

		HttpEntity httpEntity = response.getEntity();
		InputStream is = httpEntity.getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"utf-8"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		String json = sb.toString();

		return new JSONObject(json);
	}

	public static String toString(HttpResponse response)
			throws IllegalStateException, IOException, JSONException {
		HttpEntity httpEntity = response.getEntity();
		InputStream is = httpEntity.getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"utf-8"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

}
