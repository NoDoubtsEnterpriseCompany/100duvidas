package com.nodoubts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.facebook.Session;
import com.nodoubts.R;
import com.nodoubts.core.User;
import com.nodoubts.ui.fragments.FbLoginFragment;
import com.nodoubts.ui.fragments.FbLoginFragment.FbLoginCallback;

public class SplashActivity extends FragmentActivity implements Runnable, FbLoginCallback{
	
	Fragment fbLoginFrag;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
    	if((session!=null && !session.isClosed() && !session.isOpened())
    			|| session==null){
    		getSupportFragmentManager().beginTransaction().remove(fbLoginFrag).commit();
    		getSupportFragmentManager().popBackStack();
    		Intent mainActivityIntent = new Intent(this, MainActivity.class);
    		mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(mainActivityIntent);
    		finish();
    	}
    }

	@Override
	public void fbLoggedIn(User user) {
		Intent homeScreen = new Intent(this, HomeActivity.class); 
		homeScreen.putExtra("user", user);
		startActivity(homeScreen);
		finish();
	}

	@Override
	public void fbLoggedOut() {
	}
}