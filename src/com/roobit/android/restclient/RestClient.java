package com.roobit.android.restclient;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.roobit.android.restclient.RestClientRequestTask.RestClientRequestListener;

import android.net.Uri;

public class RestClient implements RestClientRequestListener {

	public interface OnCompletionListener {
		public void success(RestClient client, RestResult result);
		public void failedWithError(RestClient restClient, int responseCode, RestResult result);
	}

	public enum Operation { GET, POST, PUT, DELETE, PATCH };

	String baseUrl;
	String resource;
	LinkedHashMap<String, String> queryParameters;
	Operation operation;
	OnCompletionListener completionListener;
	
	static RestClient instance;
	
	public static RestClient sharedClient() {
		return instance;
	}
	
	public static void clearSharedClient() {
		instance = null;
		
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
	
	public RestClient execute(OnCompletionListener completionListener) {
		this.completionListener = completionListener;
		new RestClientRequestTask(this).execute(getOperation(), buildUri());
		return this;
	}

	private Operation getOperation() {
		if (operation == null) {
			operation = Operation.GET;
		}
		return operation;
	}
	
	public RestClient post() {
		operation = Operation.POST;
		setQueryParameters(null);
		return this;
	}
	
	@Override
	public void requestStarted() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void requestCancelled() {
		// TODO Auto-generated method stub
	}

	@Override
	public void requestFinished(RestResult result) {
		// TODO: Determine result success of failure
		if (completionListener != null) {
			completionListener.success(this, result);
		}
	}

}
