package com.nodoubts.serverclient.subject;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nodoubts.core.Subject;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;
import com.nodoubts.util.Constants;

public class SubjectController implements SubjectService {

	private ServerService serverService;
	private final String URL_USER = "http://192.168.25.5:3000/subjects";

	public SubjectController() {
		serverService = new ServerController();
		Constants.init();
	}

	@Override
	public String addSubject(Subject subject) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder(URL_USER).append("/addsubject");
		Gson gsonSubject= new Gson();
		return serverService.post(builder.toString(), gsonSubject.toJson(subject));
	}

	@Override
	public Subject getSubject(String subjectName) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder(URL_USER).append("?name=").append(subjectName);
		String json = serverService.get(builder.toString());
		Subject subject = null;
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(json);
			subject = gson.fromJson(jsonObject.getString("result"), Subject.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return subject;
	}

	//TODO verificar como receber uma lista
	@Override
	public Subject getSubjects() throws ApplicationViewException {
		StringBuilder builder = new StringBuilder(URL_USER);
		String json = serverService.get(builder.toString());
		Subject subject = null;
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(json);
			subject = gson.fromJson(jsonObject.getString("result"), Subject.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return subject;
	}

}
