package com.nodoubts.core;

import java.io.Serializable;

public class User implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	 private String password;
	 private String email;
	 private Profile profile;
	
	public User(String username,String password,String email){
		this.username = username;
		this.password = password;
		this.email = email;
		this.profile = new Profile("tiaraju","21");
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
