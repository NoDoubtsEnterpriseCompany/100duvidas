package com.nodoubts;

import java.util.Date;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nodoubts.core.Lecture;
import com.nodoubts.core.ModuleFactory;
import com.nodoubts.core.ScheduledLecture;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.fragments.CalendarFragmentDialog;
import com.nodoubts.fragments.CalendarFragmentDialog.GetDateListener;
import com.nodoubts.serverclient.lecture.LectureController;
import com.nodoubts.serverclient.lecture.LectureService;

public class ScheduleLectureActivity extends FragmentActivity implements GetDateListener{

	private Lecture lecture;
	private TextView dateView;
	private Date selectedData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    this.setContentView(R.layout.schedule_lecture_layout);
	    
	    
	    ImageView lectureImg = (ImageView) findViewById(R.id.lecture_imageview);
	    TextView professorName = (TextView) findViewById(R.id.professor_name_textview);
	    TextView addressTextView = (TextView) findViewById(R.id.address_textview);
	    TextView subjectName = (TextView) findViewById(R.id.subject_name_textview);
	    TextView priceView = (TextView) findViewById(R.id.price_textview);
	    dateView = (EditText) findViewById(R.id.date_textfield);
	    ImageButton dateButton = (ImageButton) findViewById(R.id.calendar_picker_btn);
	    TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);
	    Button confirmBtn = (Button) findViewById(R.id.confirm_btn);
	    
	    
	    lecture = (Lecture) getIntent().getExtras().get("lecture");
	    if(lecture!=null){
	    	
	    	professorName.setText(lecture.getTeacher().getName());
	    	addressTextView.setText(lecture.getAddress());
	    	subjectName.setText("SubjectName");
	    	priceView.setText(String.valueOf(lecture.getPrice()));
	    	
	    	dateButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CalendarFragmentDialog calendarDialog = new CalendarFragmentDialog();
					Bundle arguments = new Bundle();
					arguments.putSerializable("intentObj", lecture);
					calendarDialog.setArguments(arguments);
					calendarDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
					calendarDialog.show(getFragmentManager(),"DatePicker");
				}
			});
	    	
			confirmBtn.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					AsyncTask<
					ScheduledLecture, Void, Object> scheduleLecture = new ScheduleLectureTask();
					
					if(selectedData==null){
						Toast.makeText(
								ScheduleLectureActivity.this, getString(R.string.empty_date_hint), Toast.LENGTH_SHORT).show();
					}else{
						ScheduledLecture scheduledLecture = ModuleFactory.createScheduledFactory(
								lecture.getSubject(), lecture.getTeacher(), lecture.getPrice(),
								selectedData, lecture.getAddress(), HomeActivity.user.get_id());
						scheduleLecture.execute(scheduledLecture);
					}
				}
			});
			
	    }
	}
	
	private class ScheduleLectureTask extends AsyncTask<ScheduledLecture, Void, Object>{

		ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(ScheduleLectureActivity.this);
			this.progressDialog.setProgressStyle(progressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			this.progressDialog.show();
		}
		
		@Override
		protected Object doInBackground(ScheduledLecture... params) {
			LectureService lectureController = new LectureController();
			try {
				lectureController.scheduleLecture(params[0]);
				return params[0];
			} catch (ApplicationViewException e) {
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			progressDialog.dismiss();
			Toast.makeText(ScheduleLectureActivity.this, getString(R.string.lecture_scheduled_hint), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onFinishDatePicker(long date, Object lecture) {
		selectedData = new Date(date);
		dateView.setText(selectedData.toString());
		this.lecture = (Lecture)lecture;
		this.lecture.setDate(selectedData);
	}
}
