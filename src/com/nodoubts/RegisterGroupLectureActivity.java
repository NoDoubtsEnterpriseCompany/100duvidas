package com.nodoubts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.nodoubts.core.GroupLecture;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.grouplecture.GroupLectureController;
import com.nodoubts.serverclient.grouplecture.GroupLectureService;

public class RegisterGroupLectureActivity extends Activity {

	User user;
	Calendar myCalendar;
	DatePickerDialog.OnDateSetListener date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_group_lecture);
		if (getIntent().getSerializableExtra("user") != null) {
			user = (User) getIntent().getSerializableExtra("user");
		}
		myCalendar = Calendar.getInstance();

		date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel();
			}

		};

		Button button = (Button) findViewById(R.id.register_group_bt);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String nome = ((EditText) findViewById(R.id.name_group_lecture_ed))
						.getText().toString();
				String address = ((EditText) findViewById(R.id.address_group_lecture_ed))
						.getText().toString();
				int num = Integer
						.parseInt(((EditText) findViewById(R.id.num_group_lecture_ed))
								.getText().toString());
				float price = Float.parseFloat(((EditText) findViewById(R.id.price_group_lecture_ed))
								.getText().toString());
				GroupLecture groupLecture = new GroupLecture();
				groupLecture.setNumMaxStudents(num);
				groupLecture.setDate(myCalendar.getTime());
				groupLecture.setName(nome);
				groupLecture.setProfessor(user);
				groupLecture.setPrice(price);
				groupLecture.setAddress(address);
				new RegisterNewGroupLecture().execute(groupLecture);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_group_lecture, menu);
		return true;
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

	private void updateLabel() {

		String myFormat = "MM/dd/yy"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	}

	protected class RegisterNewGroupLecture extends
			AsyncTask<GroupLecture, Void, Object> {

		ProgressDialog progressDialog;
		GroupLectureService groupLectureService;
		GroupLecture groupLecture;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.groupLectureService = new GroupLectureController();
			this.progressDialog = new ProgressDialog(
					RegisterGroupLectureActivity.this);
			this.progressDialog
					.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}

		@Override
		protected Object doInBackground(GroupLecture... params) {
			try {
				this.groupLecture = params[0];
				return groupLectureService.saveGroupLecture(this.groupLecture);
			} catch (ApplicationViewException e) {
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result instanceof String) {
				Toast.makeText(
						RegisterGroupLectureActivity.this,
						getResources()
								.getString(R.string.subject_registered_ok),
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RegisterGroupLectureActivity.this,
						ProfessorProfileActivity.class);
				intent.putExtra("user", user);
				startActivity(intent);
			} else if (result instanceof Exception) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						RegisterGroupLectureActivity.this);
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
