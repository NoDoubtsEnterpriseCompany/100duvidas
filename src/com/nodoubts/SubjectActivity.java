package com.nodoubts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.nodoubts.core.SearchAdapter;
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
		ListView ProfessorsListView = (ListView) findViewById(R.id.subject_teachersListView);
		
		if(subject!=null){
			subjectNameTextView.setText(subject.getName());
			subjectDescriptionTextView.setText(subject.getDescription());
			SearchAdapter<User> searchAdapter = new SearchAdapter<User>(this, subject.getProfessors());
			ProfessorsListView.setAdapter(searchAdapter);
		}
	}
}
