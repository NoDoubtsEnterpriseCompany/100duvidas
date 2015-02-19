package com.nodoubts.serverclient.lecture;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nodoubts.core.Lecture;
import com.nodoubts.core.ScheduledLecture;
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
		StringBuilder builder = new StringBuilder("/lectures/addlecture");
		Gson gsonObj = new Gson();
		return serverService.post(builder.toString(), gsonObj.toJson(lecture));
	}
	
	@Override
	public String scheduleLecture(ScheduledLecture lecture) throws ApplicationViewException {
		StringBuilder builder = new StringBuilder("/lectures/schedulelecture");
		Gson gsonObj = new Gson();
		return serverService.post(builder.toString(), gsonObj.toJson(lecture));
	}

	@Override
	public List<Lecture> getSubjectLectures(String subject_id){
		StringBuilder builder = new StringBuilder("/lectures/subjectlectures/"+subject_id);		
		List<Lecture> lectures = new ArrayList<Lecture>();
		
		try {
			JSONObject response = new JSONObject(
					serverService.get(builder.toString()));
			JSONArray jsonArray = response.getJSONArray("result");
			
			Gson serializer = new Gson();
			
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				lectures.
				add((Lecture)serializer.fromJson(explrObject.toString(), Lecture.class));
				
			}
		} catch (ApplicationViewException e) {
			Log.e("LectureController",e.getMessage());
		} catch (JSONException e1) {
			Log.e("ServerController",e1.getMessage());
		}
		return lectures;
	}
	
	@Override
	public List<ScheduledLecture> getScheduledLecturesFromTeacher(String teacher_id){
		StringBuilder builder = new StringBuilder("/lectures/scheduledlecture?teacher="+teacher_id);		
		List<ScheduledLecture> lectures = new ArrayList<ScheduledLecture>();
		
		try {
			JSONObject response = new JSONObject(
					serverService.get(builder.toString()));
			JSONArray jsonArray = response.getJSONArray("result");
			
			Gson serializer = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH").create();
			
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				lectures.
				add((ScheduledLecture)serializer.fromJson(explrObject.toString(), ScheduledLecture.class));
				
			}
		} catch (ApplicationViewException e) {
			Log.e("ServerController",e.getMessage());
		} catch (JSONException e1) {
			Log.e("ServerController",e1.getMessage());
		}
		return lectures;
	}
	
	@Override
	public List<ScheduledLecture> getScheduledLecturesFromStudent(String student_id){
		StringBuilder builder = new StringBuilder("/lectures/scheduledlecture?student="+student_id);		
		List<ScheduledLecture> lectures = new ArrayList<ScheduledLecture>();
		
		try {
			JSONObject response = new JSONObject(
					serverService.get(builder.toString()));
			JSONArray jsonArray = response.getJSONArray("result");
			
			Gson serializer = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH").create();
			
			for (int i=0; i<jsonArray.length();  i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				lectures.
				add((ScheduledLecture)serializer.fromJson(explrObject.toString(), ScheduledLecture.class));
				
			}
		} catch (ApplicationViewException e) {
			Log.e("ServerController",e.getMessage());
		} catch (JSONException e1) {
			Log.e("ServerController",e1.getMessage());
		}
		return lectures;
	}
}
