package com.nodoubts.core;

import java.io.Serializable;

public interface SearchType extends Serializable{

	public Class<?> getActivityClass();
	public String getName();
}
