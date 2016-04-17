package com.sidekicker.sidekick;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;

import android.location.Criteria;
import android.location.LocationManager;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMarkerClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener,
        RoutingListener {

    private GoogleMap mMap;
    ArrayList<LatLng> markerPoints;
    private UiSettings mUiSettings;
    private ImageButton mButton;
    protected LatLng start;
    protected LatLng waypoint;
    protected LatLng end;
    //        @InjectView(R.id.start)
    AutoCompleteTextView starting;
    //        @InjectView(R.id.destination)
    AutoCompleteTextView destination;
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,R.color.colorPrimary,R.color.colorPrimary,R.color.colorAccent,R.color.primary_dark_material_light};


    private static final String LOG_TAG = "MyActivity";

    private ProgressDialog progressDialog;
    private ArrayList<Polyline> polylines;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mPermissionDenied = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //mTopText = (TextView) findViewById(R.id.top_text);
        mButton = (ImageButton) findViewById(R.id.imageButton);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) this.findViewById(R.id.left_drawer);
        mButton.setOnClickListener(this);
        polylines = new ArrayList<Polyline>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        start = new LatLng(25.208787, 121.693597);
        end = new LatLng(25.0590878727585, 121.51380110532);

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.WALKING)
                .withListener(this)
                .waypoints(start,end)
                .build();
        routing.execute();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                mDrawerLayout.openDrawer(mDrawerList);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
//        progressDialog.dismiss();
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
//        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        if(route.size() > 0)
        {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(R.color.colorPrimaryDark);
            polyOptions.width(10);
            polyOptions.addAll(route.get(0).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(), "Route: distance - " + route.get(0).getDistanceValue() + ": duration - " + route.get(0).getDurationValue(), Toast.LENGTH_SHORT).show();

            // Start marker
            MarkerOptions options = new MarkerOptions();
            options.position(start);
            options.icon(BitmapDescriptorFactory.defaultMarker());
            mMap.addMarker(options);

            // End marker
            options = new MarkerOptions();
            options.position(end);
            options.icon(BitmapDescriptorFactory.defaultMarker());
            mMap.addMarker(options);
        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.i(LOG_TAG, "Routing was cancelled.");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.sidekicker.sidekick/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.sidekicker.sidekick/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

//        private String getDirectionsUrl(LatLng origin,LatLng dest){
//            // Origin of route
//            String str_origin = "origin="+origin.latitude+","+origin.longitude;
//
//            // Destination of route
//            String str_dest = "destination="+dest.latitude+","+dest.longitude;
//
//            // Sensor enabled
//            String key = "key=";
//
//            // Building the parameters to the web service
//            String parameters = str_origin+"&"+str_dest+"&"+key+"AIzaSyBTCBTkNd1pNpFryUVt_v4tOVkvs8G913Y";
//
//            // Output format
//            String output = "json";
//
//            // Building the url to the web service
//            String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
//
//            return url;
//        }
//        /** A method to download json data from url */
//        private String downloadUrl(String strUrl) throws IOException{
//            String data = "";
//            InputStream iStream = null;
//            HttpURLConnection urlConnection = null;
//            try{
//                URL url = new URL(strUrl);
//
//                // Creating an http connection to communicate with url
//                urlConnection = (HttpURLConnection) url.openConnection();
//
//                // Connecting to url
//                urlConnection.connect();
//
//                // Reading data from url
//                iStream = urlConnection.getInputStream();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//                StringBuffer sb = new StringBuffer();
//
//                String line = "";
//                while( ( line = br.readLine()) != null){
//                    sb.append(line);
//                }
//
//                data = sb.toString();
//
//                br.close();
//
//            }catch(Exception e){
//                Toast.makeText(this, "Exception while downloading url", Toast.LENGTH_LONG).show();
//            }finally{
//                iStream.close();
//                urlConnection.disconnect();
//            }
//            return data;
//        }

//        // Fetches data from url passed
//        private class DownloadTask extends AsyncTask<String, Void, String>{
//
//            // Downloading data in non-ui thread
//            @Override
//            protected String doInBackground(String... url) {
//
//                // For storing data from web service
//                String data = "";
//
//                try{
//                    // Fetching the data from web service
//                    data = downloadUrl(url[0]);
//                }catch(Exception e){
//                    Log.d("Background Task",e.toString());
//                }
//                return data;
//            }
//
//            // Executes in UI thread, after the execution of
//            // doInBackground()
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//
//                ParserTask parserTask = new ParserTask();
//
//                // Invokes the thread for parsing the JSON data
//                parserTask.execute(result);
//            }
//        }

    /** A class to parse the Google Places in JSON format */
//        private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
//
//            // Parsing the data in non-ui thread
//            @Override
//            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//                JSONObject jObject;
//                List<List<HashMap<String, String>>> routes = null;
//
//                try{
//                    jObject = new JSONObject(jsonData[0]);
//                    DirectionsJSONParser parser = new DirectionsJSONParser();
//
//                    // Starts parsing data
//                    routes = parser.parse(jObject);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//                return routes;
//            }
//
//            // Executes in UI thread, after the parsing process
//            @Override
//            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//                ArrayList<LatLng> points = null;
//                PolylineOptions lineOptions = null;
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                // Traversing through all the routes
//                for(int i=0;i<result.size();i++){
//                    points = new ArrayList<LatLng>();
//                    lineOptions = new PolylineOptions();
//
//                    // Fetching i-th route
//                    List<HashMap<String, String>> path = result.get(i);
//
//                    // Fetching all the points in i-th route
//                    for(int j=0;j<path.size();j++){
//                        HashMap<String,String> point = path.get(j);
//
//                        double lat = Double.parseDouble(point.get("lat"));
//                        double lng = Double.parseDouble(point.get("lng"));
//                        LatLng position = new LatLng(lat, lng);
//
//                        points.add(position);
//                    }
//
//                    // Adding all the points in the route to LineOptions
//                    lineOptions.addAll(points);
//                    lineOptions.width(2);
//                    lineOptions.color(Color.RED);
//                }
//
//                // Drawing polyline in the Google Map for the i-th route
//                mMap.addPolyline(lineOptions);
//            }
//        }

//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu; this adds items to the action bar if it is present.
//            getMenuInflater().inflate(R.menu.main, menu);
//            return true;
//        }
}
