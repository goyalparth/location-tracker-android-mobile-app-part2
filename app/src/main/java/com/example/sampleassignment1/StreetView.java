package com.example.sampleassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class StreetView extends AppCompatActivity {
    Double lati;
    Double longi;
    String address,dist1,dur1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);

        setTitle("Location Tracker");

        Bundle bundle = getIntent().getExtras();
        lati = bundle.getDouble("lati");
        longi = bundle.getDouble("longi");
        dist1 = bundle.getString("dist1");
        dur1 = bundle.getString("dur1");

        TextView dist11 = findViewById(R.id.textView4);
        dist11.setText("Distance: " + dist1);

        TextView dur11 = findViewById(R.id.textView5);
        dur11.setText("Driving Time: " + dur1);


        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.streetView);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {

                        LatLng uc = new LatLng(lati, longi);
                        panorama.setPosition(uc);
                    }
                });



    }

    private void openMap() {
        Intent intent = new Intent(this, MapsActivity.class);
//        intent.putExtra("latid", lati);
//        intent.putExtra("longid" , longi);
//        intent.putExtra("addressd" , address);
        startActivity(intent);
    }

}