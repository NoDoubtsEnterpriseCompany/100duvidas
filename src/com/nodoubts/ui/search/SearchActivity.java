package com.nodoubts.ui.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

	ListView listView;
	Subject subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_search);
		listView = (ListView) findViewById(R.id.subjects_list_view);
		Button btn = (Button) findViewById(R.id.submit_search);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.search_text);
				new SearchAsyncTask().execute(editText.getText().toString());
			}
		});

		// quando clicar no botao, executar asynctask.
		// povoar listview com a lista

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (subject != null) {
					Intent searchScreen = new Intent(SearchActivity.this,
							subject.getActivityClass());
					searchScreen.putExtra("searchObj", subject);
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

	private class SearchAsyncTask extends AsyncTask<String, Void, Object> {

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
		protected Object doInBackground(String... params) {
			try {
				subject = subjectService.getSubject(params[0]);
				return subject;
			} catch (ApplicationViewException e) {
				Log.i(this.getClass().getName(), "Error: " + e.getMessage());
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			this.progressDialog.dismiss();
			if (result instanceof Subject) {
				List<Subject> subjects = new ArrayList<Subject>();
				subjects.add((Subject) result);
				SearchAdapter<Subject> subjectsAdapter = new SearchAdapter<Subject>(
						SearchActivity.this, subjects);
				listView.setAdapter(subjectsAdapter);

			} else {

			}
		}
	}
}
