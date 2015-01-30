package com.nodoubts;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nodoubts.core.User;

public class ViewProfessorActivity extends Activity {
	
	private User professor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	   //set content view AFTER ABOVE sequence (to avoid crash)
	    this.setContentView(R.layout.activity_view_professor);
	    
	    professor = (User) getIntent().getExtras().get("searchObj");
		TextView nameTextView = (TextView) findViewById(R.id.view_professor_name);
		TextView emailTextView = (TextView) findViewById(R.id.view_professor_contact);
		Button scheduleBtn = (Button) findViewById(R.id.view_professor_schedule_lecture);
		Button curriculumBtn = (Button) findViewById(R.id.view_curriculum_btn);
		ListView commentsListView = (ListView) findViewById(R.id.professor_comments_listview);
		
		if(professor!=null){
			nameTextView.setText(professor.getName());
			emailTextView.setText(professor.getEmail());
			scheduleBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO: Call schedule lecture
				}
			});
		}
	}
}
