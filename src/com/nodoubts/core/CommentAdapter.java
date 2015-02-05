package com.nodoubts.core;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nodoubts.R;

public class CommentAdapter extends SearchAdapter<Rating>  {


	private Context context;
	private Rating searchObj;

	public CommentAdapter (Context context, List<Rating> ratings) {
		super(context,ratings);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		searchObj = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comment_adapter, parent, false);
		}
		TextView comment = (TextView) convertView
				.findViewById(R.id.search_obj_name_commentAdp);
		comment.setText(searchObj.getComment());
		return convertView;
	}
}
