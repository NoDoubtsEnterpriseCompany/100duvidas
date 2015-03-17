package com.nodoubts.ui.recommendation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nodoubts.R;
import com.nodoubts.core.Recommendation;
import com.nodoubts.core.SearchAdapter;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.recommendation.RecommendationController;
import com.nodoubts.serverclient.recommendation.RecommendationService;
import com.nodoubts.ui.lecture.LectureCreationDialog;

public class RecommendationListActivity extends FragmentActivity{

	private RecommendationListActivity self;
	ListView listView;
	ListAdapter myAdpater;
	User user;
	Recommendation recommendation;
	List<Recommendation> recommendationList;
	List<Recommendation> matchingRecommendationList;
	EditText editText;
	
	LectureCreationDialog lectureDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.activity_recommendation_list);
		editText = (EditText) findViewById(R.id.editText_RecommendationSearch);
		new RecommendationAsyncTask().execute();

		editText.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					matchingRecommendationList = new ArrayList<Recommendation>();
					if(!recommendationList.isEmpty())
						matchingRecommendationList.addAll(recommendationList);
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					matchingRecommendationList.clear();
						for (Recommendation recommendation : recommendationList) {
							if(recommendation.getName().toLowerCase().trim().contains(editText.getText())){
								matchingRecommendationList.add(recommendation);
							}
						}
						SearchAdapter<Recommendation> subjectsAdapter = new SearchAdapter<Recommendation>(
								RecommendationListActivity.this, matchingRecommendationList);
						listView.setAdapter(subjectsAdapter);
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
				 
			 });
		
		
		listView = (ListView) findViewById(R.id.listView_recommendationList);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					final int position, long id) {
				
				recommendation = (Recommendation) myAdpater.getItem(position);
				
				Toast.makeText(getApplicationContext(), (String)recommendation.getDescription(), 
						   Toast.LENGTH_LONG).show();
			}
		});
		
		Button createRecommendationBtn = (Button) findViewById(R.id.btn_create_new_recommendation);
		createRecommendationBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent registerRecommendationActivity = new Intent(getApplicationContext(),RegisterRecommendationActivity.class);
		    	registerRecommendationActivity.putExtra("user", user);
		    	startActivity(registerRecommendationActivity);
		    }
		});
	}

	public void populateList(List<Recommendation> recommendationList) {
		myAdpater = new ArrayAdapter<Recommendation>(self,
				android.R.layout.simple_list_item_1, recommendationList);
		listView.setAdapter(myAdpater);
	}
	
	public void toast(String string){
		Toast.makeText(getApplicationContext(),
				string,
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onBackPressed() {
	   super.onBackPressed();
	   this.finish();
    }
	
	class RecommendationAsyncTask extends AsyncTask<URL, Integer, Long> {

		RecommendationService recommendationService = new RecommendationController();

		protected Long doInBackground(URL... urls) {
			try {
				user = (User) (getIntent().getExtras().get("user"));
				recommendationList = recommendationService.getRecommendations(user);
				for(Recommendation r : recommendationList){
					System.out.println(r.toString());
				}
			} catch (ApplicationViewException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return 1L;
		}
		
		@Override
		protected void onPostExecute(Long result) {
			self.populateList(recommendationList);
		}
	}
	
//	class AddRecommendationtAsyncTask extends AsyncTask<JSONObject, Integer, Object> {
//
//		protected Object doInBackground(JSONObject... jsonObject) {
//			RecommendationService recommendationService = new RecommendationController();
//			try {
//				user = (User) (getIntent().getExtras().get("user"));
//				return recommendationService.addRecommendation(jsonObject[0], user);
//			} catch (ApplicationViewException e) {
//				return e;
//			}
//		}
//		
//		@Override
//		protected void onPostExecute(Object result) {
//			if(result instanceof Exception){
//				AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationListActivity.this);
//				builder.setMessage(((Exception)result).getMessage());
//				builder.setTitle(getString(R.string.error_hint));
//				AlertDialog dialog = builder.create();
//				dialog.show();
//			}else{
//				Toast.makeText(getApplicationContext(),
//						getString(R.string.subject_added_message), Toast.LENGTH_SHORT).show();
//			}
//		}
//	}
}
