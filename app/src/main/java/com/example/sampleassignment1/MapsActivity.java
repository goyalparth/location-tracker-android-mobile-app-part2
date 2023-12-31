package com.example.sampleassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.sampleassignment1.databinding.ActivityMapsBinding;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    SupportStreetViewPanoramaFragment streetViewPanoramaFragment;
    StreetViewPanorama mStreetViewPanorama;

    double latitude;
    double longitude;
    LatLng latLngRed, latLngBlue;
    String address;
    TextView textViewAddress;
    TextView textViewLatitude;
    TextView textViewLongitude;
    Button btnMapStreetView;
    PlacesClient placesClient;
    Marker redMarker, blueMarker;
    boolean showMap = true;
    MyLocationPlaceMap myLocationPlaceMap;
    ArrayList<MyLocationPlace> myLocations = new ArrayList<>();
    MyLocationPlace myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setTitle("My Location Details");
        btnMapStreetView = findViewById(R.id.buttonMapStreetView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            latitude = extras.getDouble("lat");
            longitude = extras.getDouble("lng");
            latLngRed = new LatLng(latitude, longitude);
            Date date2 = new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss a", Locale.getDefault());
            String date = sdFormat.format(date2);
            address = extras.getString("addr");


//            LatLng canberraCentre = new LatLng(-35.27906, 149.13323);
//            Double lat = -35.27906;
//            Double lon = 149.13323;
//            uploadData(extras.getString("username"), lat,lon,address,date);
            uploadData(extras.getString("username"), latitude,longitude,address,date);
//            uploadData(extras.getString("username"), longitude);

            textViewAddress = findViewById(R.id.textViewStreetAddress);
            textViewAddress.setText("Address: " + address);
            textViewLatitude = findViewById(R.id.textViewLatitude);
            textViewLatitude.setText("Latitude: " + latitude);
            textViewLongitude = findViewById(R.id.textViewLongitude);
            textViewLongitude.setText("Longitude: " + longitude);
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.streetView);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        mapFragment.getView().bringToFront();

        myLocationPlaceMap = new MyLocationPlaceMap(getApplicationContext(), MapsActivity.this);
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
//        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
//        mMap.clear();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                redMarker = mMap.addMarker(new MarkerOptions()
                        .title("Show Surroundings")
                        .snippet("Latitude: " + latitude + ", Longitude: " + longitude +
                                "\nAddress: " + address)
                        .position(latLngRed)
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngRed, 14));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (marker.equals(redMarker)) {
                    streetViewPanoramaFragment.getView().bringToFront();
                    btnMapStreetView.setText("Show map");
                    showMap = false;
                    return true;
                } else {
                    return false;
                }

            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                if (!marker.getId().equals(redMarker.getId())) {
                    streetViewPanoramaFragment.getView().bringToFront();
                    latLngBlue = marker.getPosition();
                    mStreetViewPanorama.setPosition(latLngBlue);
                }
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.data_window, null);
                TextView title = (TextView) infoWindow.findViewById(R.id.textViewTitle);
                TextView snippet = (TextView) infoWindow.findViewById(R.id.textViewSnippet);
                ImageView image = (ImageView) infoWindow.findViewById(R.id.imageView);

                if (marker.getTitle() != null && marker.getSnippet() != null) {
                    title.setText(marker.getTitle());
                    snippet.setText(marker.getSnippet());
                } else {
                    title.setText("No info available");
                    snippet.setText("No location available");
                }
                image.setImageDrawable(getResources()
                        .getDrawable(R.drawable.blue_marker, getTheme()));
                return infoWindow;
            }
        });
    }

    @Override
    public void onStreetViewPanoramaReady(@NonNull StreetViewPanorama streetViewPanorama) {
        mStreetViewPanorama = streetViewPanorama;
        mStreetViewPanorama.setPosition(latLngRed);
    }

    public void showNearby(View view) {
        mapFragment.getView().bringToFront();
        myLocationPlaceMap.getNearbyPlaces(mMap, "AIzaSyB5udKJBzL5Kb6XDOIOEZT5VpYvafqdK3s");
    }

    public void showMapStreetView(View view) {
        showMap = !showMap;
        if (showMap) {
            mapFragment.getMapAsync(this);
            mapFragment.getView().bringToFront();
            btnMapStreetView.setText("Show Street View");
        } else {
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
            streetViewPanoramaFragment.getView().bringToFront();
            btnMapStreetView.setText("Show Map");
        }
    }

    public void uploadData(String name, double latitude, double longitude, String address, String date){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(name);
        String rand = dbRef.push().getKey();
        dbRef.child(rand).child("Latitude").setValue(latitude);
        dbRef.child(rand).child("Longitude").setValue(longitude);
        dbRef.child(rand).child("Address").setValue(address);
        dbRef.child(rand).child("Date").setValue(date);
    }


}