package com.nodoubts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nodoubts.core.CommentAdapter;
import com.nodoubts.core.Lecture;
import com.nodoubts.core.Rating;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;

public class ViewProfessorActivity extends Activity {

	private Lecture lecture;
	private List<String> ratingsIds;
	private float score;
	List<String> comments;
	ListView commentsListView;
	CommentAdapter myAdapter;
	public List<Rating> ratingObjs;
	User student;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_view_professor);

		lecture = (Lecture) getIntent().getExtras().get("searchObj");
		student = (User) getIntent().getExtras().get("student");
		comments = new ArrayList<String>();

		TextView nameTextView = (TextView) findViewById(R.id.view_professor_name);
		TextView emailTextView = (TextView) findViewById(R.id.view_professor_contact);
		Button scheduleBtn = (Button) findViewById(R.id.view_professor_schedule_lecture);
		Button curriculumBtn = (Button) findViewById(R.id.view_curriculum_btn);
		commentsListView = (ListView) findViewById(R.id.professor_comments_listview);
		Button rate = (Button) findViewById(R.id.rate_btn_viewProfAct);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar1);

		if (lecture != null) {
			score = lecture.getTeacher().getScore();
			ratingsIds = lecture.getTeacher().getRatings();
			nameTextView.setText(lecture.getTeacher().getName());
			emailTextView.setText(lecture.getTeacher().getEmail());
			ratingBar.setRating(score);
			new RequestRatingTask().execute(ratingsIds);

			scheduleBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * Intent scheduleLecureIntent = new Intent(
					 * getApplicationContext(), ScheduleLectureActivity.class);
					 * scheduleLecureIntent.putExtra("lecture", lecture);
					 * getApplicationContext
					 * ().startActivity(scheduleLecureIntent);
					 */
					Toast.makeText(getApplicationContext(),
							"You're still not able to schedule a lecture!",
							Toast.LENGTH_SHORT).show();
				}
			});

		}

		rate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (student != null) {
				Intent ratingActivity = new Intent(ViewProfessorActivity.this,
						RatingActivity.class);
				// ratingActivity.putExtra("user", student);
				ratingActivity.putExtra("lecture", lecture);
				startActivityForResult(ratingActivity,1);
				// }else{
				// Toast.makeText(ViewProfessorActivity.this,
				// "You're not able to rate this user",Toast.LENGTH_SHORT
				// ).show();
				// }
			}
		});
	}


	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 
             if(resultCode == RESULT_OK){  
            	 Toast.makeText(ViewProfessorActivity.this, "Rated Succesfully", Toast.LENGTH_SHORT).show();
             }
 
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	private class RequestRatingTask extends
			AsyncTask<List<String>, Void, List<Rating>> {
		

		@Override
		protected List<Rating> doInBackground(List<String>... params) {
			ratingObjs = new ArrayList<Rating>();
			List<String> ratingIDs= params[0];
			UserService userController = new UserController();
			for (String id : ratingIDs) {
				try {
					Rating r = userController.getRating(id);
					ratingObjs.add(r);
				} catch (ApplicationViewException e) {
					e.printStackTrace();
				}
			}
			return ratingObjs;
		}

		@Override
		protected void onPostExecute(List<Rating> result) {
			myAdapter = new CommentAdapter(ViewProfessorActivity.this,ratingObjs);
			commentsListView.setAdapter(myAdapter);
		}
	}

}
