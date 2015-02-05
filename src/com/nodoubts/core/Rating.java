package com.nodoubts.core;


public class Rating  implements SearchType{
	
	private float score;
	private String comment;
	private User commenter;
	
	public Rating(float score, String comment, User commenter){
		this.score=score;
		this.comment=comment;
		this.commenter=commenter;
	}
	
	public User getCommenter() {
		return commenter;
	}

	public Rating(){};

	public float getScore(){
		return score;
	}
	
	public String getComment(){
		return this.comment;
	}

	@Override
	public Class<?> getActivityClass() {
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
