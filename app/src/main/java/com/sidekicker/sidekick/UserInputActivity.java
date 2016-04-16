package com.sidekicker.sidekick;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.Marker;

public class UserInputActivity extends Fragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_user_input, null);

        Marker info = MainMapsActivity.inputMarker;

        if(info == null)
        {
            //handle some error
        }
        return v;

    }
}