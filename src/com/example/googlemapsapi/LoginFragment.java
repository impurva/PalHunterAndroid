package com.example.googlemapsapi;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.model.*;

import android.content.Intent;


public class LoginFragment extends Fragment {
	
	private static final String TAG = "LoginFragment";
	public final static String EXTRA_MESSAGE = "com.example.googlemapsapi.MESSAGE";
	
	private UiLifecycleHelper uiHelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_login, container, false);

	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    authButton.setReadPermissions(Arrays.asList("user_likes", "user_status", "user_friends"));
	    return view;
	}
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        Request.newMeRequest(session, new Request.GraphUserCallback() {

	        	  // callback after Graph API response with user object
				@Override
				public void onCompleted(GraphUser user, Response response) {
					// TODO Auto-generated method stub
					if(user!=null)
					{
						System.out.println(user.getId());
						Intent intent = new Intent(getActivity(), MainActivity.class);
						intent.putExtra(EXTRA_MESSAGE, user.getId());
					    startActivity(intent);
						//System.out.println(user.getUsername());
					}
					
				}
	        	}).executeAsync();
	        new Request(
	        	    session,
	        	    "/me/friends",
	        	    null,
	        	    HttpMethod.GET,
	        	    new Request.Callback() {
	        	        public void onCompleted(Response response) {
	        	            /* handle the result */
	        	        	System.out.println("I have reached here!! Today's greatest achievement !!! Eureka!!!");
	        	        	try {
	        	        		JSONArray obj=(JSONArray) response.getGraphObject().getInnerJSONObject().get("data");
	        	        		System.out.println(obj.getJSONObject(0).get("name"));
								System.out.println(response.getGraphObject().getInnerJSONObject().get("data"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	        	        }
	        	    }
	        	).executeAsync();
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
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
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
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
