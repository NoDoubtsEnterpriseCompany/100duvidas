package com.nodoubts.ui.subject;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.nodoubts.R;
import com.nodoubts.R.id;
import com.nodoubts.R.layout;
import com.nodoubts.core.GroupLecture;
import com.nodoubts.core.GroupLectureAdapter;
import com.nodoubts.core.Lecture;
import com.nodoubts.core.Subject;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.grouplecture.GroupLectureController;
import com.nodoubts.serverclient.grouplecture.GroupLectureService;
import com.nodoubts.serverclient.lecture.LectureController;
import com.nodoubts.serverclient.lecture.LectureService;
import com.nodoubts.ui.grouplecture.GroupLectureActivity;
import com.nodoubts.ui.lecture.LectureListAdapter;
import com.nodoubts.ui.user.ViewProfessorActivity;

public class SubjectActivity extends Activity {

	private Subject subject;
	ListView professorsListView;
	GroupLectureAdapter<GroupLecture> groupLectureAdapter;
	List<Lecture> lectures;
	List<GroupLecture> groupLectures;
	LectureListAdapter searchAdapter;
	static final int INSCRIBE_USER_ON_GROUP_CODE = 1213;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.subject_activity);

		subject = (Subject) getIntent().getExtras().get("searchObj");
		TextView subjectNameTextView = (TextView) findViewById(R.id.subject_name_textview);
		TextView subjectDescriptionTextView = (TextView) findViewById(R.id.subject_description_textview);
		professorsListView = (ListView) findViewById(R.id.subject_teachersListView);

		if (subject != null) {
			subjectDescriptionTextView.setText(subject.getDescription());
			subjectNameTextView.setText(subject.getName());
			RequestLecturesTask getLectures = new RequestLecturesTask();
			getLectures.execute(subject);
			new RequestGroupLecturesTask().execute(subject);
		}
		
		professorsListView.setAdapter(searchAdapter);
    	professorsListView.setOnItemClickListener(new OnItemClickListener() {
    		
    		@Override
    		public void onItemClick(AdapterView<?> parent,
    				View view, int position, long id) {
    			Intent searchScreen = new Intent(SubjectActivity.this, ViewProfessorActivity.class);
				searchScreen.putExtra("searchObj", lectures.get(position));
				startActivity(searchScreen);
    		}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.teachers_subject_radio:
	            if (checked)
	                professorsListView.setAdapter(searchAdapter);
	            	professorsListView.setOnItemClickListener(new OnItemClickListener() {
	            		
	            		@Override
	            		public void onItemClick(AdapterView<?> parent,
	            				View view, int position, long id) {
	            			Intent searchScreen = new Intent(SubjectActivity.this, ViewProfessorActivity.class);
	        				searchScreen.putExtra("searchObj", lectures.get(position));
	        				startActivity(searchScreen);
	            		}
					});
	            break;
	        case R.id.group_subject_radio:
	            if (checked)
	                professorsListView.setAdapter(groupLectureAdapter);
	            	professorsListView.setOnItemClickListener(new OnItemClickListener() {
	            		
	            		@Override
	                    public void onItemClick(AdapterView<?> parent, View view, int position,
	                            long id) {
	                        Intent intent = new Intent(SubjectActivity.this, GroupLectureActivity.class);
	                        intent.putExtra("groupLectureSelected", groupLectures.get(position));
	                        startActivityForResult(intent, INSCRIBE_USER_ON_GROUP_CODE);
	                    }
					});
	            break;
	    
	    default:
	    	professorsListView.setAdapter(searchAdapter);
          	professorsListView.setOnItemClickListener(new OnItemClickListener() {
          		
          		@Override
          		public void onItemClick(AdapterView<?> parent,
          				View view, int position, long id) {
          			Intent searchScreen = new Intent(SubjectActivity.this, ViewProfessorActivity.class);
      				searchScreen.putExtra("searchObj", lectures.get(position));
      				startActivity(searchScreen);
          		}
				});
	    	
	    	break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (INSCRIBE_USER_ON_GROUP_CODE==requestCode) {
			if (resultCode==Activity.RESULT_OK) {
				String groupId = data.getStringExtra("groupId");
				String userId = data.getStringExtra("userId");
				for (int i=0; i<groupLectures.size(); i++) {
					if (groupLectures.get(i).get_id().equals(groupId)) {
						groupLectures.get(i).getStudentsRegistered().add(userId);
						break;
					}
				}
			}
		}
	}

	private class RequestLecturesTask extends
			AsyncTask<Subject, Void, List<Lecture>> {

		@Override
		protected List<Lecture> doInBackground(Subject... params) {
			Subject subject = params[0];
			LectureService lectureService = new LectureController();
			lectures = lectureService.getSubjectLectures(subject.get_id());
			return lectures;
		}

		@Override
		protected void onPostExecute(List<Lecture> result) {
			searchAdapter = new LectureListAdapter(SubjectActivity.this, result);
			professorsListView.setAdapter(searchAdapter);
		}
	}

	private class RequestGroupLecturesTask extends
			AsyncTask<Subject, Void, List<GroupLecture>> {

		@Override
		protected List<GroupLecture> doInBackground(Subject... params) {
			Subject subject = params[0];
			GroupLectureService groupLectureService = new GroupLectureController();
			groupLectures = groupLectureService
					.getGroupLecturesBySubject(subject.get_id());
			return groupLectures;
		}

		@Override
		protected void onPostExecute(List<GroupLecture> result) {
			groupLectureAdapter = new GroupLectureAdapter<GroupLecture>(
					SubjectActivity.this, result);
		}
	}
}