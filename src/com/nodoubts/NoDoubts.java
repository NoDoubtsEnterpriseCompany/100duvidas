package com.nodoubts;

import android.app.Application;
import android.content.Context;

public class NoDoubts extends Application{
	private static Context context;
	
	public void onCreate(){
		super.onCreate();
		NoDoubts.context = getApplicationContext();
	}
	
	public static Context getAppContext(){
		return NoDoubts.context;
	}
}
