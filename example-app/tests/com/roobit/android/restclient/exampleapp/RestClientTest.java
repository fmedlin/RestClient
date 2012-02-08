package com.roobit.android.restclient.exampleapp;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.roobit.android.restclient.RestClient;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RestClientTest {

	@Test
	public void shouldConfigureSharedSingleton() {
		RestClient first = RestClient.clientWithBaseUrl("http://api.example.org");
		RestClient.clientWithBaseUrl("http://api.google.com");
		assertEquals(first.getBaseUrl(), RestClient.sharedClient().getBaseUrl());
	}
	
	@Test
	public void shouldSetResource() {
		RestClient client = RestClient.clientWithBaseUrl("http://api.example.org");
		client.setResource("articles");
		assertEquals("http://api.example.org/articles", client.getUrl());
	}
	
	@Test
	public void shouldSetQueryParameters() {
		LinkedHashMap<String,String> queryParams = new LinkedHashMap<String,String>();
		queryParams.put("first_param", "first_value");
		queryParams.put("second_param", Boolean.toString(true));
		
		RestClient client = RestClient.clientWithBaseUrl("http://api.example.org")
				.setResource("articles")
				.setQueryParameters(queryParams);

		assertEquals("http://api.example.org/articles?first_param=first_value&second_param=true",
				client.getUrl());
	}
}
