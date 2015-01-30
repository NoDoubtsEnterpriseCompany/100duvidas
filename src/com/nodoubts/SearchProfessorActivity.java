package com.nodoubts;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.nodoubts.core.SearchAdapter;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;

public class SearchProfessorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_professor);
		
		Button btn = (Button) findViewById(R.id.search_professor_btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText editText = (EditText) findViewById(R.id.search_name_subject);
				new SearchProfessorAsyncTask().execute(editText.getText().toString());
			}
		});
		//quando clicar no botao, executar asynctask.
		//povoar listview com a lista
		
	}
	
	private class SearchProfessorAsyncTask extends AsyncTask<String, Void, Void> {
		
		UserService userService = new UserController();
		ProgressDialog progressDialog;
		List<User> users;

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(SearchProfessorActivity.this);
			this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			try {
				users = userService.searchForUsers(params[0]);
			} catch (ApplicationViewException e) {
				e.printStackTrace();
			}
			while (users==null){}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			this.progressDialog.dismiss();
			SearchAdapter<User> professorsAdapter = new SearchAdapter<User>(SearchProfessorActivity.this, users);
			
			ListView listView = (ListView) findViewById(R.id.list_professors);
			listView.setAdapter(professorsAdapter);
		}
	}
}
