package com.nodoubts.serverclient.user;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nodoubts.core.User;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;
import com.nodoubts.util.Constants;

public class UserController implements UserService {

	private ServerService serverService;
	private final String URL_USER = "http://192.168.25.28:3000/users/";

	public UserController() {
		serverService = new ServerController();
		Constants.init();
	}

	@Override
	public  User findUser(String username) {
		StringBuilder builder = new StringBuilder(URL_USER).append("/user/")
				.append(username);
		String json = serverService.get(builder.toString());
		User user = null;
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(json);
			user = gson.fromJson(jsonObject.getString("result"), User.class);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
	public String saveUser(User user) {
		StringBuilder builder = new StringBuilder(URL_USER)
				.append("/adduser");
		Gson gsonUser = new Gson();
		return serverService.post(builder.toString(), gsonUser.toJson(user));
	}

	@Override
	public String authenticateUser(String jsonTransaction) {
		String URL_LOGIN = URL_USER+"login";
		System.out.println("URL: "+URL_LOGIN);
		StringBuilder builder = new StringBuilder(URL_LOGIN);
		return serverService.post(builder.toString(), jsonTransaction);
	}
}
