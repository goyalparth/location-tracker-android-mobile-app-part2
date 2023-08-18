package com.example.sampleassignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.sampleassignment1.databinding.ActivityMaps3Binding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity3 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Map<String, LatLng> pin = new HashMap<>();
//    private ActivityMaps3Binding binding;

    String u1;
    String u2;

    String dist1, dur1;
    LatLng latLng1, latLng2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

//        binding = ActivityMaps3Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps3);
        setTitle("Location Tracker");

        u1 = getIntent().getStringExtra("u1");
        u2 = getIntent().getStringExtra("u2");
//        Toast.makeText(this, u1, Toast.LENGTH_SHORT).show();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        LatLng canberraCentre = new LatLng(-35.27906, 149.13323);
//        LatLng ucBuilding6 = new LatLng(-35.236654,149.0842);

//        drawRoute(canberraCentre, ucBuilding6);
        getAllLocations(u1, u2);
    }

    public void drawRoute(LatLng origin, LatLng destination) {
//        Toast.makeText(this, "act3", Toast.LENGTH_SHORT).show();
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude + "&destination="
                + destination.latitude + "," + destination.longitude
                + "&mode=driving&key=AIzaSyB5udKJBzL5Kb6XDOIOEZT5VpYvafqdK3s";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.
                GET, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
// Parse the JSON response and draw the route on the map

                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.
                                RED);
                        polylineOptions.width(5);
                        JSONArray routes = null;
                        try {
                            routes = response.getJSONArray("routes");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        for (int i = 0; i < routes.length(); i++) {
                            try {
                                JSONObject route = routes.getJSONObject(i);
                                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                                String points = overviewPolyline.getString("points");
                                List<LatLng> path = PolyUtil.
                                        decode(points);
                                polylineOptions.addAll(path);

                                String distance = route.getJSONArray("legs")
                                        .getJSONObject(0)
                                        .getJSONObject("distance")
                                        .getString("text");
                                String duration = route.getJSONArray("legs")
                                        .getJSONObject(0)
                                        .getJSONObject("duration")
                                        .getString("text");
                                TextView dist = findViewById(R.id.textView2);
                                dist.setText("Distance: " + distance);
                                TextView dur = findViewById(R.id.textView3);
                                dur.setText("Driving Time: " + duration);

                                dist1 = distance;
                                dur1 = duration;


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        mMap.addPolyline(polylineOptions);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
// Handle the error
                    }
                });
        RequestQueue requestQueue = Volley.
                newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    public void getAllLocations(String first, String second) {
        DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference(first);
        DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference(second);

        Query sq1 = dbRef1.orderByChild("data").limitToLast(1);
        Query sq2 = dbRef2.orderByChild("data").limitToLast(1);

        sq1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot currentUserSnapshot = dataSnapshot.getChildren().iterator().next();
                    String address = currentUserSnapshot.child("Address").getValue(String.class);
                    Double latitude = currentUserSnapshot.child("Latitude").getValue(Double.class);
                    Double longitude = currentUserSnapshot.child("Longitude").getValue(Double.class);
                    String date = currentUserSnapshot.child("Date").getValue(String.class);
                    latLng1 = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.
                            newLatLngZoom(latLng1, 13));
//                    Toast.makeText(MapsActivity3.this, "in query1", Toast.LENGTH_SHORT).show();


                    Marker mk = mMap.addMarker(new MarkerOptions()
                            .position(latLng1)
                            .title(first)
                            .snippet("When: " + date + "\nWhere: " + address));
                    pin.put(mk.getId(), latLng1);

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Nullable
                        @Override
                        public View getInfoContents(@NonNull Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoWindow(@NonNull Marker marker) {
                            View infoWindow = getLayoutInflater().inflate(R.layout.data_window, null);
                            TextView title = infoWindow.findViewById(R.id.textViewTitle);
                            TextView snippet = infoWindow.findViewById(R.id.textViewSnippet);
                            ImageView image = infoWindow.findViewById(R.id.imageView);
                            if (marker.getTitle() != null && marker.getSnippet() != null) {
                                title.setText(marker.getTitle());
                                snippet.setText(marker.getSnippet());
                            } else {
                                title.setText("No info available");
                                snippet.setText("No info available");
                            }
                            image.setImageResource(R.drawable.blue_marker);
                            return infoWindow;
                        }
                    });
                    if (latLng1 != null && latLng2 != null){
                        drawRoute(latLng1, latLng2);
                    }

                } else {
                    Log.d("Location", "No locations found for name: " + first);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Location", "Error getting locations for name: " + first + ", " + databaseError.getMessage());
            }
        });

        sq2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot currentUserSnapshot = snapshot.getChildren().iterator().next();
                    String address = currentUserSnapshot.child("Address").getValue(String.class);
                    Double latitude = currentUserSnapshot.child("Latitude").getValue(Double.class);
                    Double longitude = currentUserSnapshot.child("Longitude").getValue(Double.class);
                    String date = currentUserSnapshot.child("Date").getValue(String.class);
                    latLng2 = new LatLng(latitude, longitude);
//                    Toast.makeText(MapsActivity3.this, "in query2", Toast.LENGTH_SHORT).show();

                    Marker mk = mMap.addMarker(new MarkerOptions()
                            .position(latLng2)
                            .title(second)
                            .snippet("When: " + date + "\nWhere: " + address));
                    pin.put(mk.getId(), latLng2);


                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Nullable
                        @Override
                        public View getInfoContents(@NonNull Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoWindow(@NonNull Marker marker) {
                            View infoWindow = getLayoutInflater().inflate(R.layout.data_window, null);
                            TextView title = infoWindow.findViewById(R.id.textViewTitle);
                            TextView snippet = infoWindow.findViewById(R.id.textViewSnippet);
                            ImageView image = infoWindow.findViewById(R.id.imageView);
                            if (marker.getTitle() != null && marker.getSnippet() != null) {
                                title.setText(marker.getTitle());
                                snippet.setText(marker.getSnippet());
                            } else {
                                title.setText("No info available");
                                snippet.setText("No info available");
                            }
                            image.setImageResource(R.drawable.blue_marker);
                            return infoWindow;
                        }
                    });
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(@NonNull Marker marker) {
                            Intent intent = new Intent(MapsActivity3.this, StreetView.class);
                            LatLng lat = pin.get(marker.getId());
//                            Toast.makeText(MapsActivity3.this, lat.toString(), Toast.LENGTH_SHORT).show();
//                            System.out.println(""lat.latitude);
//                            Toast.makeText(MapsActivity3.this, "" + lat.latitude, Toast.LENGTH_SHORT).show();

                            intent.putExtra("lati", lat.latitude);
                            intent.putExtra("longi", lat.longitude);
                            intent.putExtra("dist1",dist1);
                            intent.putExtra("dur1",dur1);
                            startActivity(intent);
                        }
                    });
                    if (latLng1 != null && latLng2 != null){
                        drawRoute(latLng1, latLng2);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}

