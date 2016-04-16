package com.sidekicker.sidekick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.google.android.gms.maps.model.Marker;

public class MainMapsActivity
		extends FragmentActivity
{
	AppSectionsPagerAdapter mAdapter;
	ViewPager mPager;
	int Number = 0;


	static public Marker inputMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_maps_activity);

		mAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.maps_pager);
		mPager.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_tutorial, menu);
		return false;
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
