package com.roobit.android.restclient.exampleapp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

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

	RestResult restResult;
	
	@Test
	public void shouldConfigureSharedSingleton() {
		RestClient first = RestClient.clientWithBaseUrl("http://api.example.org");
		RestClient.clientWithBaseUrl("http://api.google.com");
		assertThat(first.getBaseUrl(),
			equalTo(RestClient.sharedClient().getBaseUrl()));
	}
	
	@Test
	public void shouldSetResource() {
		RestClient client = RestClient.clientWithBaseUrl("http://api.example.org");
		client.setResource("articles");
		assertThat("http://api.example.org/articles",
			equalTo(client.getUrl()));
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
	public void shouldGetFromServer() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);		
		RestClient.clientWithBaseUrl("http://localhost:4567/test_endpoint")
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
			assertThat("Articles", equalTo(restResult.getResponse()));
		}
		else {
			fail("Timed out waiting for GET completion");
		}
	}
}
