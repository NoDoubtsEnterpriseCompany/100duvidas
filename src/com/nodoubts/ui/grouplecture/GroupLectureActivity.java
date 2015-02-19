package com.nodoubts.ui.grouplecture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nodoubts.R;
import com.nodoubts.core.GroupLecture;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.grouplecture.GroupLectureController;
import com.nodoubts.serverclient.grouplecture.GroupLectureService;
import com.nodoubts.ui.profile.UserProfile;

public class GroupLectureActivity extends Activity {

	GroupLecture groupLecture;
	User user;
	GroupLectureService groupLectureService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_lecture);
		groupLectureService = new GroupLectureController();
		this.groupLecture = (GroupLecture)getIntent().getSerializableExtra("groupLectureSelected");
		//this.user = (User) getIntent().getSerializableExtra("user");
		//TODO: badsmell remove static access to user
		this.user = UserProfile.user;

		TextView name = (TextView) findViewById(R.id.name_group_lecture_tv);
		TextView price = (TextView) findViewById(R.id.price_group_lecture_tv);
		TextView address = (TextView) findViewById(R.id.address_group_lecture_tv);
		TextView professor = (TextView) findViewById(R.id.teacher_group_lecture_tv);
		TextView numSubscribers = (TextView) findViewById(R.id.number_subscribers_group_lecture_tv);

		name.setText(this.groupLecture.getName());
		price.setText(String.valueOf(this.groupLecture.getPrice()));
		address.setText(groupLecture.getAddress());
		professor.setText(groupLecture.getProfessor().getProfile().getName());
		numSubscribers.setText(String.valueOf(groupLecture.getStudentsRegistered().size()));
		

		Button participateBtn = (Button) findViewById(R.id.participate_group_lecture);
		if (groupLecture.getProfessor().getUsername().equals(user.getUsername())
				|| groupLecture.getStudentsRegistered().contains(user.get_id())) {
			participateBtn.setVisibility(View.INVISIBLE);
		}

		participateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new InscribeOnGroupLecture().execute(groupLecture);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	protected class InscribeOnGroupLecture extends
	AsyncTask<GroupLecture, Void, Object> {

		ProgressDialog progressDialog;
		GroupLectureService groupLectureService;
		GroupLecture groupLectureToSave;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.groupLectureService = new GroupLectureController();
			this.progressDialog = new ProgressDialog(
					GroupLectureActivity.this);
			this.progressDialog
			.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}

		@Override
		protected Object doInBackground(GroupLecture... params) {
			try {
				this.groupLectureToSave = params[0];
				return groupLectureService.registerStudent(this.groupLectureToSave.get_id(), user);
			} catch (ApplicationViewException e) {
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result instanceof String) {
				Toast.makeText(
						GroupLectureActivity.this,
						getResources()
						.getString(R.string.group_lecture_registered_ok),
						Toast.LENGTH_LONG).show();
				Intent resultIntent = new Intent();
				resultIntent.putExtra("userId", user.get_id());
				resultIntent.putExtra("groupId", groupLecture.get_id());
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			} else if (result instanceof Exception) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GroupLectureActivity.this);
				if (((Exception) result).getMessage().contains("11000")) {
					builder.setMessage(R.string.existing_subject);
				} else {
					builder.setMessage(((Exception) result).getMessage());
				}
				builder.setTitle("Error");
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}
}

