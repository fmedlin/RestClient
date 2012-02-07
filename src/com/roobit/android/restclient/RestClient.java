package com.roobit.android.restclient;

public class RestClient {

	String baseUrl;
	
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

}
