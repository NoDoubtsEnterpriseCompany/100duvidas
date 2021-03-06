package com.nodoubts.core;

import java.util.ArrayList;
import java.util.List;

import com.nodoubts.ui.user.ViewProfessorActivity;


public class User implements SearchType{
	private static final long serialVersionUID = -3587210382909215826L;
	
	private String _id;
	private String username;
	private String password;
	private String email;
	private Profile profile;
	
	private transient List<SearchType> lecturesCreatedByUser;
	private transient List<SearchType> lecturesRegisteredByUser;

	public User(String username,String password,String email){
		this.username = username;
		this.password = password;
		this.email = email;
		this.profile = new Profile(""); //TODO: remove this
	}

	public User(){this.profile = new Profile("");}

	public User(String username,String password,String email,String name){
		this.username = username;
		this.password = password;
		this.email = email;
		this.profile = new Profile(name);
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

	@Override
	public Class<?> getActivityClass() {
		return ViewProfessorActivity.class; //TODO: Might have to refact this for common users
	}

	@Override
	public String getName() {
		return profile.getName();
	}
	
	public String toString(){
		return getName();
	}

	public float getScore() {
		return getProfile().getMean();
	}
	
	public List<String> getRatings(){
		return getProfile().getRatings();
	}
	
	public void addRating(String rating){
		getRatings().add(rating);
	}

	public String get_id() {
		return _id;
	}
	
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public List<SearchType> getLecturesCreatedByUser() {
		if (this.lecturesCreatedByUser == null) {
			setLecturesCreatedByUser(new ArrayList<SearchType>());
		}
		return lecturesCreatedByUser;
	}
	
	public void setLecturesCreatedByUser(List<SearchType> lecturesCreatedByUser) {
		this.lecturesCreatedByUser = lecturesCreatedByUser;
	}
	
	public List<SearchType> getLecturesRegisteredByUser() {
		if (this.lecturesRegisteredByUser == null) {
			setLecturesRegisteredByUser(new ArrayList<SearchType>());
		}
		return lecturesRegisteredByUser;
	}
	
	public void setLecturesRegisteredByUser(List<SearchType> lecturesRegisteredByUser) {
		this.lecturesRegisteredByUser = lecturesRegisteredByUser;
	}
}
