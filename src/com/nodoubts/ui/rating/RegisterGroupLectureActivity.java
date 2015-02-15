package com.nodoubts.ui.rating;

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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nodoubts.R;
import com.nodoubts.R.id;
import com.nodoubts.R.layout;
import com.nodoubts.R.string;
import com.nodoubts.core.GroupLecture;
import com.nodoubts.core.Subject;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.grouplecture.GroupLectureController;
import com.nodoubts.serverclient.grouplecture.GroupLectureService;
import com.nodoubts.ui.profile.ProfessorProfileActivity;
import com.nodoubts.ui.subject.SubjectListActivity;

public class RegisterGroupLectureActivity extends Activity {

	User user;
	Calendar myCalendar;
	DatePickerDialog.OnDateSetListener date;
	EditText subject;
	String subjectId;
	public static final int RESULT_CODE_SUBJECT = 1212; 
	public static final String SUBJECT_GROUP_IDENTIFIER = "subjectGroup";

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
		
		subject = (EditText) findViewById(R.id.subject_group_lecture_ed);
		
		subject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(RegisterGroupLectureActivity.this, SubjectListActivity.class); 
				i.putExtra("user", user);
				i.putExtra("isGroupLectureCreation", true);
				startActivityForResult(i, RegisterGroupLectureActivity.RESULT_CODE_SUBJECT);
			}
		});

		Button button = (Button) findViewById(R.id.register_group_bt);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String nome = ((EditText) findViewById(R.id.name_group_lecture_ed))
						.getText().toString();
				String address = ((EditText) findViewById(R.id.address_group_lecture_ed))
						.getText().toString();
				String description = ((EditText) findViewById(R.id.description_group_lecture_ed))
						.getText().toString();
				String numValue = ((EditText) findViewById(R.id.num_group_lecture_ed))
						.getText().toString();
				int num;
				if (numValue.trim().equals("")) {
					num = -1;
				} else {
					num = Integer.parseInt(numValue);
				}
				String priceValue = ((EditText) findViewById(R.id.price_group_lecture_ed))
						.getText().toString();
				float price;
				if (priceValue.trim().equals("")) {
					price = -1;
				} else {
					price = Float.parseFloat(priceValue);
				}
				TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker_group_lecture_ed);
				myCalendar.set(Calendar.HOUR, timePicker.getCurrentHour());
				myCalendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
				GroupLecture groupLecture = new GroupLecture();
				groupLecture.setNumMaxStudents(num);
				groupLecture.setDate(myCalendar.getTime().toString());
				groupLecture.setName(nome);
				groupLecture.setProfessor(user);
				groupLecture.setPrice(price);
				groupLecture.setAddress(address);
				groupLecture.setSubject(subjectId);
				groupLecture.setDescription(description);
				if (isGroupLectureValid(groupLecture)) {
					new RegisterNewGroupLecture().execute(groupLecture);
				}
			}

			private boolean isGroupLectureValid(GroupLecture groupLecture) {
				if (isVazio(groupLecture.getAddress())) {
					showToast(getResources().getString(R.string.addressEmpty));
					return false;
				} else if (isVazio(groupLecture.getName())) {
					showToast(getResources().getString(R.string.nameEmpty));
					return false;
				} else if (isVazio(groupLecture.getSubject())) {
					showToast(getResources().getString(R.string.subjectEmpty));
					return false;
				} else if (groupLecture.getPrice()<0) {
					showToast(getResources().getString(R.string.invalidPrice));
					return false;
				} else if (groupLecture.getNumMaxStudents()<=0) {
					showToast(getResources().getString(R.string.invalidCapacity));
					return false;
				}
				return true;
			}
			
			private boolean isVazio(String texto) {
				return texto==null || texto.isEmpty();
			}
			
			private void showToast(String message) {
				Toast.makeText(RegisterGroupLectureActivity.this, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (RESULT_CODE_SUBJECT) : { 
	      if (resultCode == Activity.RESULT_OK) { 
	    	  Subject subjectObj = (Subject) data.getSerializableExtra(SUBJECT_GROUP_IDENTIFIER); 
	    	  subject.setText(subjectObj.getName());
	    	  subjectId = subjectObj.get_id();
	      } 
	      break; 
	    } 
	  } 
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

		String myFormat = "dd/MM/yy"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
	}
	
	@Override
	public void onBackPressed() {
	   super.onBackPressed();
	   this.finish();
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
								.getString(R.string.group_lecture_registered_ok),
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RegisterGroupLectureActivity.this,
						ProfessorProfileActivity.class);
				intent.putExtra("user", user);
				startActivity(intent);
				finish();
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
