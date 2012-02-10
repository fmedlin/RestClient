package com.roobit.android.restclient.exampleapp;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.roobit.android.restclient.RestClient;
import com.roobit.android.restclient.RestResult;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RestClientPostTest {

	static final String TEST_ENDPOINT = "http://localhost:4567/test_endpoint";
	
	RestResult restResult;

	@Test
	public void shouldPost() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);		
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("echo_request_method")
			.post()
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
			assertThat(restResult.getResponse(), equalTo("POST"));
		}
		else {
			fail("Timed out waiting for POST");
		}
	}

	@Test
	public void shouldPostHttpHeaders() throws Exception {
		Properties httpHeaders = new Properties();
		httpHeaders.put("X-Some-Special-Header", "awesomeness");
		
		final CountDownLatch latch = new CountDownLatch(1);		
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("awesome_header")
			.post(httpHeaders)
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
			assertThat(restResult.getResponse(), equalTo("awesomeness"));
		}
		else {
			fail("Timed out waiting for POST");
		}
	}

	@Test
	public void shouldPostFormData() throws Exception {
		Properties parameters = new Properties();
		parameters.setProperty("username", "assad");
		parameters.setProperty("password", "12345");
		
		final CountDownLatch latch = new CountDownLatch(1);		
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("should_post_form")
			.postForm(parameters)
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
			assertTrue("POST form failed", restResult.isSuccess());
		}
		else {
			fail("Timed out waiting for POST");
		}
	}
}
