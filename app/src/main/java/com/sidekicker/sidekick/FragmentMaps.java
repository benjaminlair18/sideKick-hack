package com.sidekicker.sidekick;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by root on 4/16/16.
 */
public class FragmentMaps
		extends Fragment
		implements GoogleMap.OnMarkerClickListener,
		           OnMapReadyCallback,
		           GoogleMap.OnMyLocationButtonClickListener,
		           MapView.OnClickListener,
		           ActivityCompat.OnRequestPermissionsResultCallback
{
	private GoogleMap mGoogleMaps;
	private MapView mMapView;
	private Marker mSydney;
	private Marker mLastSelectedMarker;
	private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

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

		return view;
	}

	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		mGoogleMaps = googleMap;
		//		Add a marker in Sydney and move the camera
		mGoogleMaps.addMarker(new MarkerOptions().position(SYDNEY)
		                                         .title("Marker in Sydney")
		                                         .snippet("Population: 4,627,300"));
		mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));
		mGoogleMaps.setOnMarkerClickListener(this);

		mGoogleMaps.setOnMyLocationButtonClickListener(this);
		enableMyLocation();
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
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker)
	{
		if (marker.equals(mSydney))
		{}
		//
		mLastSelectedMarker = marker;
		//        // We return false to indicate that we have not consumed the event and that we wish
		//        // for the default behavior to occur (which is for the camera to move such that the
		//        // marker is centered and for the marker's info window to open, if it has one).
		return false;
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

}
