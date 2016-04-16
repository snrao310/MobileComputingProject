package com.mobilecomputing.group3.mcproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GMapFragment mapFragment = new GMapFragment();
        fragmentTransaction.replace(R.id.content_frame, mapFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
