package com.roobit.android.restclient;

import java.net.HttpURLConnection;

public class RestResult {

	int responseCode;
	String response;
	Exception exception;

	public RestResult() {
		responseCode = 0;
		response = "";
	}
	
	public boolean isSuccess() {
		return responseCode == HttpURLConnection.HTTP_OK;
	}
		
	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public boolean hasException() {
		return getException() != null;
	}
}
