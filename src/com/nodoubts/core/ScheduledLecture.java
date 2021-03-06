package com.nodoubts.core;

import com.nodoubts.ViewScheduledLectureActivity;


public class ScheduledLecture extends Lecture implements SearchType {
	private static final long serialVersionUID = -30791561334913081L;
	
	private String student;
	
	public String getStudent(){
		return student;
	}
	
	public void setStudent(String student){
		this.student = student;
	}
	
	@Override
	public Class<?> getActivityClass() {
		return ViewScheduledLectureActivity.class;
	}
}
