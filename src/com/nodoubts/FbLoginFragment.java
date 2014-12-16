package com.nodoubts;

import java.util.Arrays;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.nodoubts.core.Profile;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.user.UserController;

/**
 * Created by jeymisson on 12/4/14.
 */
public class FbLoginFragment extends Fragment {
    private static final String TAG = "FbLoginFragment";
    private UiLifecycleHelper uiHelper;

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
        View view = inflater.inflate(R.layout.fbutton_layout, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        authButton.setFragment(this);
        return view;
    }

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");  
            	Request.newMeRequest(session, new GraphUserCallback() {
				
				@Override
				public void onCompleted(final GraphUser fbUser, Response response) {	
					AsyncTask<Void, Void , Object> task = new AsyncTask<Void, Void, Object>() {
						@Override
						protected Object doInBackground(Void... arg0) {
							User user;
							UserController userController = new UserController();
							String fbUserEmail = (String) fbUser.getProperty("email");
							try {
								user = userController.findUserByEmail(fbUserEmail);
							} catch (ApplicationViewException e) {
								user = new User();
								user.setUsername(fbUserEmail);
								user.setEmail(fbUserEmail);
								user.setPassword(String.valueOf(fbUserEmail.hashCode())); //TODO: Improve this
								Profile profile = new Profile();
								profile.setName(fbUser.getName());
								profile.setProfilePic((String)fbUser.getProperty("picture.url"));
								user.setProfile(profile);
								Log.i(TAG, profile.getProfilePic());
								
								try {
									userController.saveUser(user);
								} catch (ApplicationViewException e1) {
									return e1;
								}
							}
							return user;
						}
						protected void onPostExecute(Object result) {
							if(result instanceof User){
								Intent homeScreen = new Intent(
										getActivity().getApplicationContext(),HomeActivity.class);
								homeScreen.putExtra("user", (User)result);
								startActivity(homeScreen);
							
							}else if(result instanceof Exception){
								AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
								builder.setMessage(((Exception)result).getMessage());
								builder.setTitle("Error");
								AlertDialog dialog = builder.create();
								dialog.show();
							}
					    }
					};	
				task.execute();
				}
			}).executeAsync();
            	
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
}

