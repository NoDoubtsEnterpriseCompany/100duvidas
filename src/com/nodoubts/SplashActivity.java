package com.nodoubts;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.facebook.Session;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;
import com.nodoubts.serverclient.user.UserController;
import com.nodoubts.serverclient.util.SessionManager;
import com.nodoubts.ui.fragments.FbLoginFragment;
import com.nodoubts.ui.fragments.FbLoginFragment.FbLoginCallback;
import com.nodoubts.ui.profile.UserProfile;

public class SplashActivity extends FragmentActivity implements Runnable, FbLoginCallback{
	
	private SessionManager sessionManager;
	private UserController userController;
	private Context context;
	Fragment fbLoginFrag;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;
        userController = new UserController();
        sessionManager = new SessionManager(getApplicationContext());
        fbLoginFrag = new FbLoginFragment();
        
        if (savedInstanceState == null) {
			getSupportFragmentManager()
			.beginTransaction().add(fbLoginFrag, "fbLoginFrag")
			.disallowAddToBackStack().commit();
		}

        Handler handler = new Handler();
        handler.postDelayed(this, 3000);
        
    }

    public void run(){
    	Session session = Session.getActiveSession();
    	
    	if(sessionManager.isLoggedIn()){
			getUserAsyncTask().execute();
		}
    	else if((session!=null && !session.isOpened())
    			|| session==null){
    		getSupportFragmentManager().beginTransaction().remove(fbLoginFrag).commit();
    		getSupportFragmentManager().popBackStack();
    		Intent mainActivityIntent = new Intent(this, MainActivity.class);
    		mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(mainActivityIntent);
    		finish();
    	}
    }
    

	private AsyncTask<String, Void, Object> getUserAsyncTask() {
		return new AsyncTask<String, Void, Object>(){

			@Override
			protected Object doInBackground(String... params) {
				try {
					User user = userController.findUser(
							sessionManager.getUserDetails().get(
									sessionManager.KEY_LOGIN));
					return user;
				} catch (ApplicationViewException e) {
					return e;
				}
			}
			
			@Override
			protected void onPostExecute(Object result) {
				if(result instanceof User){
					Intent homeScreen = new Intent(context, UserProfile.class);
					homeScreen.putExtra("user", (User)result);
					startActivity(homeScreen);
				}
			}
		};
	}

	@Override
	public void fbLoggedIn(User user) {
		sessionManager.createLoginSession(Session.getActiveSession().getAccessToken(), user.getEmail());
		
		if(sessionManager.isLoggedIn()){
			Intent homeScreen = new Intent(this, UserProfile.class);
			homeScreen.putExtra("user", user);
			homeScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			startActivity(homeScreen);
			finish();
		}
	}

	@Override
	public void fbLoggedOut() {
		sessionManager.logoutUser();
	}
}