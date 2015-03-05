package com.nodoubts.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.nodoubts.R;
import com.nodoubts.ViewScheduledLectureActivity;
import com.nodoubts.core.ScheduledLecture;
import com.nodoubts.core.SearchAdapter;
import com.nodoubts.core.SearchType;
import com.nodoubts.core.User;
import com.nodoubts.ui.profile.UserProfile;

public class LecturesRegisteredFragment extends Fragment {

	List<SearchType> lecturesRegistered = new ArrayList<SearchType>();
	ListView listRegistered;
	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_lectures_registered_tab, container, false);
		listRegistered = (ListView) rootView
				.findViewById(R.id.lectures_registered_lv);
		listRegistered.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchType lecture = lecturesRegistered.get(position);
				Intent intent = new Intent(context, lecture.getActivityClass());
				if (lecture instanceof ScheduledLecture) {
					intent.putExtra(ViewScheduledLectureActivity.SCHEDULED_LECTURE_SELECTED, lecture);
				} else {
					intent.putExtra("groupLectureSelected", lecture);
				}
				startActivity(intent);
			}
		});
		context = rootView.getContext();
		//TODO: badsmell remove static access to user
		User user = UserProfile.user;
		SearchAdapter<SearchType> lecturesAdapter = new SearchAdapter<SearchType>(
				context, user.getLecturesRegisteredByUser());
		listRegistered.setAdapter(lecturesAdapter);
		return rootView;
	}
}
