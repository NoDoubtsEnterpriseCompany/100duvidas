package com.nodoubts.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.nodoubts.R;

public class CalendarFragmentDialog extends DialogFragment {
	
	CalendarView calendarView;
	
	public interface GetDateListener{
		void onFinishDatePicker(long date, Object obj);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_dialog, container, false);
        
        calendarView = (CalendarView) view.findViewById(R.id.calendarView1);
        calendarView.getDate();
        
        Button okBtn = (Button) view.findViewById(R.id.ok_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        
        okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GetDateListener activity = (GetDateListener) getActivity();
				activity.onFinishDatePicker(calendarView.getDate(), getArguments().get("intentObj"));
				dismiss();
			}
		});
        
        cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
        
        return view;
    }

}