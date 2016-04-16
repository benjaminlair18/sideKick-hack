package com.sidekicker.sidekick;

import com.viewpagerindicator.IconPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter{

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getIconResId(int index) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Fragment getItem(int position)
    {
        // TODO Auto-generated method stub
        Fragment fragment = new FirstTutorialActivity();
        switch(position){
            case 0:
                fragment = new FirstTutorialActivity();
                break;
            case 1:
                fragment = new SecondTutorialActivity();
                break;
            case 2:
                fragment = new ThirdTutorialActivity();
                break;
        }
        return fragment;
    }

    //How many fragments there will be
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        switch(position){
            case 0:
                title = "Report";
                break;
            case 1:
                title = "Warn";
                break;
            case 2:
                title = "S.O.S.";
                break;
        }
        return title;
    }

}
