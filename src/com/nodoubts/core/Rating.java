package com.nodoubts.core;


public class Rating  implements SearchType{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float score;
	private String comment;
	private String commenterId;
	
	public Rating(float score, String comment, String commenter){
		this.score=score;
		this.comment=comment;
		this.commenterId=commenter;
	}
	
	public String getCommenter() {
		return commenterId;
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
