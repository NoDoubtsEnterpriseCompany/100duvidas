package com.nodoubts.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nodoubts.R;
import com.nodoubts.core.User;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserGenderEnum;
import com.nodoubts.serverclient.user.UserService;

public class EditProfileActivity extends Activity {

	Button saveBtn;
	EditText nameField, cityField, street, number;
	TextView t;
	Intent returnIntent;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		saveBtn = (Button) findViewById(R.id.save_btn);
		nameField = (EditText) findViewById(R.id.name_edit_text);
		cityField = (EditText) findViewById(R.id.city_edit_text);
		user = (User) getIntent().getSerializableExtra("user");

		if(user!=null){
			nameField.setText(user.getName());
			cityField.setText(user.getProfile().getCity());
			if(user.getProfile().getGender() == UserGenderEnum.MALE.getValue()){
				RadioButton genderBtn = (RadioButton) findViewById(R.id.male_radio_btn);
				genderBtn.setChecked(true);
			}else if(user.getProfile().getGender() == UserGenderEnum.FEMALE.getValue()){
				RadioButton genderBtn = (RadioButton) findViewById(R.id.female_radio_btn);
				genderBtn.setChecked(true);
			}
		}
		
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
				
			public void onClick(View v) {
				if (nameField.getText() != null
						&& !nameField.getText()
								.toString()
								.equals(getResources().getString(R.string.name))) {
					user.getProfile().setName(nameField.getText().toString());
				}
				if (cityField.getText() != null
						&& !cityField
								.getText()
								.toString()
								.equals(getResources()
										.getString(R.string.email)))
					user.getProfile().setCity(cityField.getText().toString());

				new SaveUser().execute(user);
			}
		});
	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.male_radio_btn:
			if (checked) {
				user.getProfile().setGender(0);
				break;
			}
		case R.id.female_radio_btn:
			if (checked) {
				user.getProfile().setGender(1);
				break;
			}
		default:
			user.getProfile().setGender(0);
			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_profile, menu);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	protected class SaveUser extends AsyncTask<User, Void, User> {

		UserService userService = new UserController();
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(EditProfileActivity.this);
			this.progressDialog
					.setProgressStyle(AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}

		@Override
		protected User doInBackground(User... params) {
			User user = params[0];
			user.setPassword(null);
			userService.actualizeUser(user);
			return params[0];

		}

		@Override
		protected void onPostExecute(User result) {
			this.progressDialog.dismiss();
			returnIntent = new Intent();
			returnIntent.putExtra("result", result);
			setResult(RESULT_OK, returnIntent);
			finish();
		}
	}
}
