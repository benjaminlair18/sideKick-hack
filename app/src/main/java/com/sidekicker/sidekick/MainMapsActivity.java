package com.sidekicker.sidekick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MainMapsActivity
		extends AppCompatActivity
{
	AppSectionsPagerAdapter mAdapter;
	ViewPager mPager;
	int Number = 0;

	static public Marker inputMarker;


		static public LatLng inputLatlng = new LatLng(-33.87365, 151.20689);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_maps_activity);
		setSupportActionBar((Toolbar)findViewById(R.id.my_login_toolbar));

		mAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.maps_pager);
		mPager.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_tutorial, menu);
		return false;
	}

	public void on99(View view)
	{

		RadioGroup rg1 = (RadioGroup) UserInputActivity.inputV.findViewById(R.id.rg1);
		RadioGroup rg2 = (RadioGroup) UserInputActivity.inputV.findViewById(R.id.rg2);
		RadioGroup rg3 = (RadioGroup) UserInputActivity.inputV.findViewById(R.id.rg3);
		RadioGroup rg4 = (RadioGroup) UserInputActivity.inputV.findViewById(R.id.rg4);
		RadioGroup rg5 = (RadioGroup) UserInputActivity.inputV.findViewById(R.id.rg5);
		RadioGroup rg6 = (RadioGroup) UserInputActivity.inputV.findViewById(R.id.rg6);
		RadioGroup rg7 = (RadioGroup) UserInputActivity.inputV.findViewById(R.id.rg7);

		String radiovalue1 = ((RadioButton)UserInputActivity.inputV.findViewById(rg1.getCheckedRadioButtonId())).getText().toString();
		String radiovalue2 = ((RadioButton)UserInputActivity.inputV.findViewById(rg2.getCheckedRadioButtonId())).getText().toString();
		String radiovalue3 = ((RadioButton)UserInputActivity.inputV.findViewById(rg3.getCheckedRadioButtonId())).getText().toString();
		String radiovalue4 = ((RadioButton)UserInputActivity.inputV.findViewById(rg4.getCheckedRadioButtonId())).getText().toString();
		String radiovalue5 = ((RadioButton)UserInputActivity.inputV.findViewById(rg5.getCheckedRadioButtonId())).getText().toString();
		String radiovalue6 = ((RadioButton)UserInputActivity.inputV.findViewById(rg6.getCheckedRadioButtonId())).getText().toString();
		String radiovalue7 = ((RadioButton)UserInputActivity.inputV.findViewById(rg7.getCheckedRadioButtonId())).getText().toString();




	}
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
	 * sections of the app.
	 */
	public
	class AppSectionsPagerAdapter
			extends FragmentPagerAdapter
			implements ViewPager.OnPageChangeListener, FragmentManager.OnBackStackChangedListener
	{

		private final String[] sectionName = { "Maps", "Tutorial" };

		public AppSectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public
		void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
		{}

		@Override
		public
		void onPageSelected(int position) {}

		@Override
		public
		void onPageScrollStateChanged(int state) {}

		@Override
		public
		void onBackStackChanged() {}

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
}
