package com.nodoubts.core;

import java.util.List;

import com.nodoubts.SubjectActivity;

public class Subject implements SearchType {
	private static final long serialVersionUID = 8116536122442142507L;
	
	private String _id;
	private String name;
	private String description;
	
	public String getId() {
		return _id;
	}
	
	public void setId(String id) {
		this._id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private List<User> teachers;
	
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


	@Override
	public Class<?> getActivityClass() {
		return SubjectActivity.class;
	}


	@Override
	public String getName() {
		return getSubjectName();
	}


	public List<User> getProfessors() {
		return teachers;
	}
	
	public void setProfessors(List<User> professors){
		this.teachers = professors;
	}

}
