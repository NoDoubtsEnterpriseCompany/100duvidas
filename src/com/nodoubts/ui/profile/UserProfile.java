package com.nodoubts.ui.profile;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.Session;
import com.nodoubts.CalendarActivity;
import com.nodoubts.MainActivity;
import com.nodoubts.R;
import com.nodoubts.core.User;
import com.nodoubts.ui.grouplecture.GroupLectureActivity;
import com.nodoubts.ui.grouplecture.RegisterGroupLectureActivity;
import com.nodoubts.ui.recommendation.RecommendationListActivity;
import com.nodoubts.serverclient.util.SessionManager;
import com.nodoubts.ui.search.SearchActivity;
import com.nodoubts.ui.subject.SubjectListActivity;
import com.nodoubts.ui.util.CircularImageView;
import com.nodoubts.ui.util.CustomDrawerAdapter;
import com.nodoubts.ui.util.DrawerItem;

public class UserProfile extends FragmentActivity {

	private static final int IMG_BORDER = 6;
	private TextView name, city;
	public static User user; //TODO: badsmell thereis static access to this field. Remove it.
	private CircularImageView profilePicture;
	private ProgressBar waitSpinner;
	private RatingBar rating;
	private ImageButton scheduleBtn, searchBtn;
	private List<DrawerItem> userOptions;
	private DrawerLayout optsDrawerLayout;
	private ListView optsDrawerList;
	private ActionBarDrawerToggle optsDrawerToggle;
	private SessionManager sessionManager;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);

		final ActionBar actionBar = getActionBar();
		waitSpinner = (ProgressBar) findViewById(R.id.singleSpinner);
		profilePicture = (CircularImageView) findViewById(R.id.profile_pic);
		name = (TextView) findViewById(R.id.profile_username_textview);
		city = (TextView) findViewById(R.id.profile_city_textview);
		rating = (RatingBar) findViewById(R.id.profile_rating);
		scheduleBtn = (ImageButton) findViewById(R.id.profile_schedule_btn);
		searchBtn = (ImageButton) findViewById(R.id.profile_search_btn);
		sessionManager = new SessionManager(getApplicationContext());
		context = this;
		rating.setEnabled(false);
		user = (User) getIntent().getSerializableExtra("user");

		userOptions = new ArrayList<DrawerItem>();
		addUserOptions();
		
		optsDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		optsDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		profilePicture.setBorderColor(Color.WHITE);
		profilePicture.setBorderWidth(IMG_BORDER);

		View mActionBarView = getLayoutInflater().inflate(R.layout.action_bar_custom, null);
		actionBar.setCustomView(mActionBarView);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayShowCustomEnabled(true);


		optsDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				optsDrawerLayout,         /* DrawerLayout object */
				R.drawable.draw_ic,  /* nav drawer image to replace 'Up' caret */
				R.string.opts_open,  /* "open drawer" description for accessibility */
				R.string.empty  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				//getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		optsDrawerLayout.setDrawerListener(optsDrawerToggle);

		CustomDrawerAdapter adapter = new CustomDrawerAdapter(context, R.layout.custom_drawer_item, userOptions);
		
		// Set the adapter for the list view
		optsDrawerList.setAdapter(adapter);
		// Set the list's click listener
		optsDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		scheduleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent calendarActivity = new Intent(context,
						CalendarActivity.class);
				startActivity(calendarActivity);
			}
		});

		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchActivity = new Intent(context,
						SearchActivity.class);
				startActivity(searchActivity);
			}
		});

		updateData();
	}

	private void addUserOptions() {
		userOptions.add(new DrawerItem(context.getString(R.string.opts_add_subject), android.R.drawable.ic_menu_add));
		userOptions.add(new DrawerItem(context.getString(R.string.opts_add_grouplecture), android.R.drawable.ic_menu_add));
		userOptions.add(new DrawerItem(getString(R.string.opts_recommend), android.R.drawable.ic_menu_add));
		userOptions.add(new DrawerItem(context.getString(R.string.opts_edit_profile), android.R.drawable.ic_menu_edit));
		userOptions.add(new DrawerItem(context.getString(R.string.opts_logout), android.R.drawable.ic_menu_close_clear_cancel));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				User newUser = (User) data.getSerializableExtra("result");
				this.user = newUser;
				updateData();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}
	
	private void updateData() {
		if (user != null) {
			rating.setRating(user.getProfile().getTotalScore());
			name.setText(user.getProfile().getName());
			city.setText(user.getProfile().getCity());
			name.refreshDrawableState();
			rating.refreshDrawableState();
			city.refreshDrawableState();
			if (user.getProfile().getProfilePic() != null) {
				SetProfilePicture setPictureTask = new SetProfilePicture();
				setPictureTask.execute(user.getProfile().getProfilePic());
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		optsDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		optsDrawerToggle.onConfigurationChanged(newConfig);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (optsDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	private class SetProfilePicture extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected void onPreExecute() {
			waitSpinner.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
		}

		protected Bitmap doInBackground(String... params) {
			String picUrl = params[0];
			Bitmap bitmap = null;
			String picWidth = String.valueOf(profilePicture.getDrawable()
					.getIntrinsicWidth() - IMG_BORDER);
			String picHeight = String.valueOf(profilePicture.getDrawable()
					.getIntrinsicHeight() - IMG_BORDER);
			List<NameValuePair> queryParams = new LinkedList<NameValuePair>();
			queryParams.add(new BasicNameValuePair("width", picWidth));
			queryParams.add(new BasicNameValuePair("height", picHeight));
			String encodedParams = URLEncodedUtils.format(queryParams, "utf-8");
			String picQuery = picUrl.concat("?").concat(encodedParams);

			try {
				URL url = new URL(picQuery);
				HttpGet httpRequest = null;

				httpRequest = new HttpGet(url.toURI());

				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient
						.execute(httpRequest);

				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bEntity = new BufferedHttpEntity(entity);
				InputStream input = bEntity.getContent();

				bitmap = BitmapFactory.decodeStream(input);

			} catch (Exception ex) {
				Log.e("UserProfile", ex.getMessage());
			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			waitSpinner.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			if (result != null) {
				profilePicture.setImageBitmap(result);
			}
		}
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@SuppressWarnings("rawtypes") 
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectItem(position);
		}

		private void selectItem(int position) {
			Intent activity;
			switch(position){
			case 0:
				activity = new Intent(context, SubjectListActivity.class);
				activity.putExtra("user", user);
				startActivity(activity);
				break;
			case 1:
				activity = new Intent(context, RegisterGroupLectureActivity.class);
				activity.putExtra("user", user);
				startActivity(activity);
				break;
			case 2:
				activity = new Intent(context, RecommendationListActivity.class);
				activity.putExtra("user", user);
				startActivity(activity);
				break;
			case 3:
				activity = new Intent(context, EditProfileActivity.class);
				activity.putExtra("user", user);
				startActivityForResult(activity, 1);
				break;
			case 4:
				logout();
				break;
			}

			optsDrawerList.setItemChecked(position, true);
			optsDrawerLayout.closeDrawer(optsDrawerList);
		}

		private void logout() {
			Session.getActiveSession().close();
	    	sessionManager.logoutUser();
    		finish();
		}
	}
}