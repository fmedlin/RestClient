package com.roobit.android.restclient.exampleapp;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.roobit.android.restclient.RestClient;
import com.roobit.android.restclient.RestResult;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RestClientPutTest {

	static final String TEST_ENDPOINT = "http://localhost:4567/test_endpoint";
	
	RestResult restResult;

	@Test
	public void shouldPut() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("echo_request_method")
			.put()
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
			assertThat(restResult.getResponse(), equalTo("PUT"));
		}
		else {
			fail("Timed out waiting for PUT");
		}
	}

	@Test
	public void shouldPutHttpHeaders() throws Exception {
		Properties httpHeaders = new Properties();
		httpHeaders.put("X-Some-Special-Header", "awesomeness");

		final CountDownLatch latch = new CountDownLatch(1);
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("awesome_header")
			.put(httpHeaders)
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
			fail("Timed out waiting for PUT");
		}
	}

	@Test
	public void shouldPutFormData() throws Exception {
		Properties parameters = new Properties();
		parameters.setProperty("username", "assad");
		parameters.setProperty("password", "12345");

		final CountDownLatch latch = new CountDownLatch(1);
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("should_put_form")
			.putForm(parameters)
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
			assertTrue("PUT form failed", restResult.isSuccess());
		}
		else {
			fail("Timed out waiting for PUT");
		}
	}

	@Test
	public void shouldPutData() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("{\"id\": 42}".getBytes());

		final CountDownLatch latch = new CountDownLatch(1);
		RestClient.clientWithBaseUrl(TEST_ENDPOINT)
			.setResource("should_put_data")
			.put(baos)
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
			assertTrue("PUT data failed", restResult.isSuccess());
		}
		else {
			fail("Timed out waiting for PUT data");
		}
	}
}
