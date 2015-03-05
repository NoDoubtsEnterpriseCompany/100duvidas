package com.nodoubts.ui.fragments;

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
import com.nodoubts.R;
import com.nodoubts.core.Profile;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.util.SessionManager;

/**
 * Created by jeymisson on 12/4/14.
 */
public class FbLoginFragment extends Fragment {
    private static final String TAG = "FbLoginFragment";
    private UiLifecycleHelper uiHelper;
    private Session mSession;
    private SessionManager sessionManager;
    
    public interface FbLoginCallback{
    	public void fbLoggedIn(User user);
    	public void fbLoggedOut();
    }
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
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
		if(!sessionManager.isLoggedIn()){
	        if (state.isOpened()) {
	        	if(mSession==null || isSessionChanged(session)){
	        		mSession = session;
		            	Request.newMeRequest(session, new GraphUserCallback() {				
						@Override
						public void onCompleted(GraphUser fbUser, Response response) {
							LoginTask fbLoginTask = new LoginTask();
							fbLoginTask.execute(fbUser);
						}
					}).executeAsync();
	        	}
	        }
		}
    }
	
    
    private boolean isSessionChanged(Session session) {

        // Check if session state changed
        if (mSession.getState() != session.getState())
            return true;

        // Check if accessToken changed
        if (mSession.getAccessToken() != null) {
            if (!mSession.getAccessToken().equals(session.getAccessToken()))
                return true;
        }
        else if (session.getAccessToken() != null) {
            return true;
        }

        // Nothing changed
        return false;
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
    
    private class LoginTask extends AsyncTask<GraphUser, Void, Object>{
		@Override
		protected Object doInBackground(GraphUser... arg0) {
			GraphUser fbUser = arg0[0];
			User user;
			UserController userController = new UserController();
			String fbUserEmail = (String) fbUser.getProperty("email");
			try {
				user = userController.findUserByEmail(fbUserEmail);
			}catch (ApplicationViewException e) {
				user = new User();
				user.setUsername(fbUserEmail);
				user.setEmail(fbUserEmail);
				user.setPassword(String.valueOf(fbUserEmail.hashCode())); //TODO: Improve this
				Profile profile = new Profile();
				profile.setName(fbUser.getName());
				profile.setProfilePic("https://graph.facebook.com/"+fbUser.getId()+"/picture");
				user.setProfile(profile);
				try {
					userController.saveUser(user);
				} catch (ApplicationViewException e1) {
					Log.i(TAG, "Error: " +e1.getMessage());
					return e1;
				}
			}
			return user;
		}
		@Override
		protected void onPostExecute(Object result) {
			if(result instanceof User){
				FbLoginCallback fbLoginCallback = (FbLoginCallback) getActivity();
				fbLoginCallback.fbLoggedIn((User)result);
			
			}else if(result instanceof Exception){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(((Exception)result).getMessage());
				builder.setTitle("Error");
				AlertDialog dialog = builder.create();
				dialog.show();
			}
	    }
	}
}