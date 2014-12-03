package com.nodoubts.core;

public class User {
	
	 private String username;
	 private String password;
	 private String email;
	 private Profile profile;
	
	public User(String username,String password,String email){
		this.username = username;
		this.password = password;
		this.email = email;
		this.profile = new Profile();
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
