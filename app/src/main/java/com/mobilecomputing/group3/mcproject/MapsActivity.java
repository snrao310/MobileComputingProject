package com.mobilecomputing.group3.mcproject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        gMap = googleMap;

        try {
            LatLng ll=new LatLng(0,0);
            JSONArray placesList=new JSONArray(getIntent().getExtras().get("Places").toString());
            for (int i = 0; i < placesList.length(); i++) {
                JSONObject temp = placesList.getJSONObject(i);

                double lat = Double.parseDouble(temp.getString("latitude"));
                double longi = Double.parseDouble(temp.getString("longitude"));

                ll=new LatLng(lat,longi);
                googleMap.addMarker(new MarkerOptions().position(ll).title(temp.getString("name")));
            }
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 13));
//            googleMap.setMyLocationEnabled(true);
        }
        catch (Exception e)
        {
            return;
        }
    }
}
