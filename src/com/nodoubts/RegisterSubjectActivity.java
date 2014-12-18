package com.nodoubts;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nodoubts.core.Subject;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.subject.SubjectController;
import com.nodoubts.serverclient.subject.SubjectService;

public class RegisterSubjectActivity extends Activity {
	
	Button addButton;
	SubjectService subjectService;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_subject);
		
		subjectService = new SubjectController();
		addButton = (Button) findViewById(R.id.btn_add_subject);
		if(getIntent().getSerializableExtra("user") != null ){
			user = (User) getIntent().getSerializableExtra("user");
		}
		
		
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Subject subject = getSubject();
				if(isSubjectValid(subject))
					new RegisterNewSubject().execute(subject);
			}
			
			private boolean isSubjectValid(Subject subject) {
				String name = subject.getSubjectName();
					if(name == null || name.isEmpty()){
						Toast.makeText(RegisterSubjectActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
						return false;
					}
				return true;
			}
			
			private Subject getSubject(){
				String name = ((EditText) findViewById(R.id.new_subject_name)).getText().toString();
				return new Subject(name);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_subject, menu);
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
	
	
	protected class RegisterNewSubject extends AsyncTask<Subject,Void, Object>{
		
		ProgressDialog progressDialog;

	 	@Override
			protected void onPreExecute() {
				super.onPreExecute();
				this.progressDialog = new ProgressDialog(RegisterSubjectActivity.this);
				this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
				this.progressDialog.show();
			}
	 	
		@Override
		protected Object doInBackground(Subject... params) {
			try {
				return subjectService.addSubject(params[0]);
			} catch (ApplicationViewException e) {
				return e;
			}
			
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if(result instanceof String){
				Toast.makeText(RegisterSubjectActivity.this, 
						getResources().getString(R.string.subject_registered_ok), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RegisterSubjectActivity.this, TeacherProfileActivity.class);
				intent.putExtra("user", user);
				startActivity(intent);
			}else if(result instanceof Exception){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSubjectActivity.this);
				if(((Exception) result).getMessage().contains("11000")){
					builder.setMessage(R.string.existing_subject);
				}else{
					builder.setMessage(((Exception)result).getMessage());
				}
				System.out.println();
				builder.setTitle("Error");
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
		
		
	}
}
