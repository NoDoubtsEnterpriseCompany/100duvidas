package com.nodoubts;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nodoubts.core.User;

public class TeacherProfileActivity extends Activity {
	
	Button editBtn;
	TextView name;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_profile);
		
		editBtn = (Button) findViewById(R.id.edit_profilebtn);
		name = (TextView) findViewById(R.id.name_text_view);
		user = (User) getIntent().getSerializableExtra("user");
		
		name.setText(user.getProfile().getName());
		
		
		editBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent editScreen = new Intent(getApplicationContext(),EditProfileActivity.class);
		    	editScreen.putExtra("user", user);
		    	startActivity(editScreen);
		    }
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_profile, menu);
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
}
