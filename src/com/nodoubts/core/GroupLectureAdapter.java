package com.nodoubts.core;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nodoubts.R;

public class GroupLectureAdapter<T extends SearchType> extends ArrayAdapter<T>{

	private Context context;
	private GroupLecture searchObj;
	
	public GroupLectureAdapter(Context context, List<T> groups) {
		super(context, 0, groups);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		searchObj = (GroupLecture) getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_group_lectures, parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.name_group_lecture_list);
		tv.setText(searchObj.getName());
		
		TextView tvDate = (TextView) convertView.findViewById(R.id.date_group_lecture_list);
		if (searchObj.getDate()!=null) {
			tvDate.setText(searchObj.getDate().toString());
		}
		return convertView;
	}
}
