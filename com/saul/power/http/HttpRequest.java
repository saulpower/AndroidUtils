package com.moki.android.kiosk.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.moki.android.kiosk.utils.MyLog;

public class HttpRequest {

	private static final String TAG = "HttpRequest";
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	
	private static final int CONNECTION_TIMEOUT = 30000;
	private static final int SOCKET_TIMEOUT = 30000;
	
	/**
	 * Make an HTTP POST request to the supplied URL, appending the query parameters,
	 * and inserting the body data
	 *  
	 * @param url
	 * @param token
	 * @param data
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String sendRequest(String url, String method, HashMap<String, String> headers, HashMap<String, String> params, String data) throws ClientProtocolException, IOException {
	    
		// Define http parameters
	    HttpParams httpParameters = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	    HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
	    
	    // Create a new HttpClient
	    DefaultHttpClient httpclient = HttpClientFactory.getThreadSafeClient();
	    httpclient.setParams(httpParameters);
	    
	    // Add query string parameters to url
	    if (params != null)
		    url += toQueryString(params);
		
	    MyLog.i(TAG, "Method: " + method + " URL: " + url);
	    
	    // Create request based on method
	    HttpUriRequest httpRequest = getRequest(method, url);
	    
	    if (httpRequest == null)
	    	throw new IllegalArgumentException("Method not supported");

	    // Add headers to request
	    if (headers != null) {
		    for (String key : headers.keySet()) {
				if (headers.get(key) != null) {
					httpRequest.setHeader(key, headers.get(key));
				}
		    }
	    }

		// Add body data
    	if ((method.equals(POST) || method.equals(PUT)) && data != null) {
	        
	        StringEntity se = new StringEntity(data);
	        ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(se);
    	}
	    
	    HttpResponse response = null;

        // Execute HTTP Request
        response = httpclient.execute(httpRequest);

        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
        	
        	//Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
        
        // Return string response
	    return getResponseBody(response.getEntity());
	}
	
	private static String getResponseBody(final HttpEntity entity) throws IOException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        entity.writeTo(out);
        out.close();
        
        return out.toString();
	}
	
	private static HttpUriRequest getRequest(String method, String url) {
		
		if (method.equals(GET)) {
			return new HttpGet(url);
		}
		
		if (method.equals(POST)) {
			return new HttpPost(url);
		}
		
		if (method.equals(PUT)) {
			return new HttpPut(url);
		}
		
		return null;
	}
	
	private static String toQueryString(HashMap<String, String> attributes) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("?");
		
		for (String key : attributes.keySet()) {
			if (attributes.get(key) != null) {
				sb.append(key);
				sb.append("=");
				sb.append(URLEncoder.encode(attributes.get(key)));
				sb.append("&");
			}
		}
		
		return sb.substring(0, sb.length() - 1);
	}
	
}
