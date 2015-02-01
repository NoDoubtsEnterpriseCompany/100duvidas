package com.nodoubts.serverclient.lecture;

import com.google.gson.Gson;
import com.nodoubts.core.Lecture;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;

public class LectureController implements LectureService {

	private ServerService serverService;
	
	public LectureController() {
		serverService = ServerController.getInstance();
	}

	@Override
	public String saveLecture(Lecture lecture) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/users/requestlecture");
		Gson gsonUser = new Gson();
		return serverService.post(builder.toString(), gsonUser.toJson(lecture));
	}
}
