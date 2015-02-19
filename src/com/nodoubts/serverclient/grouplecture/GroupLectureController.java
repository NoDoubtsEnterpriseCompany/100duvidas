package com.nodoubts.serverclient.grouplecture;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nodoubts.core.GroupLecture;
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
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH").create();
		try {
			String json = serverService.get(builder.toString());
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				groupLectures.add((GroupLecture)gson.fromJson(explrObject.toString(), GroupLecture.class));
			}
		} catch (JSONException e) {
						Log.e("GroupLectureController",e.getMessage());
		} catch (ApplicationViewException e) {
						Log.e("GroupLectureController",e.getMessage());
		}
		return groupLectures;
	}
	
	@Override
	public List<GroupLecture> getGroupLecturesByUser(String username) {
		StringBuilder builder = new StringBuilder("/grouplectures?professor=");
		builder.append(username);
		List<GroupLecture> groupLectures = new ArrayList<GroupLecture>();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH").create();
		try {
			String json = serverService.get(builder.toString());
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				groupLectures.add((GroupLecture)gson.fromJson(explrObject.toString(), GroupLecture.class));
			}
		} catch (JSONException e) {
			Log.e("GroupLectureController",e.getMessage());
		} catch (ApplicationViewException e) {
			Log.e("GroupLectureController", e.getMessage());
		}
		return groupLectures;
	}
	
	@Override
	public List<GroupLecture> getGroupLecturesOfStudent(String studentId) {
		StringBuilder builder = new StringBuilder("/grouplectures?student=");
		builder.append(studentId);
		List<GroupLecture> groupLectures = new ArrayList<GroupLecture>();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH").create();
		try {
			String json = serverService.get(builder.toString());
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				groupLectures.add((GroupLecture)gson.fromJson(explrObject.toString(), GroupLecture.class));
			}
		} catch (JSONException e) {
			Log.e("GroupLectureController",e.getMessage());
		} catch (ApplicationViewException e) {
			Log.e("GroupLectureController", e.getMessage());
		}
		return groupLectures;
	}
}
