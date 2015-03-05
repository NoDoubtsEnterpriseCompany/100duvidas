package com.nodoubts.core;

import com.nodoubts.ui.subject.SubjectActivity;

public class Recommendation implements SearchType {
	private static final long serialVersionUID = 3364823434353276045L;
	private String _id;
	private String teacherUserName;
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
	
	public Recommendation(String userName, String description){
		this.teacherUserName = userName;
		this.description = description;
	}
	

	public String getTeacherUserName() {
		return teacherUserName;
	}

	public void setTeacherUserName(String userName) {
		this.teacherUserName = userName;
	}
	
	@Override
	public String toString() {
		return description;
	}


	@Override
	public Class<?> getActivityClass() {
		return SubjectActivity.class;
	}


	public String getName() {
		return getDescription();
	}
	
	public String get_id() {
		return _id;
	}
	
	public void set_id(String _id) {
		this._id = _id;
	}
}
