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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nodoubts.core.User;

public class TeacherProfileActivity extends Activity {
	
	Button editBtn,addSubjectBtn;
	TextView name;
	User user;
	ImageView profilePicture;
	ProgressBar waitSpinner;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_profile);
		
		waitSpinner = (ProgressBar) findViewById(R.id.singleSpinner);
		profilePicture = (ImageView) findViewById(R.id.img_view_teacher);
		editBtn = (Button) findViewById(R.id.edit_profilebtn);
		addSubjectBtn = (Button) findViewById(R.id.add_subjectbtn);
		name = (TextView) findViewById(R.id.name_text_view);
		//user = (User) getIntent().getSerializableExtra("user");
		
		//name.setText(user.getProfile().getName());
		
		
		editBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent editScreen = new Intent(getApplicationContext(),EditProfileActivity.class);
		    	editScreen.putExtra("user", user);
		    	startActivityForResult(editScreen,1);
		    }
		});
		
		addSubjectBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent addSubjectScreen = new Intent(getApplicationContext(),RegisterSubjectActivity.class);
		    	startActivity(addSubjectScreen);
		    }
		});
		
		
		
		//SetProfilePicture setPictureTask = new SetProfilePicture();
		//setPictureTask.execute(user.getProfile().getProfilePic());

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
				return bitmap;
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
