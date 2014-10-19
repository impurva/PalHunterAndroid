package com.example.googlemapsapi;



import java.util.ArrayList;

import com.google.android.gcm.GCMRegistrar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	public static android.support.v4.app.FragmentManager fragmentManager;
	public static LocationManager locationManager ;
	private String savelocationname;
	public static Context maincontext;
	
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	//GSM
	// This is the project id generated from the Google console when
		// you defined a Google APIs project.
		private static final String PROJECT_ID = "839088080104";

		// This tag is used in Log.x() calls
		private static final String TAG = "MainActivity";

		// This string will hold the lengthy registration id that comes
		// from GCMRegistrar.register()
		private String regId = "";

		// These strings are hopefully self-explanatory
		private String registrationStatus = "Not yet registered";
		private String broadcastMessage = "No broadcast message";

		// This intent filter will be set to filter on the string "GCM_RECEIVED_ACTION"
		IntentFilter gcmFilter;
	
		// This broadcastreceiver instance will receive messages broadcast
		// with the action "GCM_RECEIVED_ACTION" via the gcmFilter
		
		// A BroadcastReceiver must override the onReceive() event.
		private BroadcastReceiver gcmReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				broadcastMessage = intent.getExtras().getString("gcm");

				if (broadcastMessage != null) {
					// display our received message
				//	tvBroadcastMessage.setText(broadcastMessage);
				// YOU WILL GET MESSAGE HERE.....!!!!!!!!!!!!!!!!!!!!!!!
					
					int duration = 3500;
					Toast.makeText(context, broadcastMessage, duration).show();
					Toast toast = Toast.makeText(context, broadcastMessage, duration);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					
				}
			}
		};
		
		private void sendRegistrationToServer() {
			// This is an empty placeholder for an asynchronous task to post the
			// registration
			// id and any other identifying information to your server.
		}
		
		// This registerClient() method checks the current device, checks the
		// manifest for the appropriate rights, and then retrieves a registration id
		// from the GCM cloud.  If there is no registration id, GCMRegistrar will
		// register this device for the specified project, which will return a
		// registration id.
		public void registerClient() {

			try {
				// Check that the device supports GCM (should be in a try / catch)
				GCMRegistrar.checkDevice(this);

				// Check the manifest to be sure this app has all the required
				// permissions.
				GCMRegistrar.checkManifest(this);

				// Get the existing registration id, if it exists.
				regId = GCMRegistrar.getRegistrationId(this);

				if (regId.equals("")) {

					registrationStatus = "Registering...";

			//		tvRegStatusResult.setText(registrationStatus);

					// register this device for this project
					GCMRegistrar.register(this, PROJECT_ID);
					regId = GCMRegistrar.getRegistrationId(this);

					registrationStatus = "Registration Acquired";

					// This is actually a dummy function.  At this point, one
					// would send the registration id, and other identifying
					// information to your server, which should save the id
					// for use when broadcasting messages.
					sendRegistrationToServer();

				} else {

					registrationStatus = "Already registered";

				}
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				registrationStatus = e.getMessage();
				
			}

			Log.d(TAG, registrationStatus);
			//tvRegStatusResult.setText(registrationStatus);
			
			// This is part of our CHEAT.  For this demo, you'll need to
			// capture this registration id so it can be used in our demo web
			// service.
			Log.d(TAG, regId);
			
		}	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		 fragmentManager = getSupportFragmentManager();
		 locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
		 maincontext=getApplicationContext();
		 
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
		
		//GCM receiver
		// Create our IntentFilter, which will be used in conjunction with a
				// broadcast receiver.
				gcmFilter = new IntentFilter();
				gcmFilter.addAction("GCM_RECEIVED_ACTION");

				registerClient();
		
	}

	// If our activity is paused, it is important to UN-register any
		// broadcast receivers.
		@Override
		protected void onPause() {
			
			unregisterReceiver(gcmReceiver);
			super.onPause();
		}
	
		// When an activity is resumed, be sure to register any
		// broadcast receivers with the appropriate intent
		@Override
		protected void onResume() {
			super.onResume();
			registerReceiver(gcmReceiver, gcmFilter);

		}
	
		
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			//fragment = new FindPeopleFragment();
			displaySaveLocation();
			break;
		case 2:
			//fragment = new PhotosFragment();
			break;
		case 3:
		//	fragment = new CommunityFragment();
			break;
		case 4:
			//fragment = new PagesFragment();
			break;
		case 5:
		//	fragment = new WhatsHotFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	
	public void displaySaveLocation(){
		
	
		// get prompts.xml view
		Context context = this;
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.savelocationdialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				// get user input and set it to result
				// edit text
			    	
			    	savelocationname=userInput.getText().toString();
			    	System.out.println(savelocationname);
			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
		
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
