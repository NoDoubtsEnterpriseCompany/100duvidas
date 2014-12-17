package com.nodoubts.serverclient.user;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;
import com.nodoubts.util.Constants;

public class UserController implements UserService {

	private ServerService serverService;
	private final String URL_USER = "http://192.168.25.5:3000/users/";

	public UserController() {
		serverService = new ServerController();
		Constants.init();
	}

	@Override
	public  User findUser(String username) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder(URL_USER).append("/user/")
				.append(username);
		String json = serverService.get(builder.toString());
		User user = null;
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(json);
			user = gson.fromJson(jsonObject.getString("result"), User.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public User findUserByEmail(String email) throws ApplicationViewException{
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		String paramsStr = URLEncodedUtils.format(params, "utf-8");
		
		String query = URL_USER.concat("/user?").concat(paramsStr);
		
		String response = serverService.get(query.toString());
		User user = null;
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(response);
			user = gson.fromJson(jsonObject.getString("result"), User.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
		
	}

	/*TODO Adaptar Put a mudança do password
	 * @author Jose Iago, 03-12-2014
	 */
	@Override
	public String actualizeUser(User user) {
		StringBuilder builder = new StringBuilder(URL_USER)
				.append("/updateuser/").append(user.getUsername());
		Gson gsonUser = new Gson();
		return serverService.put(builder.toString(), gsonUser.toJson(user));
	}

	@Override
	public String saveUser(User user) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder(URL_USER)
				.append("/adduser");
		Gson gsonUser = new Gson();
		return serverService.post(builder.toString(), gsonUser.toJson(user));
	}

	@Override
	public String authenticateUser(String jsonTransaction) throws ApplicationViewException {
		String URL_LOGIN = URL_USER+"/login";
		StringBuilder builder = new StringBuilder(URL_LOGIN);
		return serverService.post(builder.toString(), jsonTransaction);
	}
}
