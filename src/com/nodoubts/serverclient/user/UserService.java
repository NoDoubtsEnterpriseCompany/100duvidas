package com.nodoubts.serverclient.user;

import java.util.List;

import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;

public interface UserService {

	public User findUser(String username) throws ApplicationViewException;
	
	public String actualizeUser(User user);
	
	public String saveUser(User user) throws ApplicationViewException;
	
	public String authenticateUser(String jsonTransaction) throws ApplicationViewException;
	
	public User findUserByEmail(String email) throws ApplicationViewException;
	
	public List<User> searchForUsers(String disciplina) throws ApplicationViewException;

}
