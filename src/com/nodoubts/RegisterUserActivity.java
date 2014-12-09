package com.nodoubts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nodoubts.core.User;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;

public class RegisterUserActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		
		Button createUserBtn = (Button) findViewById(R.id.createUserBtn);
		createUserBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				User user = extractUser();
				if (validateUser(user)) {
					new UserCreator().execute(user);
				}
			}

			private boolean validateUser(User user) {
				if (user.getUsername().trim().equals("") ||
						user.getEmail().trim().equals("") ||
						user.getPassword().trim().equals("") ||
						user.getProfile().getName().equals("")) {
					Toast.makeText(RegisterUserActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			}

			private User extractUser() {
				String username = ((EditText) findViewById(R.id.register_username)).getText().toString();
				String name = ((EditText) findViewById(R.id.register_name)).getText().toString();
				String password = ((EditText) findViewById(R.id.register_passwordToRegister)).getText().toString();
				String email = ((EditText) findViewById(R.id.register_email)).getText().toString();
				User user = new User(username, password, email,name);
				user.getProfile().setName(name);
				return user;
			}
		});
	}
	
	protected class UserCreator extends AsyncTask<User, Void, String> {

		UserService userService = new UserController();
		ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(RegisterUserActivity.this);
			this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}
		
		@Override
		protected String doInBackground(User... params) {
			String result = userService.saveUser(params[0]);
			while (result==null) {}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			this.progressDialog.dismiss();
			Toast.makeText(RegisterUserActivity.this, 
					getResources().getString(R.string.user_registered_ok), Toast.LENGTH_LONG).show();
			Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}
}
