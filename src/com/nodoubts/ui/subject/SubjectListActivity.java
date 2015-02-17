package com.nodoubts.ui.subject;

import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nodoubts.R;
import com.nodoubts.core.Subject;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.subject.SubjectController;
import com.nodoubts.serverclient.subject.SubjectService;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.user.UserService;
import com.nodoubts.ui.lecture.LectureCreationDialog;
import com.nodoubts.ui.lecture.LectureCreationDialog.EditLectureListener;
import com.nodoubts.ui.rating.RegisterGroupLectureActivity;

public class SubjectListActivity extends FragmentActivity implements EditLectureListener {

	private SubjectListActivity self;

	ListView listView;
	
	ListAdapter myAdpater;
	
	User user;
	Subject subject;
	
	LectureCreationDialog lectureDialog;
	
	boolean isGroupLectureCreation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.activity_subject_list);
		
		new SubjectAsyncTask().execute();

		this.isGroupLectureCreation = getIntent().getBooleanExtra("isGroupLectureCreation", false);
		
		listView = (ListView) findViewById(R.id.listView_subjectList);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					final int position, long id) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						self);
				
				subject = (Subject) myAdpater.getItem(position);
				
				lectureDialog = new LectureCreationDialog();
				lectureDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
				lectureDialog.setArguments(getIntent().getExtras());
				
				if(getIntent().getSerializableExtra("user") != null ){
					user = (User) getIntent().getSerializableExtra("user");
				}

				lectureDialog.show(getFragmentManager(), "Teste");
				
				if (!isGroupLectureCreation) {
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					// show it
					alertDialog.show();
				} else {
					Intent resultIntent = new Intent();
					resultIntent.putExtra(RegisterGroupLectureActivity.SUBJECT_GROUP_IDENTIFIER, subject);
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				}
			}
		});
		
		Button createSubjectBtn = (Button) findViewById(R.id.btn_create_new_subject);
		createSubjectBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent addSubjectScreen = new Intent(getApplicationContext(),RegisterSubjectActivity.class);
		    	addSubjectScreen.putExtra("user", user);
		    	addSubjectScreen.putExtra("isGroupLectureCreation", isGroupLectureCreation);
		    	startActivity(addSubjectScreen);
		    }
		});
	}

	public void populateList(List<Subject> subjectList) {
		myAdpater = new ArrayAdapter<Subject>(self,
				android.R.layout.simple_list_item_1, subjectList);
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
	

	@Override
	public void onFinishEditDialog(Double price, String address, User user) {
		
		lectureDialog.dismiss();
		
		JsonObject jsonTransaction = new JsonObject();
		jsonTransaction.addProperty("subject_id", subject.getId());
		jsonTransaction.addProperty("username", user.getUsername());
		jsonTransaction.addProperty("price", price);
		jsonTransaction.addProperty("address", address);
		
		new AddSubjectAsyncTask().execute(jsonTransaction);
	}

	class SubjectAsyncTask extends AsyncTask<URL, Integer, Long> {

		SubjectService subjectService = new SubjectController();
		List<Subject> subjectList;

		protected Long doInBackground(URL... urls) {
			try {
				subjectList = subjectService.getSubjects();
			} catch (ApplicationViewException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return 1L;
		}
		
		@Override
		protected void onPostExecute(Long result) {
			self.populateList(subjectList);
		}
	}
	
	class AddSubjectAsyncTask extends AsyncTask<JsonObject, Integer, Object> {

		protected Object doInBackground(JsonObject... jsonObject) {
			UserService userService = new UserController();
			try {
				return userService.addSubjectToUser(jsonObject[0].toString());
			} catch (ApplicationViewException e) {
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if(result instanceof Exception){
				AlertDialog.Builder builder = new AlertDialog.Builder(SubjectListActivity.this);
				builder.setMessage(((Exception)result).getMessage());
				builder.setTitle(getString(R.string.error_hint));
				AlertDialog dialog = builder.create();
				dialog.show();
			}else{
				Toast.makeText(getApplicationContext(),
						getString(R.string.subject_added_message), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
