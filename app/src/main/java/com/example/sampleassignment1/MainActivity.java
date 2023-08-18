package com.example.sampleassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MyLocationPlaceMap myLocationPlaceMap;
    ArrayList<MyLocationPlace> myLocations = new ArrayList<>();
    MyLocationPlace myLocation;
    String u1,u2,u3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Location Tracker");


        dbHelper dbHelper = new dbHelper(MainActivity.this, "user");

        myLocationPlaceMap = new MyLocationPlaceMap(getApplicationContext(), MainActivity.this);
        myLocationPlaceMap.requestPermissions();
        myLocationPlaceMap.getLatLngAddress(myLocations);

        Button buttonMe = findViewById(R.id.buttonWhereAmI);
        Button buttonShe = findViewById(R.id.buttonWhereIsUser2);
        Button buttonHe = findViewById(R.id.buttonWhereIsUser3);

//

        if (dbHelper != null){
            ArrayList<String> list = dbHelper.getAllColours();
//            Toast.makeText(this, list.get(0), Toast.LENGTH_SHORT).show();
            u1=list.get(0);
            u2=list.get(1);
            u3=list.get(2);

            buttonMe.setText("WHERE AM I ("+u1+")?");
            buttonShe.setText("WHERE IS "+u2+"?");
            buttonHe.setText("WHERE IS "+ u3+"?");
        }

        buttonShe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MapsActivity3.class);
                intent.putExtra("u1",u1);
                intent.putExtra("u2", u2);
                startActivity(intent);

            }
        });

        buttonHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MapsActivity3.class);
                intent.putExtra("u1",u1);
                intent.putExtra("u2", u3);
                startActivity(intent);

            }
        });
    }

    public void whereAmI (View view) {
        myLocationPlaceMap.getLatLngAddress(myLocations);

        if (myLocations.size() > 0) {
            myLocation = myLocations.get(0);
            myLocations.clear();

            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("lat", myLocation.getLatitude());
            intent.putExtra("lng", myLocation.getLongitude());
            intent.putExtra("addr", myLocation.getAddress());
            intent.putExtra("username", u1);
            startActivity(intent);


        }
    }



}