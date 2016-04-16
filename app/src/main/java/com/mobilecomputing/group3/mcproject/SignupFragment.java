package com.mobilecomputing.group3.mcproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by snrao on 12/4/16.
 */
public class SignupFragment extends Fragment implements View.OnClickListener{

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.signup_fragment,container,false);
        Button setLoc=(Button) view.findViewById(R.id.locSelect);
        setLoc.setOnClickListener(this);

        Button signup=(Button) view.findViewById(R.id.submit);
        signup.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.locSelect) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            GMapFragment mapFragment = new GMapFragment();
            fragmentTransaction.replace(R.id.content_frame, mapFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(v.getId()==R.id.submit){

        }
    }

    public void update(Address curraddr){
        EditText t=(EditText) view.findViewById(R.id.locationbox);
        t.setText(curraddr.getAddressLine(0)+", "+curraddr.getAddressLine(1)+" "+curraddr.getAddressLine(2)+" "+curraddr.getAddressLine(3));
    }
}
