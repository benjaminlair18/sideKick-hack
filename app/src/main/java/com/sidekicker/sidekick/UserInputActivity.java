package com.sidekicker.sidekick;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

public class UserInputActivity extends Fragment
{
    static public TextView viewtext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_user_input, null);

        Marker info = MainMapsActivity.inputMarker;

        viewtext = (TextView)v.findViewById(R.id.textid);

        if(info == null)
        {
            //handle some error
        }
        else
        {


        }

        return v;

    }
}