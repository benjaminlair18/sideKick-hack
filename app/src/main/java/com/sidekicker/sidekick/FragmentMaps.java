package com.sidekicker.sidekick;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by root on 4/16/16.
 */
public class FragmentMaps
		extends Fragment
		implements GoogleMap.OnMarkerClickListener,
		           OnMapReadyCallback,
		           GoogleMap.OnMyLocationButtonClickListener,
		           MapView.OnClickListener,
		           ActivityCompat.OnRequestPermissionsResultCallback,
		           GoogleMap.OnMapLongClickListener,
		           GoogleMap.OnMapClickListener
{
	public static GoogleMap mGoogleMaps = null;
	private MapView mMapView;
	private Marker mSydney;
	private UiSettings mUiSettings;
	private Marker mLastSelectedMarker;
	private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	private boolean mPermissionDenied = false;


	class PopupAdapter implements GoogleMap.InfoWindowAdapter {
		LayoutInflater inflater = null;

		PopupAdapter(LayoutInflater inflater) {
			this.inflater=inflater;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return(null);
		}

		@Override
		public View getInfoContents(Marker marker) {
			View popup=inflater.inflate(R.layout.pop_up, null);

			TextView tv=(TextView)popup.findViewById(R.id.title);

			tv.setText(marker.getTitle());
			tv=(TextView)popup.findViewById(R.id.snippet);
			tv.setText(marker.getSnippet());

			return(popup);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_maps, null);

		try
		{
			MapsInitializer.initialize(getActivity());
		}
		catch (Exception e)
		{
			Log.e("Address Map", "Could not initialize google play", e);
		}

		mMapView = (MapView) view.findViewById(R.id.maps_view);
		mMapView.getMapAsync(this);

		mMapView.onCreate(savedInstanceState);

		initListeners();

		return view;
	}

	private void initListeners()
	{
		if (mGoogleMaps != null)
		{
			mGoogleMaps.setOnMarkerClickListener(this);
			mGoogleMaps.setOnMyLocationButtonClickListener(this);
			mGoogleMaps.setOnMapLongClickListener(this);
			mGoogleMaps.setOnMapClickListener(this);
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		mGoogleMaps = googleMap;
		//		Add a marker in Sydney and move the camera

		initListeners();
		enableMyLocation();

		mUiSettings = mGoogleMaps.getUiSettings();
		mUiSettings.setZoomControlsEnabled(true);
	}

	private void enableMyLocation()
	{
		if (mGoogleMaps != null)
		{
			// Access to the location has been granted to the app.
			if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			{
				ActivityCompat.requestPermissions(this.getActivity(),
				                                  new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
				                                  MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
				return;
			}
			mGoogleMaps.setMyLocationEnabled(true);
			SetUpMap();
		}
	}

	private void SetUpMap()
	{
		try
		{
			LocationManager locationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, true);
			Location myLocation = locationManager.getLastKnownLocation(provider);
			mGoogleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			if (myLocation != null)
			{
				double latitude = myLocation.getLatitude();
				double longitude = myLocation.getLongitude();
				LatLng latLng = new LatLng(latitude, longitude);
				mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				FragmentMaps.mGoogleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
			}
		} catch (SecurityException e) {
			enableMyLocation();
		}
	}

	@Override
	public boolean onMarkerClick(final Marker marker)
	{
		mGoogleMaps.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(new Bundle())));

		marker.showInfoWindow();
		return true;
	}

	@Override
	public boolean onMyLocationButtonClick()
	{
		Toast.makeText(this.getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT)
		     .show();
		//        // Return false so that we don't consume the event and the default behavior still occurs
		//        // (the camera animates to the user's current position).
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
	                                       @NonNull int[] grantResults) {
		if (requestCode != MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
			return;
		}

		if (grantResults.length > 0
				    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

			// permission was granted, yay! Do the
			// contacts-related task you need to do.
			enableMyLocation();
		} else {
			mPermissionDenied = true;
			// permission denied, boo! Disable the
			// functionality that depends on this permission.
		}
		return;
	}
	
	@Override
	public void onClick(View v)
	{
		Toast.makeText(this.getActivity(), "Good", Toast.LENGTH_SHORT)
		     .show();
	}

	@Override
	public void onResume()
	{
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onPause()
	{
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onDestroy()
	{
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		mMapView.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onLowMemory()
	{
		mMapView.onLowMemory();
		super.onLowMemory();
	}

	@Override
	public void onMapClick(LatLng latLng)
	{}

	@Override
	public void onMapLongClick(LatLng latLng)
	{
		mGoogleMaps.clear();
		MarkerOptions options = new MarkerOptions().position(latLng);
		options.title(getAddressFromLatLng(latLng));
		options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pink_red));

		MainMapsActivity.inputMarker = mGoogleMaps.addMarker(options);

		Marker info = MainMapsActivity.inputMarker;


		TextView tv = UserInputActivity.viewtext;
		tv.setText(info.getTitle());

	}

	private String getAddressFromLatLng(LatLng latLng) {
		Geocoder geocoder = new Geocoder(this.getActivity());

		String address = "";
		try {
			address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
					          .get(0).getAddressLine(0);
		} catch (IOException e) {
		}

		return address;
	}
}
