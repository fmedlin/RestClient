package com.roobit.android.restclient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import android.net.Uri;
import android.util.Log;

import com.roobit.android.restclient.RestClient.Operation;

public class RestClientRequest {

	static final String TAG = "RestClientRequest";

	public static RestResult synchronousExecute(Operation op, Uri uri) {
		return synchronousExecute(op, uri, null);
	}
	
	public static RestResult synchronousExecute(Operation op, Uri uri, Properties httpHeaders) {
		Log.d(TAG, "Executing " + op.toString() + " to " + uri.toString());
		
		RestResult result = new RestResult();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(uri.toString()).openConnection();
			setRequestMethod(urlConnection, op);
			setRequestHeaders(urlConnection, httpHeaders);
			result.setResponseCode(urlConnection.getResponseCode());
			result.setResponse(convertStreamToString(new BufferedInputStream(urlConnection.getInputStream())));
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

	private static void setRequestMethod(HttpURLConnection urlConnection, Operation op) {
		if (op == Operation.POST) {
			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
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
	
	private static String convertStreamToString(InputStream is) throws IOException {
		final char[] buffer = new char[0x10000];
		StringBuilder sb = new StringBuilder();
		Reader in = new InputStreamReader(is, "UTF-8");
		
		int read;
		do {
			read = in.read(buffer, 0, buffer.length);
			if (read>0) {
				sb.append(buffer, 0, read);
			}
		} while (read>=0);
		
		return sb.toString();
	}
}
