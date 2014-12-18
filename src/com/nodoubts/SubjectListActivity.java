package com.nodoubts;

import java.net.URL;
import java.util.ArrayList;
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

import com.nodoubts.core.Subject;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.subject.SubjectController;
import com.nodoubts.serverclient.subject.SubjectService;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;

public class SubjectListActivity extends Activity {

	private SubjectListActivity self;

	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.activity_subject_list);

		new SubjectAsyncTask().execute();

		listView = (ListView) findViewById(R.id.listView_subjectList);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
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
										Toast.makeText(getApplicationContext(),
												((TextView) view).getText(),
												Toast.LENGTH_SHORT).show();
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

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
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
		ListAdapter myAdpater = new ArrayAdapter<Subject>(self,
				android.R.layout.simple_list_item_1, subjectList);
		listView.setAdapter(myAdpater);
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
			self.populateList(subjectList);
			return 1L;
		}
	}
	
//	class AddSubjectAsyncTask extends AsyncTask<URL, Integer, Long> {
//		
//		private Subject subject;
//
//		public AddSubjectAsyncTask(Subject subject) {
//			this.subject = subject;
//		}
//
//		UserService userService = new UserController();
//
//		protected Long doInBackground(URL... urls) {
//			userService.addSubject(subject);
//			// self.populateList(subjectList);
//			return 1L;
//		}
//	}

}
