package com.nodoubts.ui.profile;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nodoubts.R;
import com.nodoubts.UserLecturesTabsActivity;
import com.nodoubts.core.User;
import com.nodoubts.ui.util.ImageHelper;

public class UserProfile extends FragmentActivity {

	private TextView name, city;
	private User user;
	private ImageView profilePicture;
	private ProgressBar waitSpinner;
	private RatingBar rating;
	private ImageButton scheduleBtn;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);

		waitSpinner = (ProgressBar) findViewById(R.id.singleSpinner);
		profilePicture = (ImageView) findViewById(R.id.profile_pic);
		name = (TextView) findViewById(R.id.profile_username_textview);
		city = (TextView) findViewById(R.id.profile_city_textview);
		rating = (RatingBar) findViewById(R.id.profile_rating);
		scheduleBtn = (ImageButton) findViewById(R.id.profile_schedule_btn);
		context = this;
		rating.setEnabled(false);
		user = (User) getIntent().getSerializableExtra("user");
		
		scheduleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent calendarActivity = new Intent(context, UserLecturesTabsActivity.class);
				startActivity(calendarActivity);
				finish();
			}
		});
		
		if( user != null ){
			rating.setRating(user.getProfile().getTotalScore());
			name.setText(user.getProfile().getName());
			city.setText(user.getProfile().getCity());
			if(user.getProfile().getProfilePic() != null){
				SetProfilePicture setPictureTask = new SetProfilePicture();
				setPictureTask.execute(user.getProfile().getProfilePic());
			}else{

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture_label);
				ImageHelper.getRoundedCornerBitmap(
						bitmap, profilePicture.getDrawable().getIntrinsicWidth()
						* profilePicture.getDrawable().getIntrinsicHeight());
			}
			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if(resultCode == RESULT_OK){
				User newUser=(User) data.getSerializableExtra("result");
				this.user=newUser;
				this.name.setText(user.getProfile().getName());
				this.city.setText(user.getProfile().getCity());
			}
			if (resultCode == RESULT_CANCELED) {
				//Write your code if there's no result
			}
		}
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	private class SetProfilePicture extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected void onPreExecute(){
			waitSpinner.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
		}

		protected Bitmap doInBackground(String... params) {
			String picUrl = params[0];
			Bitmap bitmap = null;
			String picWidth = String.valueOf(profilePicture.getDrawable().getIntrinsicWidth());
			String picHeight = String.valueOf(profilePicture.getDrawable().getIntrinsicHeight());
			List<NameValuePair> queryParams = new LinkedList<NameValuePair>();
			queryParams.add(new BasicNameValuePair("width", picWidth));
			queryParams.add(new BasicNameValuePair("height", picHeight));
			String encodedParams = URLEncodedUtils.format(queryParams, "utf-8");
			String picQuery = picUrl.concat("?").concat(encodedParams);

			try {
				URL url = new URL(picQuery);
				HttpGet httpRequest = null;

				httpRequest = new HttpGet(url.toURI());

				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient
						.execute(httpRequest);

				HttpEntity entity = response.getEntity();
				BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
				InputStream input = b_entity.getContent();

				bitmap = BitmapFactory.decodeStream(input);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return ImageHelper.getRoundedCornerBitmap(
					bitmap, profilePicture.getDrawable().getIntrinsicWidth()
					* profilePicture.getDrawable().getIntrinsicHeight());
		}
		@Override
		protected void onPostExecute(Bitmap result){
			waitSpinner.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			if(result!=null){
				profilePicture.setImageBitmap(result);
			}
		}
	}

}