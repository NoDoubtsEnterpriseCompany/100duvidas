package com.nodoubts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nodoubts.core.GroupLecture;
import com.nodoubts.core.Lecture;
import com.nodoubts.core.ScheduledLecture;
import com.nodoubts.core.SearchAdapter;
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
	Map<String, List<SearchType>> eventos = new HashMap<String, List<SearchType>>();
	static final int CUSTOM_DIALOG_ID = 10;
	Date selectedDate;
	SimpleDateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");
	ListView lvDialog;

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
				selectedDate = date;
				showDialog(CUSTOM_DIALOG_ID);
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
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
			switch (id) {
			case CUSTOM_DIALOG_ID:
				dialog = new Dialog(CalendarActivity.this);
				dialog.setContentView(R.layout.lectures_dialog);
				dialog.setTitle(dateOnly.format(selectedDate));
				lvDialog = (ListView) dialog.findViewById(R.id.dialoglist);
				if (eventos.containsKey(dateOnly.format(selectedDate))) {
					SearchAdapter<SearchType> adapter = new SearchAdapter<SearchType>(dialog.getContext(), eventos.get(dateOnly.format(selectedDate)));
					lvDialog.setAdapter(adapter);
				}
				TextView textView = (TextView) dialog.findViewById(R.id.dialogtext);
				if (lvDialog.getAdapter()!=null && lvDialog.getAdapter().getCount()>0) {
					textView.setVisibility(View.INVISIBLE);
				} else {
					textView.setVisibility(View.VISIBLE);
				}
				lvDialog.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						SearchType searchType = (SearchType) lvDialog.getItemAtPosition(position);
						Intent intent = new Intent(CalendarActivity.this, searchType.getActivityClass());
						if (searchType instanceof ScheduledLecture) {
							intent.putExtra(ViewScheduledLectureActivity.SCHEDULED_LECTURE_SELECTED, searchType);
						} else {
							intent.putExtra("groupLectureSelected", searchType);
						}
						startActivity(intent);
					}
					
				});
				selectedDate = null;
				break;

			default:
				break;
			}

		return dialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if (selectedDate != null) {
			dialog.setTitle(dateOnly.format(selectedDate));
			TextView textView = (TextView) dialog.findViewById(R.id.dialogtext);
			if (eventos.containsKey(dateOnly.format(selectedDate))) {
				SearchAdapter<SearchType> adapter = new SearchAdapter<SearchType>(dialog.getContext(), eventos.get(dateOnly.format(selectedDate)));
				lvDialog.setAdapter(adapter);
			} else {
				SearchAdapter<SearchType> adapter = new SearchAdapter<SearchType>(dialog.getContext(), new ArrayList<SearchType>());
				lvDialog.setAdapter(adapter);
			}
			System.out.println(lvDialog.getAdapter()!=null && lvDialog.getAdapter().getCount()==0);
			if (lvDialog.getAdapter()!=null && lvDialog.getAdapter().getCount()>0) {
				textView.setVisibility(View.INVISIBLE);
			} else {
				textView.setVisibility(View.VISIBLE);
			}
			selectedDate = null;
		}
		super.onPrepareDialog(id, dialog);
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
				String date;
				if (event instanceof GroupLecture) {
					GroupLecture groupLecture = (GroupLecture) event;
					caldroidFragment.setSelectedDates(groupLecture.getDate(), groupLecture.getDate());
					caldroidFragment.setBackgroundResourceForDate(R.color.blue, groupLecture.getDate());
					date = dateOnly.format(groupLecture.getDate());
					if (!eventos.containsKey(date)) {
						eventos.put(date, new ArrayList<SearchType>());
					}
					eventos.get(date).add(groupLecture);
				} else {
					Lecture lecture = (Lecture) event;
					caldroidFragment.setSelectedDates(lecture.getDate(), lecture.getDate());
					caldroidFragment.setBackgroundResourceForDate(R.color.blue, lecture.getDate());
					date = dateOnly.format(lecture.getDate());
					if (!eventos.containsKey(date)) {
						eventos.put(date, new ArrayList<SearchType>());
					}
					eventos.get(date).add(lecture);
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
				String date;
				if (event instanceof GroupLecture) {
					GroupLecture groupLecture = (GroupLecture) event;
					caldroidFragment.setSelectedDates(groupLecture.getDate(), groupLecture.getDate());
					caldroidFragment.setBackgroundResourceForDate(R.color.blue, groupLecture.getDate());
					date = dateOnly.format(groupLecture.getDate());
					if (!eventos.containsKey(date)) {
						eventos.put(date, new ArrayList<SearchType>());
					}
					eventos.get(date).add(groupLecture);
				} else {
					Lecture lecture = (Lecture) event;
					caldroidFragment.setSelectedDates(lecture.getDate(), lecture.getDate());
					caldroidFragment.setBackgroundResourceForDate(R.color.blue, lecture.getDate());
					date = dateOnly.format(lecture.getDate());
					if (!eventos.containsKey(date)) {
						eventos.put(date, new ArrayList<SearchType>());
					}
					eventos.get(date).add(lecture);
				}
			}
			caldroidFragment.refreshView();
		}
	}

}
