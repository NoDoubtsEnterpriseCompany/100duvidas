package com.nodoubts.serverclient;

import com.nodoubts.exceptions.ApplicationViewException;

public interface ServerService {

	public String get(String url) throws ApplicationViewException;
	
	public String post(String url, String json) throws ApplicationViewException;
	
	public String put(String url, String json);
	
	public String delete(String url);
}
