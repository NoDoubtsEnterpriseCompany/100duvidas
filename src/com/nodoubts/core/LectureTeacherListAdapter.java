package com.nodoubts.core;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nodoubts.R;

public class LectureTeacherListAdapter extends SearchAdapter<Lecture> {
	
	
	public LectureTeacherListAdapter(Context context, List<Lecture> lectures) {
		super(context, lectures);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		searchObj = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.lecture_teacher_list_adapter, parent, false);
		}
		TextView lectureName = (TextView) convertView.findViewById(R.id.lecture_name_ltAdapter);
		TextView teacherName = (TextView) convertView.findViewById(R.id.teacher_name_ltAdapter);
		lectureName.setText(searchObj.getName());
		teacherName.setText(searchObj.getTeacher().getName());
		
		lectureName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent searchScreen = new Intent(context, searchObj.getActivityClass());
				searchScreen.putExtra("searchObj", searchObj);
				context.startActivity(searchScreen);
			}
		});
		return convertView;
	}
}
