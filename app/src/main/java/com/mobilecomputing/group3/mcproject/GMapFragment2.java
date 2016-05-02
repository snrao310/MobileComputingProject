package com.mobilecomputing.group3.mcproject;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by snrao on 12/4/16.
 */
public class GMapFragment2 extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    GoogleMap gmap;
    View view;
    LatLng currPos;
    Address curraddr;
    OnLocationSetListener onLocationSetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.map_fragment2, container, false);
           return view;
        }
        catch (Exception e){
            e.getMessage();}
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment mapFragment = null;

        int apiVersion = android.os.Build.VERSION.SDK_INT;
        if ( apiVersion > Build.VERSION_CODES.KITKAT) {
            mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapfrag2);
        } else {
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfrag2);
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap=googleMap;

//        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        try {
            LatLng ll=new LatLng(0,0);
            JSONArray placesList=new JSONArray(getActivity().getIntent().getExtras().get("Places").toString());
            for (int i = 0; i < placesList.length(); i++) {
                JSONObject temp = placesList.getJSONObject(i);

                double lat = Double.parseDouble(temp.getString("latitude"));
                double longi = Double.parseDouble(temp.getString("longitude"));

                ll=new LatLng(lat,longi);
                googleMap.addMarker(new MarkerOptions().position(ll).title(temp.getString("name")));
            }
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
//            googleMap.setMyLocationEnabled(true);
        }
        catch (Exception e)
        {
            return;
        }
    }



    @Override
    public void onClick(View v) {

       if (v.getId()==R.id.selectBut)
        {
            getActivity().finish();
        }
    }



    public interface OnLocationSetListener{
        public void setLoc(Address s);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            onLocationSetListener=(OnLocationSetListener) activity;
        }catch (Exception e){}
    }
}
