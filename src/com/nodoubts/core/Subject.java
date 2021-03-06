package com.nodoubts.core;

import com.nodoubts.ui.subject.SubjectActivity;

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
	
	public Subject(String subjectName, String description){
		this.name = subjectName;
		this.description = description;
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
	
	public String get_id() {
		return _id;
	}
	
	public void set_id(String _id) {
		this._id = _id;
	}
}
