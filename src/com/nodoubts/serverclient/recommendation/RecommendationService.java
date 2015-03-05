package com.nodoubts.serverclient.recommendation;

import java.util.List;

import org.json.JSONObject;

import com.nodoubts.core.Recommendation;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;

public interface RecommendationService {
	
	public List<Recommendation> getRecommendations(User user) throws ApplicationViewException;

	String addRecommendation(Recommendation recommendation, User user) throws ApplicationViewException;
}
