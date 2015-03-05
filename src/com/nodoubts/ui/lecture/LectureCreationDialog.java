package com.nodoubts.ui.lecture;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nodoubts.R;
import com.nodoubts.core.User;

public class LectureCreationDialog extends DialogFragment {

	EditText np;
	EditText address;

	public interface EditLectureListener {
		void onFinishEditDialog(Double price, String address, User user);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.create_lecture_dialog, container,
				false);

		np = (EditText) view.findViewById(R.id.price_picker);
		np.requestFocus();

		address = (EditText) view.findViewById(R.id.address);

		Button okBtn = (Button) view.findViewById(R.id.create_lecture_ok);
		Button cancelBtn = (Button) view
				.findViewById(R.id.create_lecture_cancel);

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditLectureListener activity = (EditLectureListener) getActivity();
				double price = 0;
				if (!np.getText().toString().isEmpty()) {
					price = Double.valueOf(np.getText().toString());
					activity.onFinishEditDialog(price, address.getText()
							.toString(), (User) getArguments().get("user"));
					dismiss();
				}else{
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.invalidPrice), Toast.LENGTH_SHORT).show();
				}
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
