package com.nodoubts;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nodoubts.core.Lecture;
import com.nodoubts.core.SearchAdapter;

public class LectureListAdapter extends SearchAdapter<Lecture> {

	public LectureListAdapter(Context context, List<Lecture> lectures) {
		super(context, lectures);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		searchObj = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.professors_list_adapter, parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.search_obj_name);
		tv.setText(searchObj.getName());
		RatingBar professor_rating = (RatingBar) convertView
				.findViewById(R.id.professor_rating);
		professor_rating.setRating(searchObj.getTeacher().getScore());
		return convertView;
	}
}
