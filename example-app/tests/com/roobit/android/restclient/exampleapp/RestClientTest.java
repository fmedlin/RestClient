package com.roobit.android.restclient.exampleapp;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.roobit.android.restclient.RestClient;
import com.roobit.android.restclient.RestResult;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RestClientTest {

	static final String TEST_ENDPOINT = "http://localhost:4567/test_endpoint";
	
	RestResult restResult;
	
	@Test
	public void shouldConfigureSharedSingleton() {
		RestClient.clearSharedClient();
		RestClient first = RestClient.clientWithBaseUrl("http://api.example.org");
		RestClient.clientWithBaseUrl("http://api.google.com");
		assertThat(first.getBaseUrl(),
			equalTo(RestClient.sharedClient().getBaseUrl()));
	}
	
	@Test
	public void shouldSetResource() {
		RestClient client = RestClient
			.clientWithBaseUrl("http://api.example.org")
			.setResource("articles");
		assertThat("http://api.example.org/articles", equalTo(client.getUrl()));
	}
	
	@Test
	public void shouldSetQueryParameters() {
		LinkedHashMap<String,String> queryParams = new LinkedHashMap<String,String>();
		queryParams.put("first_param", "first_value");
		queryParams.put("second_param", Boolean.toString(true));
		
		RestClient client = RestClient.clientWithBaseUrl("http://api.example.org")
				.setResource("articles")
				.setQueryParameters(queryParams);

		assertThat("http://api.example.org/articles?first_param=first_value&second_param=true",
			equalTo(client.getUrl()));
	}
	
	@Test
	public void shouldGet() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);		
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("articles")
			.execute(new RestClient.OnCompletionListener() {					
				@Override
				public void success(RestClient client, RestResult result) {
					restResult = result;
					latch.countDown();
				}
	
				@Override
				public void failedWithError(RestClient restClient, int responseCode, RestResult result) {
					restResult = result;
					latch.countDown();
				}					
		});
		
		if (latch.await(5, TimeUnit.SECONDS)) {
			assertTrue(restResult.isSuccess());
			assertThat(HttpURLConnection.HTTP_OK, equalTo(restResult.getResponseCode()));
			assertThat("Articles", equalTo(restResult.getResponse()));
		}
		else {
			fail("Timed out waiting for GET completion");
		}
	}
	
	@Test
	public void shouldThrowOnNonExistentResource() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);		
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("articles/does_not_exist")
			.execute(new RestClient.OnCompletionListener() {					
				@Override
				public void success(RestClient client, RestResult result) {
					restResult = result;
					latch.countDown();
				}
	
				@Override
				public void failedWithError(RestClient restClient, int responseCode, RestResult result) {
					restResult = result;
					latch.countDown();
				}					
		});
		
		if (latch.await(5, TimeUnit.SECONDS)) {
			assertFalse(restResult.isSuccess());
			assertThat(HttpURLConnection.HTTP_NOT_FOUND, equalTo(restResult.getResponseCode()));
		}
		else {
			fail("Timed out waiting for non-existent POST completion");
		}
	}
}
