package com.nodoubts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nodoubts.core.Lecture;
import com.nodoubts.core.Rating;
import com.nodoubts.core.User;

public class ViewProfessorActivity extends Activity {

	private Lecture lecture;
	private List<String> ratings;
	private List<String> comments;
	private float score;
	ListView commentsListView;
	ListAdapter myAdapter;
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
			ratings = lecture.getTeacher().getRatings();
			nameTextView.setText(lecture.getTeacher().getName());
			emailTextView.setText(lecture.getTeacher().getEmail());
			fillRatingAdapter(comments);
			ratingBar.setRating(score);

			scheduleBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent scheduleLecureIntent = new Intent(
							ViewProfessorActivity.this,
							ScheduleLectureActivity.class);
					scheduleLecureIntent.putExtra("lecture", lecture);
					startActivity(scheduleLecureIntent);
				}
			});
					
		}
		

		rate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//if (student != null) {
					Intent ratingActivity = new Intent(
							ViewProfessorActivity.this, RatingActivity.class);
					//ratingActivity.putExtra("user", student);
					ratingActivity.putExtra("lecture", lecture);
					startActivity(ratingActivity);
				//}else{
					//Toast.makeText(ViewProfessorActivity.this, "You're not able to rate this user",Toast.LENGTH_SHORT ).show();
				//}
			}
		});
	}

	private void fillCommentList() {
		List<Rating> ratings = new ArrayList<Rating>();
		// TODO fazer um join pra pegar a lista de rating a partir das IDs
		for (Rating rating : ratings) {
			comments.add(rating.getComment());
		}
	}

	private void fillRatingAdapter(List<String> comments) {
		fillCommentList();
		myAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, comments);
		commentsListView.setAdapter(myAdapter);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
}
