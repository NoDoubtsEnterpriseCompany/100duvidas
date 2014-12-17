package com.nodoubts.serverclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.nodoubts.exceptions.ApplicationViewException;

public class ServerController implements ServerService{

	@Override
	public String get(String url) throws ApplicationViewException {
		String response = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			HttpGet httpGet = new HttpGet(url);

			httpResponse = httpClient.execute(httpGet);
			
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			
			checkGETResponse(httpResponse.getStatusLine().getStatusCode(), response);

		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getMessage());
		} catch (ClientProtocolException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}		

		return response;

	}

	private void checkGETResponse(
			int responseStatusCode, String response) throws ApplicationViewException {
		if(responseStatusCode != HttpStatus.SC_OK){
			try {
				JSONObject jsonObject = new JSONObject(response);
				throw new ApplicationViewException(
						jsonObject.getJSONObject("error").getString("message"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}		
	}
	
	private void checkPOSTResponse(int responseStatusCode, String response) throws ApplicationViewException {
		if(responseStatusCode != HttpStatus.SC_CREATED){
			try {
				JSONObject jsonObject = new JSONObject(response);
				throw new ApplicationViewException(jsonObject.getJSONObject("error").getString("message"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public String post(String url, String json) throws ApplicationViewException {
		String response = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			HttpPost httpPost = new HttpPost(url);
			// adding post params
			if (json != null) {
				StringEntity se = new StringEntity(json);
	            httpPost.setEntity(se);
	 
	            // Headers to inform server about the type of the content   
	            httpPost.setHeader("Accept", "application/json");
	            httpPost.setHeader("Content-type", "application/json");
			}

			httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			
			checkPOSTResponse(httpResponse.getStatusLine().getStatusCode(), response);

		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} catch (ClientProtocolException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return response;
	}

	@Override
	public String put(String url, String json) {
		String response = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			HttpPut httpPut = new HttpPut(url);
			// adding put params
			if (json != null) {
				StringEntity se = new StringEntity(json);
	            httpPut.setEntity(se);
	 
	            // Headers to inform server about the type of the content   
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
			}

			httpResponse = httpClient.execute(httpPut);
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} catch (ClientProtocolException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return response;
	}

	@Override
	public String delete(String url) {
		return null;
	}
}
