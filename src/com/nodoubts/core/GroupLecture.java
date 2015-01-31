package com.nodoubts.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupLecture implements Serializable, SearchType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String _id;
	private int numMaxStudents;
	private List<User> studentsRegistered;
	private Date date;
	private User professor;
	private String name;
	private float price;
	private String address;
	
	public GroupLecture() {
		this.professor = new User();
		this.studentsRegistered = new ArrayList<User>();
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public int getNumMaxStudents() {
		return numMaxStudents;
	}
	public void setNumMaxStudents(int numMaxStudents) {
		this.numMaxStudents = numMaxStudents;
	}
	public List<User> getStudentsRegistered() {
		return studentsRegistered;
	}
	public void setStudentsRegistered(List<User> studentsRegistered) {
		this.studentsRegistered = studentsRegistered;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public User getProfessor() {
		return professor;
	}
	
	public void setProfessor(User professor) {
		this.professor = professor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Class<?> getActivityClass() {
		return GroupLecture.class;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}
