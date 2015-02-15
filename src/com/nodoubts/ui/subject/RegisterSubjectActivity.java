package com.nodoubts.ui.subject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nodoubts.R;
import com.nodoubts.R.id;
import com.nodoubts.R.layout;
import com.nodoubts.R.menu;
import com.nodoubts.R.string;
import com.nodoubts.core.Subject;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.subject.SubjectController;
import com.nodoubts.serverclient.subject.SubjectService;
import com.nodoubts.ui.profile.ProfessorProfileActivity;

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
						//TODO: Remove hardcoded error messages
						Toast.makeText(RegisterSubjectActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
						return false;
					}
				return true;
			}
			
			private Subject getSubject(){
				String name = ((EditText) findViewById(R.id.new_subject_name)).getText().toString();
				String description = ((EditText) findViewById(R.id.new_subject_description)).getText().toString();
				return new Subject(name, description);
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
	
	@Override
	public void onBackPressed() {
	   super.onBackPressed();
	   this.finish();
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
				Intent intent = new Intent(RegisterSubjectActivity.this, ProfessorProfileActivity.class);
				intent.putExtra("user", user);
				startActivity(intent);
				finish();
			}else if(result instanceof Exception){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSubjectActivity.this);
				if(((Exception) result).getMessage().contains("11000")){
					builder.setMessage(R.string.existing_subject);
				}else{
					builder.setMessage(((Exception)result).getMessage());
				}
				builder.setTitle("Error");
				AlertDialog dialog = builder.create();
				dialog.show();
				dialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();
					}
				});
			}
		}
		
		
	}
}
