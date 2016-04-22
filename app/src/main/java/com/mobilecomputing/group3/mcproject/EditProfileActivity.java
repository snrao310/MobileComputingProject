package com.mobilecomputing.group3.mcproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditProfileActivity extends AppCompatActivity implements GMapFragment.OnLocationSetListener{

    GMapFragment mapFragment;
    EditProfileFragment editProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        editProfileFragment=new EditProfileFragment();
        fragmentTransaction.add(R.id.editp_content_frame, editProfileFragment, "TRY");
        fragmentTransaction.addToBackStack("TRY");
        fragmentTransaction.commit();
    }

    @Override
    public void setLoc(Address s) {
        editProfileFragment.update(s);
    }

}
