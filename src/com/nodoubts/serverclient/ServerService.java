package com.nodoubts.serverclient;

import java.util.Map;

import com.nodoubts.exceptions.ApplicationViewException;

public interface ServerService {

	public String get(String url) throws ApplicationViewException;
	
	public String get(String url, Map<String, String> parametros) throws ApplicationViewException;
	
	public String post(String url, String json) throws ApplicationViewException;
	
	public String put(String url, String json);
	
	public String delete(String url);
}
