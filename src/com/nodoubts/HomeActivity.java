package com.nodoubts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nodoubts.core.User;


public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Button buttonProfile = (Button) findViewById(R.id.btn_profile);
		buttonProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent profileScreen = new Intent(getApplicationContext(),TeacherProfileActivity.class);
				profileScreen.putExtra("user", (User) getIntent().getSerializableExtra("user"));
				startActivity(profileScreen);
			}
		});
		
		Button buttonSearch = (Button) findViewById(R.id.btn_search);
		buttonSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent searchScreen = new Intent(getApplicationContext(), SearchActivity.class);
				startActivity(searchScreen);
			}
		});
		
		
	
	}
}
