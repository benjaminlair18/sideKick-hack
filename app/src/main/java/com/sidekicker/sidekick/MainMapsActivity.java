package com.sidekicker.sidekick;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainMapsActivity
		extends AppCompatActivity
		implements View.OnFocusChangeListener,
		           View.OnClickListener
{
	AppSectionsPagerAdapter mAdapter;
	ViewPager mPager;
	int Number = 0;
	final int duration = 500;

	static public Marker inputMarker;
	private ImageView mSearchImg;
	private EditText mTextBox;
	private ImageButton mSearchButton;

	private int semephore = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_maps_activity);
		setSupportActionBar((Toolbar)findViewById(R.id.my_login_toolbar));

		mAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.maps_pager);
		mPager.setAdapter(mAdapter);

		mSearchImg = (ImageView)findViewById(R.id.search_icon);
		mSearchImg.setOnFocusChangeListener(this);
		mSearchImg.setOnClickListener(this);
		mTextBox = (EditText)findViewById(R.id.search_textbox);
		mTextBox.setOnFocusChangeListener(this);
		mSearchButton = (ImageButton)findViewById(R.id.search__button);
		mSearchButton.setOnFocusChangeListener(this);
		mSearchButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_tutorial, menu);
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
		switch (v.getId())
		{
		case R.id.search__button:
			semephore += (hasFocus)? 1 : -1;
			break;

		case R.id.search_textbox:
			semephore += (hasFocus)? 1 : -1;
			break;

		case R.id.search_icon:
			semephore += (hasFocus)? 1 : -1;
			break;
		}
		if (semephore < 0)
			semephore = 0;

		if (semephore == 0)
		{
			mTextBox.setText("");
			mTextBox.setVisibility(View.GONE);
			mSearchButton.setVisibility(View.GONE);
			mTextBox.animate().alpha(0.0f).scaleX(0.0f).setDuration(duration);
			mSearchButton.animate().alpha(0.0f).scaleX(0.0f).setDuration(duration);
		}
		else
		{
			mTextBox.setVisibility(View.VISIBLE);
			mSearchButton.setVisibility(View.VISIBLE);
			mTextBox.animate().alpha(1.0f).scaleX(1.0f).setDuration(duration);
			mSearchButton.animate().alpha(1.0f).scaleX(1.0f).setDuration(duration);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.search_icon:
			mTextBox.setVisibility(View.VISIBLE);
			mSearchButton.setVisibility(View.VISIBLE);
			mTextBox.animate().alpha(1.0f).scaleX(1.0f).setDuration(duration);
			mSearchButton.animate().alpha(1.0f).scaleX(1.0f).setDuration(duration);
			break;

		case R.id.search__button:
			if (!TextUtils.isEmpty(mTextBox.getText().toString()) && onSearch(mTextBox.getText().toString()))
			{
				mTextBox.setVisibility(View.GONE);
				mSearchButton.setVisibility(View.GONE);
				mTextBox.animate().alpha(0.0f).scaleX(0.0f).setDuration(duration);
				mSearchButton.animate().alpha(0.0f).scaleX(0.0f).setDuration(duration);
				mTextBox.setText("");
				Toast.makeText(this, "Here we go!", Toast.LENGTH_SHORT).show();
			}
			else
			{
				mTextBox.setVisibility(View.GONE);
				mSearchButton.setVisibility(View.GONE);
				mTextBox.animate().alpha(0.0f).scaleX(0.0f).setDuration(duration);
				mSearchButton.animate().alpha(0.0f).scaleX(0.0f).setDuration(duration);

				mTextBox.setText("");
				Toast.makeText(this, "Wrong address", Toast.LENGTH_SHORT)
				     .show();
			}
			break;
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
	 * sections of the app.
	 */
	public
	class AppSectionsPagerAdapter
			extends FragmentPagerAdapter
	{

		private final String[] sectionName = { "Maps", "Tutorial" };
		private int curPosition = -1;

		public AppSectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public
		Fragment getItem(int i)
		{
			Fragment newFragment = null;

			switch (i)
			{

			case 0:
				newFragment = new FragmentMaps();
				break;

			case 1:
				newFragment = new UserInputActivity();
				break;

			default:
				return null;
			}

			return newFragment;
		}

		@Override
		public
		int getCount()
		{
			return sectionName.length;
		}


		@Override
		public
		CharSequence getPageTitle(int position)
		{
			return sectionName[position];
		}

	}

	public boolean onSearch(String placeName)
	{
		String location = placeName;
		List<Address> addressList = null;
		if (location != null || !location.equals(""))
		{
			Geocoder geocoder = new Geocoder(this);
			try
			{
				addressList = geocoder.getFromLocationName(location, 1);
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			Address address = addressList.get(0);
			LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
			FragmentMaps.mGoogleMaps.addMarker(new MarkerOptions().position(latLng).title("Marker"));
			FragmentMaps.mGoogleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
			return true;
		}
		return false;
	}
}
