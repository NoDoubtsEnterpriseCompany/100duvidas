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
	private String city;
	private String profilePic;
	private String degree;
	private String speciality;
	private List<String> subjects;
	
	public Profile (){
	}
	
	public Profile(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
