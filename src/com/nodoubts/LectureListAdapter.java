package com.nodoubts;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
		RatingBar professor_rating = (RatingBar) convertView.findViewById(R.id.professor_rating);
		professor_rating.setRating(3);
		
		tv.setOnClickListener(new OnClickListener() {
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
