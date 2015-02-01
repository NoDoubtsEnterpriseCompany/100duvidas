package com.nodoubts.serverclient.user;

import java.util.List;

import org.json.JSONException;

import com.nodoubts.core.Rating;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;

public interface UserService {

	public User findUser(String username) throws ApplicationViewException;
	
	public String actualizeUser(User user);
	
	public String saveUser(User user) throws ApplicationViewException;
	
	public String authenticateUser(String jsonTransaction) throws ApplicationViewException;
	
	public User findUserByEmail(String email) throws ApplicationViewException;
	
	public List<User> searchForUsers(String disciplina) throws ApplicationViewException;
	
	public String addSubjectToUser(String jsonTransaction) throws ApplicationViewException;

	String addRatingToUser(String teacherName, User user, Rating rating) throws ApplicationViewException, JSONException;
}
