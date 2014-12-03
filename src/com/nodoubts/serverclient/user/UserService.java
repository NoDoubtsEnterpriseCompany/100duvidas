package com.nodoubts.serverclient.user;

import com.nodoubts.core.User;

public interface UserService {

	public User findUser(String username);
	
	public String actualizeUser(User user);
	
	public String saveUser(User user);
}
