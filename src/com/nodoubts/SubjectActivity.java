package com.nodoubts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.nodoubts.core.Lecture;
import com.nodoubts.core.Subject;
import com.nodoubts.core.User;

public class SubjectActivity extends Activity {
	
	private Subject subject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	   //set content view AFTER ABOVE sequence (to avoid crash)
	    this.setContentView(R.layout.subject_activity);
	    
	    subject = (Subject) getIntent().getExtras().get("searchObj");
		TextView subjectNameTextView = (TextView) findViewById(R.id.subject_name_textview);
		TextView subjectDescriptionTextView = (TextView) findViewById(R.id.subject_description_textview);
		ListView professorsListView = (ListView) findViewById(R.id.subject_teachersListView);
		
		if(subject!=null){
			subjectDescriptionTextView.setText(subject.getDescription());
			subjectNameTextView.setText(subject.getName());
			List<Lecture> lectures = new ArrayList<Lecture>();
			List<User> users = subject.getProfessors();
			for(User user : users){
				Lecture lecture = new Lecture();
				lecture.setTeacher(user);
				lecture.setSubject(subject.getId());
				lectures.add(lecture);
			}
			LectureListAdapter searchAdapter = new LectureListAdapter(this, lectures);
			professorsListView.setAdapter(searchAdapter);
		}
	}
	
	@Override
	public void onBackPressed() {
	   super.onBackPressed();
	   this.finish();
    }
}
