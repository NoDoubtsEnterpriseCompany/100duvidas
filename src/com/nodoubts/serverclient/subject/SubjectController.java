package com.nodoubts.serverclient.subject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nodoubts.core.Subject;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;

public class SubjectController implements SubjectService {

	private ServerService serverService;

	public SubjectController() {
		serverService = ServerController.getInstance();
	}

	@Override
	public String addSubject(Subject subject) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/subjects/addsubject");
		Gson gsonSubject= new Gson();
		return serverService.post(builder.toString(), gsonSubject.toJson(subject));
	}

	@Override
	public Subject getSubject(String subjectName) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/subjects?name=").append(subjectName);
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
	
	@Override
	public Subject getSubjectById(String subjectId) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/subjects/subject/").append(subjectId);
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
	public List<Subject> getSubjects() throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/subjects");
		String json = serverService.get(builder.toString());
		List<Subject> subjects = new ArrayList<Subject>();
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				subjects.add((Subject)gson.fromJson(explrObject.toString(), Subject.class));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return subjects;
	}

}
