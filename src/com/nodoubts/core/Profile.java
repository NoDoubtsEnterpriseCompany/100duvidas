package com.nodoubts.core;

import java.util.List;

public class Profile {
	
	private String name;
	private String age;
	private String profilePic;
	private String degree;
	private String speciality;
	private List<Subject> subjects;
	
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
	
	public List<Subject> getSubjects() {
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
	
	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
}
