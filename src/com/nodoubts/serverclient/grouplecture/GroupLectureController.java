package com.nodoubts.serverclient.grouplecture;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nodoubts.core.GroupLecture;
import com.nodoubts.core.Subject;
import com.nodoubts.core.User;
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
	
	@Override
	public String registerStudent(String groupLectureId, User user)
			throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/grouplectures/grouplecture/");
		builder.append(groupLectureId);
		builder.append("/addUser");
		Gson gsonUser = new Gson();
		return serverService.put(builder.toString(), gsonUser.toJson(user));
	}
	
	@Override
	public List<GroupLecture> getGroupLecturesBySubject(String subjectId) {
		StringBuilder builder = new StringBuilder("/grouplectures?subject=");
		builder.append(subjectId);
		List<GroupLecture> groupLectures = new ArrayList<GroupLecture>();
		Gson gson = new Gson();
		try {
			String json = serverService.get(builder.toString());
			System.out.println(json);
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				groupLectures.add((GroupLecture)gson.fromJson(explrObject.toString(), GroupLecture.class));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ApplicationViewException e) {
			e.printStackTrace();
		}
		return groupLectures;
	}
}
