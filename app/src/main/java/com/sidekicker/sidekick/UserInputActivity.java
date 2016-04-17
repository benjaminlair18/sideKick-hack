package com.sidekicker.sidekick;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.Marker;

public class UserInputActivity extends Fragment
{
    static public TextView viewtext;

        static public StreetViewPanoramaView mStreetViewPanoramaView;
        static public StreetViewPanorama mPanorama;
        static  public  View inputV;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        inputV = inflater.inflate(R.layout.activity_user_input, null);

        Marker info = MainMapsActivity.inputMarker;

        viewtext = (TextView)inputV.findViewById(R.id.textid);



        if(info == null)
        {
            //handle some error
        }
        else
        {

        }

                mStreetViewPanoramaView = (StreetViewPanoramaView) inputV.findViewById(R.id.steet_view_panorama);
                mStreetViewPanoramaView.onCreate(savedInstanceState);
                mStreetViewPanoramaView.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
                        @Override
                        public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                                panorama.setPosition(MainMapsActivity.inputLatlng);
                                mPanorama = panorama;
                            }


                            });

        return inputV;

    }


    public void onresult(View view)
    {




    }
}