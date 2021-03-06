package com.nodoubts.core;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String age;
	private int gender;
	private String profilePic;
	private String city;

	private String degree;
	private String speciality;
	private List<String> subjects;
	private List<String> groupLecturesCreated;
	private List<String> groupLecturesRegistered;
	private List<String> ratings;
	private float totalScore;
	private float mean;
	
	public Profile (){
		
	}
	
		
	
	public float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

	public float getMean() {
		return mean;
	}

	public void setMean(float mean) {
		this.mean = mean;
	}
	
	public Profile(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	public String getDegree() {
		return degree;
	}
	public String getName() {
		return name;
	}
	
	public String getProfilePic() {
		return profilePic;
	}
	
	public String getSpeciality() {
		return speciality;
	}
	
	public List<String> getSubjects() {
		return subjects;
	}
	
	public void setAge(String age) {
		this.age = age;
	}
	
	public void setDegree(String degree) {
		this.degree = degree;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}
	public int getGender() {
		return this.gender;
	}
	public void setGender(int gender) {
		this.gender= gender;
	}
	
	public List<String> getRatings(){
		return ratings;
	}

}
