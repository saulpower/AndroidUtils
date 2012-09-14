package com.moki.android.kiosk.http;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {

    public synchronized static DefaultHttpClient getThreadSafeClient() {
  
    	DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();

        client = new DefaultHttpClient(
            new ThreadSafeClientConnManager(params,
                mgr.getSchemeRegistry()), params);

        return client;
    }
}
