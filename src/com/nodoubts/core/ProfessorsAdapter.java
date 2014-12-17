package com.nodoubts.core;

import java.util.List;

import com.nodoubts.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProfessorsAdapter extends ArrayAdapter<User> {

	public ProfessorsAdapter(Context context, List<User> users) {
		super(context, 0, users);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User user = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.adapter_professors, parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.name_professor_list);
		tv.setText(user.getUsername());
		return convertView;
	}
}
