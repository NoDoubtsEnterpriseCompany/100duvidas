package com.nodoubts.core;

import java.util.Date;
import java.util.List;

import com.nodoubts.ViewProfessorActivity;

public class Lecture implements SearchType {
	private static final long serialVersionUID = -30791561334913081L;
	
	private String subject;
	private User teacher;
	private List<String> students;
	private Double price;
	private Date date;
	
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

	public List<String> getStudent() {
		return students;
	}

	public void setStudent(List<String> student) {
		this.students = student;
	}

	@Override
	public String getName() {
		return teacher.getName();
	}
}
