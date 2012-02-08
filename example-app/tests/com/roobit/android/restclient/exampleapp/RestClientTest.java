package com.roobit.android.restclient.exampleapp;

import static org.junit.Assert.assertEquals;

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
}
