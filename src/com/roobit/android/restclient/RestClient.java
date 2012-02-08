package com.roobit.android.restclient;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.net.Uri;

public class RestClient {

	String baseUrl;
	String resource;
	LinkedHashMap<String, String> queryParameters;
	
	static RestClient instance;
	
	public static RestClient sharedClient() {
		return instance;
	}
	
	public static RestClient clientWithBaseUrl(String baseUrl) {
		RestClient client =  new RestClient(baseUrl);
		if (instance == null) {
			instance = client;
		}
		return client;
	}

	protected RestClient(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	
	public String getUrl() {
		return buildUri().toString();
	}

	private Uri buildUri() {
		Uri.Builder builder = Uri.parse(getBaseUrl())
				.buildUpon()
				.appendEncodedPath(getResource());
		
		if (queryParameters != null && !queryParameters.isEmpty()) {
			Iterator<Entry<String, String>> iter = queryParameters.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				builder.appendQueryParameter(entry.getKey(), entry.getValue());				
			}
		}
		return builder.build();
	}
	
	private String getResource() {
		return resource;
	}
	
	public RestClient setResource(String resource) {
		this.resource = resource;
		return this;
	}
	
	public RestClient setQueryParameters(LinkedHashMap<String,String> queryParameters) {
		this.queryParameters = queryParameters;
		return this;
	}
}
