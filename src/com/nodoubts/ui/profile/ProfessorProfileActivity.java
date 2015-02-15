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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nodoubts.R;
import com.nodoubts.R.id;
import com.nodoubts.R.layout;
import com.nodoubts.R.menu;
import com.nodoubts.core.User;
import com.nodoubts.ui.lecture.UserLectureListActivity;
import com.nodoubts.ui.rating.RegisterGroupLectureActivity;
import com.nodoubts.ui.subject.SubjectListActivity;
import com.nodoubts.ui.util.ImageHelper;

public class ProfessorProfileActivity extends Activity {
	
	Button editBtn,addSubjectBtn, addGroupLectureBtn,lectures;
	TextView name;
	User user;
	ImageView profilePicture;
	ProgressBar waitSpinner;
	Context context;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_profile);
		
		waitSpinner = (ProgressBar) findViewById(R.id.singleSpinner);
		profilePicture = (ImageView) findViewById(R.id.img_view_teacher);
		editBtn = (Button) findViewById(R.id.edit_profilebtn);
		addSubjectBtn = (Button) findViewById(R.id.add_subjectbtn);
		addGroupLectureBtn = (Button) findViewById(R.id.add_grouplecturebtn);
		name = (TextView) findViewById(R.id.name_text_view);
		lectures = (Button) findViewById(R.id.lectures_btn);
		context = getApplicationContext();
		
		if(getIntent().getSerializableExtra("user") != null ){
			user = (User) getIntent().getSerializableExtra("user");
			name.setText(user.getProfile().getName());
			if(user.getProfile().getProfilePic() != null){
				SetProfilePicture setPictureTask = new SetProfilePicture();
				setPictureTask.execute(user.getProfile().getProfilePic());
			}
		}
		
		editBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent editScreen = new Intent(getApplicationContext(),EditProfileActivity.class);
		    	editScreen.putExtra("user", user);
		    	startActivityForResult(editScreen,1);
		    }
		});
		

		addSubjectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent searchScreen = new Intent(getApplicationContext(), SubjectListActivity.class);
				searchScreen.putExtra("user", (User) getIntent().getSerializableExtra("user"));
				startActivity(searchScreen);
			}
		});
		
		addGroupLectureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent createGroupScreen = new Intent(getApplicationContext(), RegisterGroupLectureActivity.class);
				createGroupScreen.putExtra("user",  user);
				startActivity(createGroupScreen);
			}
		});
		
		lectures.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent createUserLectureListScreen = new Intent(getApplicationContext(), UserLectureListActivity.class);
				createUserLectureListScreen.putExtra("user",  user);
				startActivity(createUserLectureListScreen);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	            User newUser=(User) data.getSerializableExtra("result");
	            this.user=newUser;
	            this.name.setText(user.getProfile().getName());
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
				Log.i("TeacherProfile", picQuery);
				
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
