package com.nodoubts.ui.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nodoubts.R;
import com.nodoubts.SplashActivity;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;
import com.nodoubts.serverclient.util.SessionManager;

public class RegisterUserActivity extends Activity{
	
	private SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		
		sessionManager = new SessionManager(getApplicationContext());
		
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
					Toast.makeText(RegisterUserActivity.this, getResources().getString(R.string.fields_not_filled), Toast.LENGTH_SHORT).show();
					return false;
				}else if (user.getPassword().length() < 6){
					Toast.makeText(RegisterUserActivity.this, getResources().getString(R.string.invalid_password_format), Toast.LENGTH_SHORT).show();
					return false;
				}
				if(!isValidEmail(user.getEmail())){
					Toast.makeText(RegisterUserActivity.this, getResources().getString(R.string.invalid_email_format), Toast.LENGTH_SHORT).show();
					return false;
				};
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
	
	private boolean isValidEmail(String email) {
		String regex = "^[-!#$%&'*+/0-9=?A-Z^_a-z{|}~](\\.?[-!#$%&'*+/0-9=?A-Z^_a-z{|}~])*@[a-zA-Z](-?[a-zA-Z0-9])*(\\.[a-zA-Z](-?[a-zA-Z0-9])*)+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	@Override
	public void onBackPressed() {
	   super.onBackPressed();
	   this.finish();
    }
	
	protected class UserCreator extends AsyncTask<User, Void, Object> {

		UserService userService = new UserController();
		ProgressDialog progressDialog;
		User user;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(RegisterUserActivity.this);
			this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}
		
		@Override
		protected Object doInBackground(User... params) {			
			try {
				user = params[0];
				return userService.saveUser(user);
			} catch (ApplicationViewException e) {
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			this.progressDialog.dismiss();
			if(result instanceof String){
				Toast.makeText(RegisterUserActivity.this, 
						getResources().getString(R.string.user_registered_ok), Toast.LENGTH_LONG).show();
				sessionManager.createLoginSession("", user.getUsername());
				Intent splashScreen = new Intent(getApplicationContext(), SplashActivity.class);
				startActivity(splashScreen);
				finish();
			}else if(result instanceof Exception){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
				builder.setMessage(R.string.existing_user);
				builder.setTitle("Error");
				AlertDialog dialog = builder.create();
				dialog.show();
			}

		}
	}
}
