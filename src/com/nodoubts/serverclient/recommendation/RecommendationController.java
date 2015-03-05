package com.nodoubts.serverclient.recommendation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nodoubts.core.Recommendation;
import com.nodoubts.core.Subject;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;

public class RecommendationController implements RecommendationService {

	private ServerService serverService;

	public RecommendationController() {
		serverService = ServerController.getInstance();
	}

	@Override
	public String addRecommendation(Recommendation recommendation, User user) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/users/addrecommendation/".concat(user.getUsername()));
		Gson gsonRecommendation= new Gson();
		return serverService.post(builder.toString(), gsonRecommendation.toJson(recommendation));
	}


	//TODO verificar como receber uma lista
	@Override
	public List<Recommendation> getRecommendations(User user) throws ApplicationViewException {
		user.getUsername();
		StringBuilder builder = new StringBuilder("/users/recommendation/".concat(user.getUsername()));
		String json = serverService.get(builder.toString());
		List<Recommendation> recommendations = new ArrayList<Recommendation>();
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				recommendations.add((Recommendation)gson.fromJson(explrObject.toString(), Recommendation.class));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return recommendations;
	}

}
