package com.mobilecomputing.group3.mcproject;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by snrao on 12/4/16.
 */
public class GMapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    GoogleMap gmap;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.map_fragment, container, false);
        Button sb=(Button) view.findViewById(R.id.searchButton);
        sb.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap=googleMap;
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        try {
            googleMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e)
        {
                return;
        }
    }



    @Override
    public void onClick(View v) {
        EditText location_tf = (EditText) view.findViewById(R.id.searchbox);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            gmap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}
