package com.nodoubts.serverclient.user;

public enum UserGenderEnum {
	MALE(0),FEMALE(1);
	
	private final int gender;
    	UserGenderEnum(int gender) { this.gender = gender;
    }
    public int getValue() { return gender; }
}
