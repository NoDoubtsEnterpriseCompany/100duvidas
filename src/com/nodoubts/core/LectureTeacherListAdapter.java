package com.nodoubts.core;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nodoubts.R;

public class LectureTeacherListAdapter extends SearchAdapter<Lecture>  {

	private Context context;
	private Lecture searchObj;

	public LectureTeacherListAdapter(Context context, List<Lecture> lectures) {
		super(context,lectures);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		searchObj = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.lecture_teacher_list_adapter, parent, false);
		}
		TextView lectureName = (TextView) convertView
				.findViewById(R.id.lecture_name_ltAdapter);
		TextView teacherName = (TextView) convertView
				.findViewById(R.id.teacher_name_ltAdapter);
		lectureName.setText(searchObj.getName());
		teacherName.setText(searchObj.getTeacher().getName());
		return convertView;
	}
}
