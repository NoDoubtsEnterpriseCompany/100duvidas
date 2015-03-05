package com.nodoubts.serverclient.util;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nodoubts.MainActivity;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	Context _context;
	
	int PRIVATE_MODE = 0;
	
	private static final String PREF_NAME = "Authentication";
	
	private static final String IS_LOGIN = "IsLoggedIn";
	
	public static String KEY_LOGIN = "Login";
	
	public static String KEY_AUTH_TOKEN = "AuthToken";

	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	public void createLoginSession(String authToken, String login){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		 
		editor.putString(KEY_AUTH_TOKEN, authToken);
		editor.putString(KEY_LOGIN, login);
		 
		// commit changes
		editor.commit();
    }

	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> session = new HashMap<String, String>();
        // user name
		session.put(KEY_AUTH_TOKEN, pref.getString(KEY_AUTH_TOKEN, null));
		 
		// user email id
		session.put(KEY_LOGIN, pref.getString(KEY_LOGIN, null));
		 
		// return user
		    return session;
	}
	
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}

	public void logoutUser(){
		editor.clear();
		editor.commit();
		Intent i = new Intent(_context, MainActivity.class);		
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}
}