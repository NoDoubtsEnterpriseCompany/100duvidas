package com.nodoubts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.nodoubts.core.Lecture;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.lecture.LectureController;
import com.nodoubts.serverclient.lecture.LectureService;

public class ScheduleLectureActivity extends Activity {

	private Lecture lecture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    this.setContentView(R.layout.activity_view_professor);
	    
	    lecture = (Lecture) getIntent().getExtras().get("searchObj");
	    if(lecture!=null){
			AsyncTask<
			String, Void, Object> scheduleLecture = new ScheduleLectureTask(); 
			scheduleLecture.execute(lecture.getSubject());
	    }
	}
	
	
	
	private class ScheduleLectureTask extends AsyncTask<String, Void, Object>{

		ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(ScheduleLectureActivity.this);
			this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}
		
		@Override
		protected Object doInBackground(String... params) {
			LectureService lectureController = new LectureController();
			try {
				lectureController.saveLecture(lecture);
				return lecture;
			} catch (ApplicationViewException e) {
				return e;
			}
		}
	}
}
