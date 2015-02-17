package com.nodoubts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.ui.fragments.FbLoginFragment.FbLoginCallback;
import com.nodoubts.ui.user.RegisterUserActivity;

public class MainActivity extends FragmentActivity implements FbLoginCallback {
	UserController userController;
	ServerService serverController;
	EditText userNameEditText;
	EditText passEditText;
	Button loginBtn;
	Button registerBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().commit();
		}

		userController = new UserController();
		serverController = ServerController.getInstance();
		userNameEditText = (EditText) findViewById(R.id.email);
		passEditText = (EditText) findViewById(R.id.passwordEditText);
		loginBtn = (Button) findViewById(R.id.loginbtn);
		loginBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
					new LoginAsyncTask().execute();
			}
		});

		registerBtn = (Button) findViewById(R.id.registerbtn);

		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(MainActivity.this,
						RegisterUserActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	private class LoginAsyncTask extends AsyncTask<String, Void, String> {

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(MainActivity.this);
			this.progressDialog
					.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String response = "";
			JsonObject jsonTransaction = new JsonObject();
			jsonTransaction.addProperty("login", userNameEditText.getText()
					.toString());
			jsonTransaction.addProperty("password", passEditText.getText()
					.toString());

			try {
				response = userController.authenticateUser(jsonTransaction
						.toString());

				User user = userController.findUser(userNameEditText.getText()
						.toString());
				Intent homeScreen = new Intent(getApplicationContext(),
						HomeActivity.class);
				homeScreen.putExtra("user", user);
				startActivity(homeScreen);
			} catch (ApplicationViewException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.isEmpty()){
				Toast.makeText(MainActivity.this, R.string.invalid_user_pass, Toast.LENGTH_SHORT).show();
			}
			this.progressDialog.dismiss();
		}
	}

	@Override
	public void fbLoggedIn(User user) {

		getSupportFragmentManager()
				.beginTransaction()
				.remove(getSupportFragmentManager().findFragmentById(
						R.id.FbLoginFragment)).commit();
		getSupportFragmentManager().popBackStack();

		Intent homeScreen = new Intent(this, HomeActivity.class);
		homeScreen.putExtra("user", user);
		startActivity(homeScreen);
		finish();
	}

	@Override
	public void fbLoggedOut() {
	}
}
