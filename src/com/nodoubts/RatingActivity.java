package com.nodoubts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RatingActivity extends Activity {

	TextView name;
	RatingBar ratingbar;
	EditText comment;
	ImageView pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		name = (TextView) findViewById(R.id.user_name_ratingAct);
		ratingbar = (RatingBar) findViewById(R.id.rating_bar_ratingAct);
		pic = (ImageView) findViewById(R.id.view_professor_pic_ratingAct);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rating, menu);
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
