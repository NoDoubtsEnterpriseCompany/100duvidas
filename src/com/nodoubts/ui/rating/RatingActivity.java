package com.nodoubts.ui.rating;

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
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nodoubts.R;
import com.nodoubts.R.id;
import com.nodoubts.R.layout;
import com.nodoubts.R.menu;
import com.nodoubts.core.Lecture;
import com.nodoubts.core.Rating;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;
import com.nodoubts.ui.util.ImageHelper;

public class RatingActivity extends Activity {

	TextView name;
	RatingBar ratingbar;
	EditText commentEditText;
	ImageView profilePicture;
	User user;
	ProgressBar waitSpinner;
	Button rate;
	Lecture lecture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);

		name = (TextView) findViewById(R.id.user_name_ratingAct);
		ratingbar = (RatingBar) findViewById(R.id.rating_bar_ratingAct);
		profilePicture = (ImageView) findViewById(R.id.view_professor_pic_ratingAct);
		waitSpinner = (ProgressBar) findViewById(R.id.singleSpinner);
		rate = (Button) findViewById(R.id.rate_btn_ratingAct);
		commentEditText = (EditText) findViewById(R.id.comment_edit_text_ratingAct);

		if (getIntent().getExtras() != null) {
		//	user = (User) getIntent().getExtras().get("user");
			user = new User("joao","asdf","j@j.com");
			user.getProfile().setName("joao");
			user.setUsername("joao");
			lecture = (Lecture) getIntent().getExtras().get("lecture");
			name.setText(user.getName());

			if (user.getProfile().getProfilePic() != null) {
				SetProfilePicture setPictureTask = new SetProfilePicture();
				setPictureTask.execute(user.getProfile().getProfilePic());
			}
		}

		rate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String teacherName = lecture.getTeacher().getUsername();
				float score = ratingbar.getRating();
				String comment = commentEditText.getText().toString();
				Rating rating = new Rating(score, comment, user);
				
				new RatingUser().execute(rating,teacherName);
			}
		});
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

	private class SetProfilePicture extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected void onPreExecute() {
			waitSpinner.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
		}

		protected Bitmap doInBackground(String... params) {
			String picUrl = params[0];
			Bitmap bitmap = null;
			String picWidth = String.valueOf(profilePicture.getDrawable()
					.getIntrinsicWidth());
			String picHeight = String.valueOf(profilePicture.getDrawable()
					.getIntrinsicHeight());
			List<NameValuePair> queryParams = new LinkedList<NameValuePair>();
			queryParams.add(new BasicNameValuePair("width", picWidth));
			queryParams.add(new BasicNameValuePair("height", picHeight));
			String encodedParams = URLEncodedUtils.format(queryParams, "utf-8");
			String picQuery = picUrl.concat("?").concat(encodedParams);
			Log.i("RatingScreen", picQuery);

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

			return ImageHelper.getRoundedCornerBitmap(bitmap, profilePicture
					.getDrawable().getIntrinsicWidth()
					* profilePicture.getDrawable().getIntrinsicHeight());
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			waitSpinner.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			if (result != null) {
				profilePicture.setImageBitmap(result);
			}
		}
	}
	
	
	
	protected class RatingUser extends AsyncTask<Object, Void, Object> {

		UserService userService = new UserController();
		ProgressDialog progressDialog;
		Rating rating;
		String teacherName;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(RatingActivity.this);
			this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}
		
		@Override
		protected Object doInBackground(Object... params) {			
			try {
				rating = (Rating) params[0];
				teacherName = (String) params[1];
				try {
					userService.addRatingToUser(teacherName, rating);
				} catch (JSONException e) {
					Log.e("rating",e.getMessage());
				}
			} catch (ApplicationViewException e) {
				
			}
			return new Object();
		}
		
		@Override
		protected void onPostExecute(Object result) {
			this.progressDialog.dismiss();
			
			setResult(RESULT_OK);
			finish();
			/*
			if(result instanceof String){
				Toast.makeText(RatingActivity.this, 
						getResources().getString(R.string.user_registered_ok), Toast.LENGTH_LONG).show();
				Intent homeScreen = new Intent(getApplicationContext(),HomeActivity.class);
				homeScreen.putExtra("user", user);
				startActivity(homeScreen);
			}else if(result instanceof Exception){
				AlertDialog.Builder builder = new AlertDialog.Builder(RatingActivity.this);
				builder.setMessage(((Exception)result).getMessage());
				builder.setTitle("Error");
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			 	*/
		}
	}
}
