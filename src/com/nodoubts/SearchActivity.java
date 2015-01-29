package com.nodoubts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.nodoubts.core.SearchAdapter;
import com.nodoubts.core.Subject;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.subject.SubjectController;
import com.nodoubts.serverclient.subject.SubjectService;

public class SearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	   //set content view AFTER ABOVE sequence (to avoid crash)
	    this.setContentView(R.layout.activity_search);
		
		Button btn = (Button) findViewById(R.id.submit_search);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.search_text);
				new SearchAsyncTask().execute(editText.getText().toString());
			}
		});
		//quando clicar no botao, executar asynctask.
		//povoar listview com a lista
		
	}
	
	private class SearchAsyncTask extends AsyncTask<String, Void, Object> {
		
		SubjectService subjectService = new SubjectController();
		ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(SearchActivity.this);
			this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}
		
		@Override
		protected Object doInBackground(String... params) {
			try {
				Subject subject = subjectService.getSubject(params[0]);
				return subject;
			} catch (ApplicationViewException e) {
				Log.i(this.getClass().getName(), "Error: " +e.getMessage());
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			this.progressDialog.dismiss();
			if(result instanceof Subject){
				List<Subject> subjects = new ArrayList<Subject>();
				subjects.add((Subject)result);
				SearchAdapter<Subject> subjectsAdapter = new SearchAdapter<Subject>(SearchActivity.this, subjects);
				ListView listView = (ListView) findViewById(R.id.subjects_list_view);
				listView.setAdapter(subjectsAdapter);
			}else{
				
			}
		}
	}
}
