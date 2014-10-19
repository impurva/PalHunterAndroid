package com.example.googlemapsapi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.app.Fragment;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class HomeFragment extends Fragment implements LocationListener{

private static View view;
/**
 * Note that this may be null if the Google Play services APK is not
 * available.
 */

private static GoogleMap googleMap;
private static Double latitude, longitude;
private LatLng current;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    if (container == null) {
        return null;
    }
    view = (RelativeLayout) inflater.inflate(R.layout.fragment_home, container, false);
    // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
            latitude = 26.78;
            longitude = 72.56;

            setUpMapIfNeeded(); // For setting up the MapFragment

    return view;
}

/***** Sets up the map if it is possible to do so *****/
private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (googleMap == null) {
        // Try to obtain the map from the SupportMapFragment.
    	googleMap = ((SupportMapFragment) MainActivity.fragmentManager
                .findFragmentById(R.id.location_map)).getMap();
        // Check if we were successful in obtaining the map.
        if (googleMap != null)
            setUpMap();
    }
}

/**
 * This is where we can add markers or lines, add listeners or move the
 * camera.
 * <p>
 * This should only be called once and when we are sure that {@link #mMap}
 * is not null.
 */
private void setUpMap() {
    // For showing a move to my loction button
	googleMap.setMyLocationEnabled(true);
    // For dropping a marker at a point on the Map
  //  mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
    // For zooming automatically to the Dropped PIN Location
   // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
   //         longitude), 12.0f));
    
 // Enabling MyLocation Layer of Google Map
    googleMap.setMyLocationEnabled(true);

          
    // Getting LocationManager object from System Service LOCATION_SERVICE
    LocationManager locationManager = MainActivity.locationManager;

    // Creating a criteria object to retrieve provider
    Criteria criteria = new Criteria();

    // Getting the name of the best provider
    String provider = locationManager.getBestProvider(criteria, true);

    // Getting Current Location
    Location location = locationManager.getLastKnownLocation(provider);
    if(location!=null)
    {
    	current = new LatLng(location.getLatitude(), location.getLongitude());
    	onLocationChanged(location);
    }
    locationManager.requestLocationUpdates(provider, 20000, 0,this);
}

private void addLine(LatLng loc) {

	googleMap
			.addPolyline((new PolylineOptions())
					.add(current,loc).width(5).color(Color.BLUE)
					.geodesic(true));
	// move camera to zoom on map
	googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,13));
	current=loc;
}
public void onLocationChanged(Location location) {

    //TextView tvLocation = (TextView) findViewById(R.id.tv_location);

    // Getting latitude of the current location
    double latitude = location.getLatitude();

    // Getting longitude of the current location
    double longitude = location.getLongitude();

    // Creating a LatLng object for the current location
    LatLng latLng = new LatLng(latitude, longitude);
    addLine(latLng);
    sendCurrentLocation(latLng);
    // Showing the current location in Google Map
  //  googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    // Zoom in the Google Map
    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    // Setting latitude and longitude in the TextView tv_location
   // tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );

}

private void sendCurrentLocation(LatLng latlng) {
	// TODO Auto-generated method stub
	new sendCurrentLocationAsyncTask ().execute(latlng.latitude,latlng.longitude);
	
}
private class sendCurrentLocationAsyncTask extends AsyncTask <Double,Double,Void> {

	@Override
	protected Void doInBackground(Double... arg) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		Long current_time=getCurrentTimestamp();
		HttpGet httpGet = new HttpGet("http://localhost:8080/Hello/api/controller/setcurrentlocation?long=" +arg[0]+"&lat="+arg[1]+"&uid=12323" + "&time=" + current_time);
		//String text = null;
		try {
		HttpResponse response = httpClient.execute(httpGet, localContext);


		HttpEntity entity = response.getEntity();


		//text = getASCIIContentFromEntity(entity);


		} catch (Exception e) {
	//	return e.getLocalizedMessage();
		}
		return null;


		//return "";
	}
	
}

private void getTrajectory(String userid)
{
	new getTrajectoryAsyncTask ().execute(userid);
}

private class getTrajectoryAsyncTask extends AsyncTask <String, String, Void> {
	
	@Override
	protected Void doInBackground(String... arg) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet("http://localhost:8080/Hello/api/controller/gettrajectory?uid=8989");
		//String text = null;
		try {
		HttpResponse response = httpClient.execute(httpGet, localContext);


		HttpEntity entity = response.getEntity();


		//text = getASCIIContentFromEntity(entity);


		} catch (Exception e) {
	//	return e.getLocalizedMessage();
		}

		return null;
		
	}
	
}

/*@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}*/

private static Long getCurrentTimestamp()
{
	return System.currentTimeMillis();
}

@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    if (googleMap != null)
        setUpMap();

    if (googleMap == null) {
        // Try to obtain the map from the SupportMapFragment.
    	googleMap = ((SupportMapFragment) MainActivity.fragmentManager
                .findFragmentById(R.id.location_map)).getMap();
        // Check if we were successful in obtaining the map.
        if (googleMap != null)
            setUpMap();
    }
}

/**** The mapfragment's id must be removed from the FragmentManager
 **** or else if the same it is passed on the next time then 
 **** app will crash ****/
@Override
public void onDestroyView() {
    super.onDestroyView();
    if (googleMap != null) {
    	
        MainActivity.fragmentManager.beginTransaction()
            .remove(MainActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
        googleMap = null;
    }
}

@Override
public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}
}