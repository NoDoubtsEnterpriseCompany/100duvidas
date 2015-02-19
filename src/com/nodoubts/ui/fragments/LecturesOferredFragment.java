package com.nodoubts.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.nodoubts.core.User;
import com.nodoubts.serverclient.grouplecture.GroupLectureController;
import com.nodoubts.serverclient.grouplecture.GroupLectureService;
import com.nodoubts.serverclient.lecture.LectureController;
import com.nodoubts.serverclient.lecture.LectureService;
import com.nodoubts.ui.profile.UserProfile;
import com.nodoubts.ui.rating.RegisterGroupLectureActivity;

public class LecturesOferredFragment extends Fragment {

	List<SearchType> lecturesCreated = new ArrayList<SearchType>();
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
				SearchType lecture = lecturesCreated.get(position);
				Intent intent = new Intent(context, lecture.getActivityClass());
				if (lecture instanceof ScheduledLecture) {
					intent.putExtra(ViewScheduledLectureActivity.SCHEDULED_LECTURE_SELECTED, lecture);
				} else {
					intent.putExtra("groupLectureSelected", lecture);
				}
				startActivity(intent);
			}
		});
		Button btnNewGroup = (Button) rootView.findViewById(R.id.create_new_group_lecture_btn);
		btnNewGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, RegisterGroupLectureActivity.class);
				intent.putExtra("user", UserProfile.user);
				//TODO: badsmell- remove static access to user 
				startActivity(intent);
			}
		});
		context = rootView.getContext();
		//TODO: badsmell- remove static access to user 
		new RequestGroupLecturesTask().execute(UserProfile.user);
		return rootView;
	}

	private class RequestGroupLecturesTask extends
			AsyncTask<User, Void, List<SearchType>> {

		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			this.dialog.setProgressStyle(dialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.dialog.show();
		}
		
		@Override
		protected List<SearchType> doInBackground(User... params) {
			User user = params[0];
			GroupLectureService groupLectureService = new GroupLectureController();
			LectureService lectureService = new LectureController();
			lecturesCreated.addAll(lectureService.getScheduledLecturesFromTeacher(user.get_id()));
			lecturesCreated.addAll(groupLectureService.getGroupLecturesByUser(user.getUsername()));
			return lecturesCreated;
		}
		
		@Override
		protected void onPostExecute(List<SearchType> result) {
			dialog.dismiss();
			SearchAdapter<SearchType> lecturesAdapter = new SearchAdapter<SearchType>(context, result);
			listOferred.setAdapter(lecturesAdapter);
		}
	}
}
