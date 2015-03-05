package com.nodoubts.ui.recommendation;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nodoubts.R;
import com.nodoubts.core.Recommendation;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.recommendation.RecommendationController;
import com.nodoubts.serverclient.recommendation.RecommendationService;
import com.nodoubts.ui.profile.UserProfile;

public class RegisterRecommendationActivity extends Activity {
	
	Button addButton;
	RecommendationService recommendationService;
	User user;
	EditText nameEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_recommendation);
		
		recommendationService = new RecommendationController();
		addButton = (Button) findViewById(R.id.btn_add_recommendation);
		if(getIntent().getSerializableExtra("user") != null ){
			user = (User) getIntent().getSerializableExtra("user");
		}
		
		nameEt = (EditText) findViewById(R.id.new_recommendation_name);
		nameEt.requestFocus();
		
		
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Recommendation recommendation = getRecommendation();
				if(isRecommendationValid(recommendation))
					new RegisterNewRecommendation().execute(recommendation);
			}
			
			private boolean isRecommendationValid(Recommendation recommendation) {
				String name = recommendation.getTeacherUserName();
					if(name == null || name.isEmpty()){
						//TODO: Remove hardcoded error messages
						Toast.makeText(RegisterRecommendationActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
						return false;
					}
				return true;
			}
			
			private Recommendation getRecommendation(){
				
				String name = nameEt.getText().toString();
				String description = ((EditText) findViewById(R.id.new_recommendation_description)).getText().toString();
				return new Recommendation(name, description);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_subject, menu);
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
	
	protected class RegisterNewRecommendation extends AsyncTask<Recommendation,Void, Object>{
		
		ProgressDialog progressDialog;

	 	@Override
			protected void onPreExecute() {
				super.onPreExecute();
				this.progressDialog = new ProgressDialog(RegisterRecommendationActivity.this);
				this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
				this.progressDialog.show();
			}
	 	
		@Override
		protected Object doInBackground(Recommendation... params) {
			try {
				user = (User) (getIntent().getExtras().get("user"));
				return recommendationService.addRecommendation(params[0], user);
			} catch (ApplicationViewException e) {
				return e;
			}
			
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if(result instanceof String){
				Toast.makeText(RegisterRecommendationActivity.this, 
						getResources().getString(R.string.recommendation_registered_ok), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RegisterRecommendationActivity.this, UserProfile.class);
				intent.putExtra("user", user);
				startActivity(intent);
				finish();
			}else if(result instanceof Exception){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterRecommendationActivity.this);
				if(((Exception) result).getMessage().contains("11000")){
					builder.setMessage(R.string.existing_recommendation);
				}else{
					builder.setMessage(((Exception)result).getMessage());
				}
				builder.setTitle("Error");
				AlertDialog dialog = builder.create();
				dialog.show();
				dialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();
					}
				});
			}
		}
		
		
	}
}
