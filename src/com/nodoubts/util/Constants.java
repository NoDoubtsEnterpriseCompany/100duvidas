package com.nodoubts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Constants {
	
	public static final String CONF_FILE_PATH = "conf" + File.separator + "conf.properties";

	
	public static String SERVER_LOCATION;
	public static String URL_USER;
	public static String URL_LOGIN;
	
	
	public static void init(){
		Properties prop = new Properties();
		FileInputStream file = null;
		try {
			file = new FileInputStream(CONF_FILE_PATH);
			prop.load(file);
		} catch (FileNotFoundException e) {
			System.out.println("did not work file not found");
		} catch (IOException e) {
			System.out.println("did not work io ");
		}
		
		
		SERVER_LOCATION = prop.getProperty("SERVER_LOCATION");
		URL_USER= SERVER_LOCATION+"/users";
		System.out.println(file == null);
		System.out.println("url_user = " + URL_USER);
		
		if(file != null ){
			try {
				file.close();
			} catch (IOException e) {
			}
		}
	}

}
