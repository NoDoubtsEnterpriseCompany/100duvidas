package com.nodoubts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nodoubts.core.GroupLecture;
import com.nodoubts.core.Lecture;
import com.nodoubts.core.SearchType;
import com.nodoubts.core.User;
import com.nodoubts.serverclient.grouplecture.GroupLectureController;
import com.nodoubts.serverclient.grouplecture.GroupLectureService;
import com.nodoubts.serverclient.lecture.LectureController;
import com.nodoubts.serverclient.lecture.LectureService;
import com.nodoubts.ui.profile.UserProfile;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class CalendarActivity extends FragmentActivity {

	Context context;
	CaldroidFragment caldroidFragment;
	HashMap<String, Object> caldroidData = new HashMap<String, Object>();
	List<SearchType> lecturesCreated = new ArrayList<SearchType>();
	List<SearchType> lecturesRegistered = new ArrayList<SearchType>();
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		context = this;
		user = UserProfile.user;
		caldroidFragment = new CaldroidFragment();
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		caldroidFragment.setArguments(args);

		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				// Do something
			}

			@Override
			public void onCaldroidViewCreated() {
				// Supply your own adapter to weekdayGridView (SUN, MON, etc)

				Button leftButton = caldroidFragment.getLeftArrowButton();
				Button rightButton = caldroidFragment.getRightArrowButton();
				TextView textView = caldroidFragment.getMonthTitleTextView();

				// Do customization here
			}

		};

		caldroidFragment.setCaldroidListener(listener);

		android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();
		new RequestGroupLecturesTask().execute(user);
		new RequestLecturesRegisteredTask().execute(user);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.calendar, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_list_lectures:
	            Intent intent = new Intent(CalendarActivity.this, UserLecturesTabsActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
			UserProfile.user.setLecturesCreatedByUser(result);
			for (SearchType event: result) {
				if (event instanceof GroupLecture) {
					GroupLecture groupLecture = (GroupLecture) event;
					caldroidFragment.setSelectedDates(groupLecture.getDate(), groupLecture.getDate());
					caldroidFragment.setBackgroundResourceForDate(R.color.blue, groupLecture.getDate());
				} else {
					Lecture lecture = (Lecture) event;
					caldroidFragment.setSelectedDates(lecture.getDate(), lecture.getDate());
					caldroidFragment.setBackgroundResourceForDate(R.color.blue, lecture.getDate());
				}
			}
			caldroidFragment.refreshView();
		}
	}

	private class RequestLecturesRegisteredTask extends
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
			lecturesRegistered.addAll(lectureService
					.getScheduledLecturesFromStudent(user.get_id()));
			lecturesRegistered.addAll(groupLectureService
					.getGroupLecturesOfStudent(user.get_id()));
			return lecturesRegistered;
		}

		@Override
		protected void onPostExecute(List<SearchType> result) {
			dialog.dismiss();
			UserProfile.user.setLecturesRegisteredByUser(result);
			for (SearchType event: result) {
				if (event instanceof GroupLecture) {
					GroupLecture groupLecture = (GroupLecture) event;
					caldroidFragment.setSelectedDates(groupLecture.getDate(), groupLecture.getDate());
					caldroidFragment.setBackgroundResourceForDate(R.color.green, groupLecture.getDate());
				} else {
					Lecture lecture = (Lecture) event;
					caldroidFragment.setSelectedDates(lecture.getDate(), lecture.getDate());
					caldroidFragment.setBackgroundResourceForDate(R.color.green, lecture.getDate());
				}
			}
			caldroidFragment.refreshView();
		}
	}

}
