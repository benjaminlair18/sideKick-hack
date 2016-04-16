package com.sidekicker.sidekick;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.speech.RecognizerIntent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.LocationSource;
import com.quinny898.library.persistentsearch.SearchBox;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


    public class MapsActivity extends FragmentActivity implements OnMarkerClickListener,
            OnMapReadyCallback,
            GoogleMap.OnMyLocationButtonClickListener,
            ActivityCompat.OnRequestPermissionsResultCallback,
            OnMapLongClickListener,
            GoogleMap.OnMapClickListener {

        private GoogleMap mMap;
        private UiSettings mUiSettings;
        private Marker mSydney;
        private Marker mLastSelectedMarker;
        private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
        private TextView mTopText;

        private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
        private static final LatLng ADELAIDE = new LatLng(34.92873, 138.59995);
        private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        private boolean mPermissionDenied = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            //mTopText = (TextView) findViewById(R.id.top_text);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        }

        private void initListeners() {
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMapClickListener(this);
        }


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            initListeners();
            enableMyLocation();

            mUiSettings = mMap.getUiSettings();

            // Keep the UI Settings state in sync with the checkboxes.
            mUiSettings.setZoomControlsEnabled(true);
        }

        /**
         * Enables the My Location layer if the fine location permission has been granted.
         */
        /**
         * Enables the My Location layer if the fine location permission has been granted.
         */
        private void enableMyLocation() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission to access the location is missing.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else if (mMap != null) {
                // Access to the location has been granted to the app.
                mMap.setMyLocationEnabled(true);
                SetUpMap();
            }
        }

        private void SetUpMap() {
            try {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                Location myLocation = locationManager.getLastKnownLocation(provider);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (myLocation != null) {
                    double latitude = myLocation.getLatitude();
                    double longitude = myLocation.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title("You are here!")
                    );
                }
            } catch (SecurityException e) {
                enableMyLocation();
            }
        }


        @Override
        public boolean onMyLocationButtonClick() {
            Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
            // Return false so that we don't consume the event and the default behavior still occurs
            // (the camera animates to the user's current position).
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
        public void onMapClick(LatLng latLng) {
        }

        @Override
        public void onMapLongClick(LatLng latLng) {
            MarkerOptions options = new MarkerOptions().position(latLng);
            options.title(getAddressFromLatLng(latLng));
            options.icon(BitmapDescriptorFactory.defaultMarker());

            mMap.addMarker(options);
        }

        private String getAddressFromLatLng(LatLng latLng) {
            Geocoder geocoder = new Geocoder(this);

            String address = "";
            try {
                address = geocoder
                        .getFromLocation(latLng.latitude, latLng.longitude, 1)
                        .get(0).getAddressLine(0);
            } catch (IOException e) {
            }

            return address;
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            return true;
        }


        public void onSearch(View view) {
            EditText location_tf = (EditText) findViewById(R.id.TFaddress);
            String location = location_tf.getText().toString();
            List<Address> addressList = null;
            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }

    }

