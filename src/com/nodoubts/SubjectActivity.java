package com.nodoubts;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.nodoubts.core.Lecture;
import com.nodoubts.core.Subject;
import com.nodoubts.serverclient.lecture.LectureController;
import com.nodoubts.serverclient.lecture.LectureService;

public class SubjectActivity extends Activity {
	
	private Subject subject;
	ListView professorsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.subject_activity);
	    
	    subject = (Subject) getIntent().getExtras().get("searchObj");
		TextView subjectNameTextView = (TextView) findViewById(R.id.subject_name_textview);
		TextView subjectDescriptionTextView = (TextView) findViewById(R.id.subject_description_textview);
		professorsListView = (ListView) findViewById(R.id.subject_teachersListView);
		
		if(subject!=null){
			subjectDescriptionTextView.setText(subject.getDescription());
			subjectNameTextView.setText(subject.getName());
			RequestLecturesTask getLectures = new RequestLecturesTask();
			getLectures.execute(subject);
		}
	}
	
	@Override
	public void onBackPressed() {
	   super.onBackPressed();
	   this.finish();
    }
	
	private class RequestLecturesTask extends AsyncTask<Subject, Void, List<Lecture>>{

		@Override
		protected List<Lecture> doInBackground(Subject... params) {
			Subject subject = params[0];
			LectureService lectureService = new LectureController();
			return lectureService.getSubjectLectures(subject.get_id());
		}
		
		@Override
		protected void onPostExecute(List<Lecture> result) {
			LectureListAdapter searchAdapter = new LectureListAdapter(
					SubjectActivity.this, result);
			System.out.println("Lectures: " + result.toString());
			professorsListView.setAdapter(searchAdapter);
		}
	}
}
