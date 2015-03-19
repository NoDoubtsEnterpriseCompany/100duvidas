package com.nodoubts.serverclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

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

import android.util.Log;

import com.nodoubts.NoDoubts;
import com.nodoubts.R;
import com.nodoubts.exceptions.ApplicationViewException;

public class ServerController implements ServerService{
	
	private static ServerController instance = null;
	private String HOST_URL;
	
	private ServerController(){
		String hostName = NoDoubts.getAppContext().getString(R.string.host_name);
		String port = NoDoubts.getAppContext().getString(R.string.port);
		HOST_URL = hostName.concat(":").concat(port);
	}
	

	public static ServerController getInstance(){
		if(instance == null){
			instance = new ServerController();
		}
		return instance;
	}

	@Override
	public String get(String uri) throws ApplicationViewException {
		String response = null;
		String url = HOST_URL.concat(uri);
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
			Log.e("ServerController",e.getMessage());
		} catch (ClientProtocolException e) {
			Log.e("ServerController",e.getMessage());
		} catch (IOException e) {
			Log.e("ServerController",e.getMessage());
		}		

		return response;

	}
	
	@Override
	public String get(String uri, Map<String, String> parametros) throws ApplicationViewException {
		String url = HOST_URL.concat(uri);
		StringBuilder builder = new StringBuilder(url);
		if (parametros != null) {
			builder.append("?");
			int count = 0;
			for (String key : parametros.keySet()) {
				builder.append(key);
				builder.append(":");
				builder.append(parametros.get(key));
				if (count < parametros.size() - 1) {
					builder.append("&&");
				}
				count++;
			}
		}
		return get(builder.toString());
	}


	private void checkGETResponse(
			int responseStatusCode, String response) throws ApplicationViewException {
		if(responseStatusCode != HttpStatus.SC_OK){
			try {
				JSONObject jsonObject = new JSONObject(response);
				throw new ApplicationViewException(
						jsonObject.getJSONObject("error").getString("message"));
			} catch (JSONException e) {
				Log.e("ServerController",e.getMessage());
			}
		}		
	}
	
	private void checkPOSTResponse(int responseStatusCode, String response) throws ApplicationViewException {
		if(responseStatusCode != HttpStatus.SC_CREATED){
			try {
				JSONObject jsonObject = new JSONObject(response);
				throw new ApplicationViewException(jsonObject.getJSONObject("error").getString("message"));
			} catch (JSONException e) {
				Log.e("ServerController",e.getMessage());
			}
		}		
	}

	@Override
	public String post(String uri, String json) throws ApplicationViewException {
		String url = HOST_URL.concat(uri);
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
	            httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
			}

			httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			
			checkPOSTResponse(httpResponse.getStatusLine().getStatusCode(), response);

		} catch (UnsupportedEncodingException e) {
			Log.e("ServerController",e.getMessage());
		} catch (ClientProtocolException e) {
			Log.e("ServerController",e.getMessage());
		} catch (IOException e) {
			Log.e("ServerController",e.getMessage());
		}
		return response;
	}

	@Override
	public String put(String uri, String json) {
		String url = HOST_URL.concat(uri);
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
	            httpPut.setHeader("Content-type", "application/json;charset=UTF-8");
			}

			httpResponse = httpClient.execute(httpPut);
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			Log.e("ServerController",e.getMessage());
		} catch (ClientProtocolException e) {
			Log.e("ServerController",e.getMessage());
		} catch (IOException e) {
			Log.e("ServerController",e.getMessage());
		}
		return response;
	}

	@Override
	public String delete(String uri) {
		return null;
	}
}
