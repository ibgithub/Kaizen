/**
 * 
 */
package com.dev.kaizen.restful;

import android.content.Context;
import android.util.Log;

import com.dev.kaizen.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RestfulHttpMethod {
	private static int flag = 0;
	
	private static final String TAG = "RestfulHttpMethod";
	
	private RestfulHttpMethod() {
		// TODO Auto-generated constructor stub
	}

    public static String connect(String url, int method) throws ClientProtocolException, SocketException , SocketTimeoutException, ConnectTimeoutException, Exception{     	   
    	String result = Constant.JSON_TIMEOUT;
    	if(flag == 1)
    		flag=1;    	
    	
    	HttpParams httpParameters = new BasicHttpParams();
    	// Set the timeout in milliseconds until a connection is established.
    	int timeoutConnection = 180000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	
    	// Set the default socket timeout (SO_TIMEOUT) 
    	// in milliseconds which is the timeout for waiting for data.
    	int timeoutSocket = 180000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	
    	
    	// begin disable ssl certificate validation    	
        /*KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        SSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParameters, registry);*/
        
        // end of disable ssl 
    	
        /* Start of SSL Pinning versi 2 */
    	String[] pins = new String[] {"74140B3307C41636D0070C8C4035B57155150B92"};
    	//Log.e(TAG, "pins " + pins[0]);
    	
    	// tanpa ssl pin
    	//KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        //trustStore.load(null, null);
        //SSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
        //sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        
    	SchemeRegistry schemeRegistry = new SchemeRegistry();
    	schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    	schemeRegistry.register(new Scheme("https", new PinningSSLSocketFactory(pins, 0), 443));
//    	schemeRegistry.register(new Scheme("https", sf, 443));
    	
    	HttpParams httpParams = new BasicHttpParams();
    	ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
    	/* End of SSL Pinning versi 2*/
    	
    	
//        HttpClient httpclient = new DefaultHttpClient();
        HttpClient httpclient = new DefaultHttpClient(ccm,httpParameters);
//        httpclient.getParams().setParameter(
//        		"http.useragent",
//        	    "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
//        	);
        
        HttpUriRequest request;
        switch (method) {
	        case Constant.REST_GET:
				request = new HttpGet(url);
				break;
			case Constant.REST_PUT:
				request = new HttpPut(url);
				break;
			case Constant.REST_POST:
				List<NameValuePair> nvppost = new ArrayList<NameValuePair>();
				int startPos2 = url.indexOf("?");
				String query2 = url.substring(startPos2+1);
				url = url.substring(0, startPos2);

				String[] pairs2 = query2.split("&");
				for (String pair : pairs2) {
					int idx = pair.indexOf("=");
					String name = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
					String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
					Log.d("name : value", name + " : \"" + value + "\"");
					nvppost.add(new BasicNameValuePair(name, value));
				}
				UrlEncodedFormEntity uefe2 = new UrlEncodedFormEntity(nvppost);
				HttpPost post2 = new HttpPost(url);
				post2.setEntity(uefe2);
				request = post2;
				break;
			case Constant.REST_DELETE:
				request = new HttpDelete(url);
				break;
			default:
				request = new HttpGet(url);
				break;
		}
        Log.d(TAG, url);
        request.addHeader("Accept-Language", "ind");
		request.setHeader("Content-Type", "application/json");

        ResponseHandler<String> handler = new BasicResponseHandler();
        result = httpclient.execute(request, handler);	       
//        HttpResponse response = httpclient.execute(request, context);
//        result = EntityUtils.toString(response.getEntity());
        Log.d(TAG, result);    
        return result;
    }



	public static String connect(String url, int method, UrlEncodedFormEntity uefe, int timeout, Context context) throws ClientProtocolException, SocketException, SocketTimeoutException, ConnectTimeoutException, Exception {
		String result = "{\"errorCode\":\"IB-0000\"}";

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);

		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, timeout);

		HttpClient httpClient = new DefaultHttpClient(httpParameters);

		ResponseHandler<String> handler = new BasicResponseHandler();
		HttpUriRequest request;


		switch (method) {
			case Constant.REST_GET:
				request = new HttpGet(url);
				break;
			case Constant.REST_PUT:
				request = new HttpPut(url);
				break;
			case Constant.REST_POST:
				HttpPost post = new HttpPost(url);
				post.getRequestLine();
				post.setEntity(uefe);
				request = post;

				Log.i("post", "Request Connection post : " + post.getURI().toString() + " get entity : " + uefe.getContentType());

				break;
			case Constant.REST_DELETE:
				request = new HttpDelete(url);
				break;
			default:
				request = new HttpGet(url);
				break;
		}

		request.addHeader("Accept-Language", "ind");
		request.setHeader("Accept-Language", "ind");

		request.addHeader("Content-Type", "application/json");
		request.setHeader("Content-Type", "application/json");

		request.addHeader("Accept", "application/json");
		request.setHeader("Accept", "application/json");
		result = httpClient.execute(request, handler);

		return result;
	}

	public static String connect(String url, int method, JSONObject json, int timeout, Context context) throws ClientProtocolException, SocketException, SocketTimeoutException, ConnectTimeoutException, Exception {
		String result = "{\"errorCode\":\"IB-0000\"}";

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);

		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, timeout);

		HttpClient httpClient = new DefaultHttpClient(httpParameters);

		ResponseHandler<String> handler = new BasicResponseHandler();
		HttpUriRequest request;


		switch (method) {
			case Constant.REST_GET:
				request = new HttpGet(url);

				Iterator<?> keys = json.keys();
				while(keys.hasNext()) {
					String key = (String)keys.next();
					request.addHeader(key, json.getString(key));
					request.setHeader(key, json.getString(key));
				}
				break;
			case Constant.REST_PUT:
				request = new HttpPut(url);
				break;
			case Constant.REST_POST:
				HttpPost post = new HttpPost(url);
//				if(json.has("Authorization")) {
//					post.addHeader("Authorization", json.getString("Authorization"));
//					post.setHeader("Authorization", json.getString("Authorization"));
//				} else {
//					if(json.has("file")) {
//						String filePath = json.getString("file");
//						MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//						if (filePath != null) {
//							Log.d("filepath","====== filepath " + filePath);
//							File file = new File(new URI(filePath));
//							Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
//							Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
//							mpEntity.addPart("avatar", new FileBody(file, "application/octet"));
//						}
//					} else {
						post.getRequestLine();
						post.setEntity(new StringEntity(json.toString()));
//					}
//				}

				request = post;

				Log.i("post", "Request Connection post : " + post.getURI().toString() + " json tostring : " + json.toString());

				break;
			case Constant.REST_DELETE:
				request = new HttpDelete(url);
				break;
			default:
				request = new HttpGet(url);
				break;
		}

		request.addHeader("Accept-Language", "ind");
		request.setHeader("Accept-Language", "ind");

		request.addHeader("Content-Type", "application/json");
		request.setHeader("Content-Type", "application/json");

		request.addHeader("Accept", "application/json");
		request.setHeader("Accept", "application/json");
		result = httpClient.execute(request, handler);

		return result;
	}
}
