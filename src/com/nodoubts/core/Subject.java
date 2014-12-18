package com.nodoubts.core;

import java.io.Serializable;

public class Subject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	
	
	public Subject(String subjectName){
		this.name = subjectName;
	}
	

	public String getSubjectName() {
		return name;
	}

	public void setSubjectName(String subjectName) {
		this.name = subjectName;
	}
	
	@Override
	public String toString() {
		return name;
	}
	

}
