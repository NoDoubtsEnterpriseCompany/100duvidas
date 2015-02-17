package com.nodoubts.core;

import java.util.Date;

import com.nodoubts.ui.user.ViewProfessorActivity;

public class Lecture implements SearchType {
	private static final long serialVersionUID = -30791561334913081L;
	
	protected String subject;
	protected User teacher;
	protected Double price;
	protected Date date;
	protected String address;
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public Class<?> getActivityClass() {
		return ViewProfessorActivity.class;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@Override
	public String getName() {
		return teacher.getName();
	}

	public String getAddress() {
		return address;
	}
	
	public String setAddress(String address) {
		return this.address = address;
	}
}
