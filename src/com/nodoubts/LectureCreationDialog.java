package com.nodoubts;

import com.nodoubts.core.User;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

public class LectureCreationDialog extends DialogFragment {
	
	NumberPicker np;
	
	public interface EditLectureListener{
		void onFinishEditDialog(int price, User user);
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
        View view = inflater.inflate(R.layout.create_lecture_dialog, container, false);
        
        np = (NumberPicker) view.findViewById(R.id.price_picker);
        np.setMinValue(1);
        np.setMaxValue(2000);

        Button okBtn = (Button) view.findViewById(R.id.create_lecture_ok);
        Button cancelBtn = (Button) view.findViewById(R.id.create_lecture_cancel);
        
        okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditLectureListener activity = (EditLectureListener) getActivity();
				activity.onFinishEditDialog(np.getValue(), (User) getArguments().get("user"));
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
