package com.roobit.android.restclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Properties;

import android.net.Uri;
import android.util.Log;

import com.roobit.android.restclient.RestClient.Operation;

public class RestClientRequest {

	static final String TAG = "RestClientRequest";

	public enum StreamingMode { CHUNKED, FIXED };
	static StreamingMode streamingMode = StreamingMode.CHUNKED;
	
	public static RestResult synchronousExecute(Operation op, Uri uri) {
		return synchronousExecute(op, uri, null);
	}

	public static RestResult synchronousExecute(Operation op, Uri uri, Properties httpHeaders) {
		return synchronousExecute(op, uri, httpHeaders, null, null);
	}

	public static RestResult synchronousExecute(Operation op,
			Uri uri, 
			Properties httpHeaders,
			Properties parameters,
			ByteArrayOutputStream postData) {
		
		Log.d(TAG, "Executing " + op.toString() + " to " + uri.toString());
		
		streamingMode = (parameters == null && postData == null)  ? StreamingMode.CHUNKED : StreamingMode.FIXED;
		
		RestResult result = new RestResult();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(uri.toString()).openConnection();
			//setAuthentication(urlConnection, uri.getEncodedUserInfo());
			setRequestHeaders(urlConnection, httpHeaders);
			setRequestParameters(urlConnection, parameters);
			setRequestMethod(urlConnection, op, httpHeaders);
			setPostData(urlConnection, postData);
			
			int statusCode = 0;
			try {
				statusCode = urlConnection.getResponseCode();
			} catch (IOException e) {
				statusCode = HttpURLConnection.HTTP_UNAUTHORIZED;
			}
			
			result.setResponseCode(statusCode);
			Log.d(TAG, " - received response code [" + urlConnection.getResponseCode() + "]");
			if (statusCode < HttpURLConnection.HTTP_BAD_REQUEST) {
				result.setResponse(convertStreamToString(new BufferedInputStream(urlConnection.getInputStream())));
			}
			else {
				String errorMessage = convertStreamToString(new BufferedInputStream(urlConnection.getErrorStream())); 
				Log.d(TAG, "error message: " + errorMessage);
				result.setResponse(errorMessage);
			}
		} catch (Exception e) {
			result.setException(e);
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		
		return result;
	}

	@SuppressWarnings("unused")
	private static void setAuthentication(HttpURLConnection urlConnection, String encodedUserInfo) {
		if (encodedUserInfo != null) {
			urlConnection.setRequestProperty("Authorization", "Basic " + encodedUserInfo);
		}
	}

	private static void setPostData(HttpURLConnection urlConnection, ByteArrayOutputStream postData) {
		if (postData == null) {
			return;
		}
		
		urlConnection.setFixedLengthStreamingMode(postData.size());
		OutputStream os = null;
		try {
			os = urlConnection.getOutputStream();
			os.write(postData.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {}
			}
		}
	}

	private static void setRequestMethod(HttpURLConnection urlConnection, Operation op, Properties httpProperties) throws Exception {
		if (op == Operation.POST || op == Operation.PATCH) {
			urlConnection.setDoOutput(true);			
			urlConnection.setRequestMethod("POST");

			if (streamingMode == StreamingMode.CHUNKED) {
				urlConnection.setChunkedStreamingMode(0);
			}

			if (op == Operation.PATCH) {
				httpProperties.put("X-HTTP-Method-Override", "PATCH");
			}
		} else if (op == Operation.PUT) {
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("PUT");

			if (streamingMode == StreamingMode.CHUNKED) {
				urlConnection.setChunkedStreamingMode(0);
			}
		} else {
			urlConnection.setRequestMethod("GET");			
		}
		
		// TODO: Handle OPTIONS, HEAD, PUT, DELETE and TRACE
	}
	
	private static void setRequestHeaders(HttpURLConnection urlConnection, Properties httpHeaders) {
		if (httpHeaders == null) {
			return;
		}
		
		Iterator<Object> iter = httpHeaders.keySet().iterator();
		while(iter.hasNext()) {
			String name = (String) iter.next();
			urlConnection.setRequestProperty(name, httpHeaders.getProperty(name));
		}
	}
	
	private static void setRequestParameters(HttpURLConnection urlConnection, Properties parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return;
		}

		String params = buildParameterString(parameters);
		urlConnection.setFixedLengthStreamingMode(params.getBytes().length);
		
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(urlConnection.getOutputStream());
			os.write(params.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (Exception e) {}
			}
		}
	}
	
	private static String buildParameterString(Properties parameters) {
		StringBuilder sb = new StringBuilder();
		boolean prependAmp = false;

		Iterator<Object> iter = parameters.keySet().iterator();
		while(iter.hasNext()) {
			if (prependAmp) {
				sb.append("&");
			}
			prependAmp = true;

			try {
				String name = (String) iter.next();
				sb.append(name).append('=').append(URLEncoder.encode(parameters.getProperty(name), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	private static String convertStreamToString(InputStream is) throws IOException {
		final char[] buffer = new char[0x10000];
		StringBuilder sb = new StringBuilder();
		Reader in = new InputStreamReader(is, "UTF-8");
		
		int read;
		do {
			read = in.read(buffer, 0, buffer.length);
			if (read > 0) {
				sb.append(buffer, 0, read);
			}
		} while (read >= 0);
		
		return sb.toString();
	}
}
