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

public class SearchAdapter<T extends SearchType> extends ArrayAdapter<T>{

	protected Context context;
	protected T searchObj;
	
	public SearchAdapter(Context context, List<T> searchObjects) {
		super(context, 0, searchObjects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		searchObj = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.search_adapter, parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.search_obj_name);
		tv.setText(searchObj.getName());
		return convertView;
	}
}
