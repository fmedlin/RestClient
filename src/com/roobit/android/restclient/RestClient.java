package com.roobit.android.restclient;

import android.net.Uri;

public class RestClient {

	String baseUrl;
	String resource;
	
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
		return builder.build();
	}
	
	private String getResource() {
		return resource;
	}
	
	public RestClient setResource(String resource) {
		this.resource = resource;
		return this;
	}
	
}
