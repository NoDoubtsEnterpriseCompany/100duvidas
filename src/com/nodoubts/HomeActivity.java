package com.nodoubts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nodoubts.R;
import com.nodoubts.core.User;
import com.nodoubts.ui.profile.ProfessorProfileActivity;
import com.nodoubts.ui.search.SearchActivity;


public class HomeActivity extends Activity {

	public static User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		user = (User) getIntent().getSerializableExtra("user");
		Button buttonProfile = (Button) findViewById(R.id.btn_profile);
		buttonProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent profileScreen = new Intent(getApplicationContext(),ProfessorProfileActivity.class);
				profileScreen.putExtra("user", user);
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
	
   @Override
   public void onBackPressed() {
	   super.onBackPressed();
	   this.finish();
   }
}
