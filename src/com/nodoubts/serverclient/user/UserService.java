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
	/**
	 * return a list of ratings given an username
	 * @param userName the username of the user to get the ratings
	 * @return a list of ratings of the given user
	 * @throws ApplicationViewException
	 */
	public List<Rating> getRatings(String userName) throws ApplicationViewException;
	
	public String addRatingToUser(String teacherName, Rating rating)
			throws ApplicationViewException, JSONException;
}
