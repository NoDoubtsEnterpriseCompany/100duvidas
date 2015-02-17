package com.nodoubts.serverclient.user;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.nodoubts.core.Rating;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;

public class UserController implements UserService {

	private ServerService serverService;

	public UserController() {
		serverService = ServerController.getInstance();
	}

	@Override
	public User findUser(String username) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/users/user/")
				.append(username);
		String json = serverService.get(builder.toString());
		User user = null;
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(json);
			user = gson.fromJson(jsonObject.getString("result"), User.class);
		} catch (JSONException e) {
			Log.e("UserControler",e.getMessage());
		}
		return user;
	}

	public User findUserByEmail(String email) throws ApplicationViewException {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		String paramsStr = URLEncodedUtils.format(params, "utf-8");

		String query = "/users/user?".concat(paramsStr);

		String response = serverService.get(query.toString());
		User user = null;
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(response);
			user = gson.fromJson(jsonObject.getString("result"), User.class);
		} catch (JSONException e) {
			Log.e("UserControler",e.getMessage());
		}
		return user;

	}

	/*
	 * TODO Adaptar Put a mudança do password
	 * 
	 * @author Jose Iago, 03-12-2014
	 */
	@Override
	public String actualizeUser(User user) {
		StringBuilder builder = new StringBuilder("/users/updateuser/")
				.append(user.getUsername());
		Gson gsonUser = new Gson();
		return serverService.put(builder.toString(), gsonUser.toJson(user));
	}

	@Override
	public String saveUser(User user) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/users/adduser");
		Gson gsonUser = new Gson();
		return serverService.post(builder.toString(), gsonUser.toJson(user));
	}

	@Override
	public String authenticateUser(String jsonTransaction)
			throws ApplicationViewException {
		String URL_LOGIN = "/users/login";
		StringBuilder builder = new StringBuilder(URL_LOGIN);
		return serverService.post(builder.toString(), jsonTransaction);
	}

	@Override
	public List<User> searchForUsers(String subject)
			throws ApplicationViewException {
		StringBuilder builder = new StringBuilder();
		if (subject != null && !subject.equals("")) {
			builder.append("?subject=");
			builder.append(subject);
		}
		String json = serverService.get(builder.toString());
		List<User> users = new ArrayList<User>();
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				users.add((User) gson.fromJson(explrObject.toString(),
						User.class));
			}
		} catch (JSONException e) {
			Log.e("UserControler",e.getMessage());
		}
		return users;
	}

	@Override
	public String addSubjectToUser(String jsonTransaction)
			throws ApplicationViewException {

		StringBuilder builder = new StringBuilder("/users/addsubject");
		return serverService.post(builder.toString(), jsonTransaction);
	}

	@Override
	public String addRatingToUser(String teacherUserName,Rating rating) throws ApplicationViewException, JSONException {
		StringBuilder builder = new StringBuilder("/users/addrating/"
				+ teacherUserName);
		Gson gson = new Gson();
		JSONObject json = new JSONObject();
		json.put("student", gson.toJson(rating.getCommenter(),String.class));
		json.put("rating", gson.toJson(rating,Rating.class));
		return serverService.post(builder.toString(), json.toString());
	}

	@Override
	public List<Rating> getRatings(String userName) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/users/rating/").append(userName);
		List<Rating> ratings = new ArrayList<Rating>();
		
		try {
			JSONObject response = new JSONObject(serverService.get(builder.toString()));
			JSONArray jsonArray = response.getJSONArray("result");
			Gson serializer = new Gson();
			for (int i=0; i<jsonArray.length();i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				ratings.add((Rating)serializer.fromJson(explrObject.toString(), Rating.class));
			}
		} catch (ApplicationViewException e) {
			Log.e("UserControler",e.getMessage());
		} catch (JSONException e1) {
			Log.e("UserControler",e1.getMessage());
		}
		
		return ratings;
	}

}
