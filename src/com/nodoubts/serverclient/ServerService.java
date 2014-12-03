package com.nodoubts.serverclient;

public interface ServerService {

	public String get(String url);
	
	public String post(String url, String json);
	
	public String put(String url, String json);
	
	public String delete(String url);
}
