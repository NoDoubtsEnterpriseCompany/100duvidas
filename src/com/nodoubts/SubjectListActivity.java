package com.nodoubts;

import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nodoubts.core.Subject;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.subject.SubjectController;
import com.nodoubts.serverclient.subject.SubjectService;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;

public class SubjectListActivity extends Activity {

	private SubjectListActivity self;

	ListView listView;
	
	ListAdapter myAdpater;
	
	User user;
	
	boolean isGroupLectureCreation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.activity_subject_list);

		new SubjectAsyncTask().execute();

		this.isGroupLectureCreation = getIntent().getBooleanExtra("isGroupLectureCreation", false);
		
		listView = (ListView) findViewById(R.id.listView_subjectList);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					final int position, long id) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						self);

				// set title
				alertDialogBuilder.setTitle("Your Title");

				// set dialog message
				alertDialogBuilder
						.setMessage("Add " + ((TextView) view).getText() + " to your subjects?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										
										if(getIntent().getSerializableExtra("user") != null ){
											user = (User) getIntent().getSerializableExtra("user");
										}
										
										Subject subject = (Subject) myAdpater.getItem(position);
										
										JsonObject jsonTransaction = new JsonObject();
										jsonTransaction.addProperty("name", subject.getSubjectName());
										jsonTransaction.addProperty("username", user.getUsername());
										
										new AddSubjectAsyncTask(jsonTransaction).execute();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});
				
				Subject subject = (Subject) myAdpater.getItem(position);
				if (!isGroupLectureCreation) {
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					// show it
					alertDialog.show();
				} else {
					Intent resultIntent = new Intent();
					resultIntent.putExtra(RegisterGroupLectureActivity.SUBJECT_GROUP_IDENTIFIER, subject);
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				}
			}
		});
		
		Button createSubjectBtn = (Button) findViewById(R.id.btn_create_new_subject);
		createSubjectBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent addSubjectScreen = new Intent(getApplicationContext(),RegisterSubjectActivity.class);
		    	addSubjectScreen.putExtra("user", user);
		    	addSubjectScreen.putExtra("isGroupLectureCreation", isGroupLectureCreation);
		    	startActivity(addSubjectScreen);
		    }
		});
		
		
		
		Button buttonBack = (Button) findViewById(R.id.btn_search_subject_back);
		buttonBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void populateList(List<Subject> subjectList) {
		myAdpater = new ArrayAdapter<Subject>(self,
				android.R.layout.simple_list_item_1, subjectList);
		listView.setAdapter(myAdpater);
	}
	
	public void toast(String string){
		Toast.makeText(getApplicationContext(),
				string,
				Toast.LENGTH_SHORT).show();
	}

	class SubjectAsyncTask extends AsyncTask<URL, Integer, Long> {

		SubjectService subjectService = new SubjectController();
		List<Subject> subjectList;

		protected Long doInBackground(URL... urls) {
			try {
				subjectList = subjectService.getSubjects();
			} catch (ApplicationViewException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return 1L;
		}
		
		@Override
		protected void onPostExecute(Long result) {
			self.populateList(subjectList);
		}
	}
	
	class AddSubjectAsyncTask extends AsyncTask<URL, Integer, Long> {
		
		private JsonObject jsonObject;

		public AddSubjectAsyncTask(JsonObject jsonObject) {
			this.jsonObject = jsonObject;
		}
		

		UserService userService = new UserController();

		protected Long doInBackground(URL... urls) {
			try {
				userService.addSubjectToUser(jsonObject.toString());
			} catch (ApplicationViewException e) {
				e.printStackTrace();
			}
			return 1L;
		}
		
		@Override
		protected void onPostExecute(Long result) {
			self.toast("Subject successfully added");
		}
	}
}
