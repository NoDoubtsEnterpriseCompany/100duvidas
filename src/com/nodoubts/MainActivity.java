package com.nodoubts;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;
import com.nodoubts.serverclient.user.UserController;



public class MainActivity extends Activity {

	UserController userController;
	ServerService serverController;
	EditText userNameEditText;
	EditText passEditText;
	Button loginBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		userController = new UserController();
		serverController= new ServerController();
		userNameEditText = (EditText) findViewById(R.id.email);
		passEditText = (EditText) findViewById(R.id.passwordEditText);
		loginBtn = (Button) findViewById(R.id.loginbtn);
		
		loginBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	new LoginAsyncTask().execute();
		    }
		});
		    
		Button registerBtn = (Button) findViewById(R.id.registerbtn);
		
		registerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
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

		@Override
		protected String doInBackground(String... params) {
			JsonObject jsonTransaction = new JsonObject();
			jsonTransaction.addProperty("login", userNameEditText.getText()
					.toString());
			jsonTransaction.addProperty("password", passEditText.getText()
					.toString());

			String response = userController.authenticateUser(jsonTransaction
					.toString());
			int resultCode = -1;
			try {
				JSONObject jsonResponse = new JSONObject(response);
				JSONObject code = new JSONObject(
						jsonResponse.getString("result"));
				resultCode = code.getInt("code");

			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (resultCode == 200) {
				Intent profileScreen = new Intent(getApplicationContext(),
						TeacherProfileActivity.class);
				startActivity(profileScreen);
			}
			return response;
		}

	}
}
