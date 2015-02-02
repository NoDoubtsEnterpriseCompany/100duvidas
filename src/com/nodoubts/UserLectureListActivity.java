package com.nodoubts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nodoubts.core.User;

public class UserLectureListActivity extends Activity {
	
	TextView name;
	ImageView profilePicture;
	ListView lecturesList;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_lecture_list);
		
		name = (TextView) findViewById(R.id.user_name_userLecAct);
		profilePicture= (ImageView) findViewById(R.id.view_professor_pic_userLecAct);
		lecturesList = (ListView) findViewById(R.id.lecutes_list_view_LecAct);
		
		if(getIntent().getExtras() !=  null){
			user = (User) getIntent().getExtras().get("user");
			name.setText(user.getName());
				
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_lecture_list, menu);
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
