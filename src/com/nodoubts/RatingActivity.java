package com.nodoubts;

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

import com.nodoubts.core.Lecture;
import com.nodoubts.core.Rating;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;
import com.nodoubts.util.ui.ImageHelper;

public class RatingActivity extends Activity {

	TextView name;
	RatingBar ratingbar;
	EditText commentEditText;
	ImageView profilePicture;
	User user;
	ProgressBar waitSpinner;
	Button rate;
	UserService userController;
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
		userController = new UserController();

		if (getIntent().getExtras() != null) {
			user = (User) getIntent().getExtras().get("user");
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
				try {
					userController.addRatingToUser(teacherName, rating);
				} catch (ApplicationViewException e) {
					Log.e("ratingActivitiy", e.getMessage());
				} catch (JSONException e) {
					Log.e("ratingActivitiy", e.getMessage());
				}
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
}
