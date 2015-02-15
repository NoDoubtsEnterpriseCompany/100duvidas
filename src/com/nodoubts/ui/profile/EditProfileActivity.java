package com.nodoubts.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nodoubts.R;
import com.nodoubts.core.Profile;
import com.nodoubts.core.User;

public class EditProfileActivity extends Activity {
	
	Button saveBtn;
	EditText name,email,city,street,number;
	TextView t;
	Intent returnIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		
		saveBtn = (Button) findViewById(R.id.save_btn);
		name = (EditText) findViewById(R.id.name_edit_text);
		email = (EditText) findViewById(R.id.email_edit_text);
		city = (EditText) findViewById(R.id.city_edit_text);
		street = (EditText) findViewById(R.id.street_edit_text);
		number = (EditText) findViewById(R.id.number_edit_text);
		
		
		
		saveBtn.setOnClickListener(new View.OnClickListener() { 
		    @Override
		    public void onClick(View v) {
		    	User user = (User) getIntent().getSerializableExtra("user");
		    	Profile userProfile = user.getProfile();
		    	
		    	if(name.getText()!= null && !name.getText().toString().equals(getResources().getString(R.string.name))){
		    		user.getProfile().setName(name.getText().toString());
		    	}	    	
		    	if(email.getText() != null && !email.getText().toString().equals(getResources().getString(R.string.email)))
		    		user.setEmail(email.getText().toString());
		    	if(city.getText() != null && !city.getText().toString().equals(getResources().getString(R.string.city)))
		    		userProfile.setCity(city.getText().toString());
		    	if(email.getText() != null && !email.getText().toString().equals(getResources().getString(R.string.email)))		    	
		    		
		    	returnIntent = new Intent();
		    	returnIntent.putExtra("result", user);
		    	setResult(RESULT_OK,returnIntent);
		    	finish();
		    }
		});
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
}
