package com.nodoubts.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.nodoubts.R;
import com.nodoubts.ViewScheduledLectureActivity;
import com.nodoubts.core.ScheduledLecture;
import com.nodoubts.core.SearchAdapter;
import com.nodoubts.core.SearchType;
import com.nodoubts.ui.grouplecture.RegisterGroupLectureActivity;
import com.nodoubts.ui.profile.UserProfile;

public class LecturesOferredFragment extends Fragment {

	ListView listOferred;
	Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_lectures_created_tab, container, false);
		listOferred = (ListView) rootView.findViewById(R.id.lectures_oferred_lv);
		listOferred.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchType lecture = UserProfile.user.getLecturesCreatedByUser().get(position);
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
		//TODO: badsmell- remove static access to user 
		SearchAdapter<SearchType> lecturesAdapter = new SearchAdapter<SearchType>(context, UserProfile.user.getLecturesCreatedByUser());
		listOferred.setAdapter(lecturesAdapter);
		return rootView;
	}
}
