package com.nodoubts;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.google.gson.JsonObject;
import com.nodoubts.core.User;
import com.nodoubts.serverclient.ServerController;
import com.nodoubts.serverclient.ServerService;
import com.nodoubts.serverclient.user.UserController;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jeymisson on 12/4/14.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private UiLifecycleHelper uiHelper;
    UserController userController;
	ServerService serverController;
	EditText userNameEditText;
	EditText passEditText;
	Button loginBtn;
	Button registerBtn;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        
        userController = new UserController();
		serverController= new ServerController();
		userNameEditText = (EditText) view.findViewById(R.id.email);
		passEditText = (EditText) view.findViewById(R.id.passwordEditText);
		loginBtn = (Button) view.findViewById(R.id.loginbtn);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
		    	new LoginAsyncTask().execute();
		    }
		});
		    
		registerBtn = (Button) view.findViewById(R.id.registerbtn);
		
		registerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(this, RegisterUserActivity.class);
				startActivity(intent);
			}
		});

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
        authButton.setFragment(this);
        return view;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    
    private class LoginAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			JsonObject jsonTransaction = new JsonObject();
			jsonTransaction.addProperty("login", userNameEditText.getText()
					.toString());
			jsonTransaction.addProperty("password", passEditText.getText()
					.toString());

			String response = userController.authenticateUser(jsonTransaction
					.toString());
			int resultCode = -1;
			try {
				JSONObject jsonResponse = new JSONObject(response);
				JSONObject code = new JSONObject(
						jsonResponse.getString("result"));
				resultCode = code.getInt("code");

			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (resultCode == 200) {
				User user = userController.findUser(userNameEditText.getText().toString());
				Intent profileScreen = new Intent(getApplicationContext(),TeacherProfileActivity.class);
				profileScreen.putExtra("user", user);
				startActivity(profileScreen);
			}
			return response;
		}
	}
}

