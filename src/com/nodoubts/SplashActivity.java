package com.nodoubts;

import com.facebook.Session;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class SplashActivity extends FragmentActivity implements Runnable {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View fbLoginFrag = (View) findViewById(R.id.FbLoginFragment);
        fbLoginFrag.setVisibility(View.INVISIBLE);
        
        if (savedInstanceState == null) {
			getSupportFragmentManager()
			.beginTransaction()
			.commit();
		}
        
        Handler handler = new Handler();
        handler.postDelayed(this, 3000);
        
    }

    public void run(){
    	Session session = Session.getActiveSession();
    	if((session!=null && !session.isClosed() && !session.isOpened())
    			|| session==null){
    		startActivity(new Intent(this, MainActivity.class));
    	}
    }
}