package com.nodoubts.serverclient.grouplecture;

import com.google.gson.Gson;
import com.nodoubts.core.GroupLecture;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;

public class GroupLectureController implements GroupLectureService {

	private ServerService serverService;
	
	public GroupLectureController() {
		serverService = ServerController.getInstance();
	}
	
	@Override
	public String saveGroupLecture(GroupLecture groupLecture) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/grouplectures/addGroupLecture");
		Gson gsonUser = new Gson();
		return serverService.post(builder.toString(), gsonUser.toJson(groupLecture));
	}
}
