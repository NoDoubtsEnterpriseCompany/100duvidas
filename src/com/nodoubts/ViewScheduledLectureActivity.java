package com.nodoubts;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.nodoubts.core.ScheduledLecture;

public class ViewScheduledLectureActivity extends Activity {

	public static final String SCHEDULED_LECTURE_SELECTED = "scheduledLectureSelected";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_scheduled_lecture);
		
		ScheduledLecture scheduledLecture = (ScheduledLecture) getIntent().getSerializableExtra(SCHEDULED_LECTURE_SELECTED);
		
		TextView price = (TextView) findViewById(R.id.price_scheduled_lecture_tv);
		TextView address = (TextView) findViewById(R.id.address_scheduled_lecture_tv);
		TextView professor = (TextView) findViewById(R.id.teacher_scheduled_lecture_tv);
		TextView date = (TextView) findViewById(R.id.date_scheduled_lecture_tv);
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
		
		price.setText(String.valueOf(scheduledLecture.getPrice()));
		address.setText(scheduledLecture.getAddress());
		professor.setText(scheduledLecture.getTeacher().getProfile().getName());
		date.setText(spf.format(scheduledLecture.getDate()));
	}

}
