package com.nodoubts.ui.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.nodoubts.R;
import com.nodoubts.core.SearchAdapter;
import com.nodoubts.core.Subject;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.subject.SubjectController;
import com.nodoubts.serverclient.subject.SubjectService;

public class SearchActivity extends Activity {

	private ListView listView;
	private List<Subject> subjects;
	private List<Subject> matchingSubjects;
	private AutoCompleteTextView editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_search);
		listView = (ListView) findViewById(R.id.subjects_list_view);
		Button btn = (Button) findViewById(R.id.submit_search);
		editText = (AutoCompleteTextView) findViewById(R.id.search_text);
		
		
		new SearchAsyncTask().execute("");
		
		
		//TODO delete this callback method
		/*btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
*/
		
		 editText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				matchingSubjects = new ArrayList<Subject>();
				matchingSubjects.addAll(subjects);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
					matchingSubjects.clear();
					for (Subject subject : subjects) {
						if(subject.getName().toLowerCase().trim().contains(editText.getText())){
							matchingSubjects.add(subject);
						}
					}
					SearchAdapter<Subject> subjectsAdapter = new SearchAdapter<Subject>(
							SearchActivity.this, matchingSubjects);
					listView.setAdapter(subjectsAdapter);
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
			 
		 });

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (subjects != null) {
					Intent searchScreen = new Intent(SearchActivity.this,
							subjects.get(position).getActivityClass());
					searchScreen.putExtra("searchObj", subjects.get(position));
					startActivity(searchScreen);
				}

			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	private class SearchAsyncTask extends AsyncTask<String, Void, List<Subject>> {

		SubjectService subjectService = new SubjectController();
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(SearchActivity.this);
			this.progressDialog
					.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}

		@Override
		protected List<Subject> doInBackground(String... params) {
			try {
				subjects = subjectService.getSubjects();
				return subjects;
			} catch (ApplicationViewException e) {
				Log.e(this.getClass().getName(), "Error: " + e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<Subject> result) {
			this.progressDialog.dismiss();
			if (result instanceof List) {
				SearchAdapter<Subject> subjectsAdapter = new SearchAdapter<Subject>(
						SearchActivity.this, subjects);
				listView.setAdapter(subjectsAdapter);
			} else {

			}
		}
	}
}
