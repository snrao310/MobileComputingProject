package com.mobilecomputing.group3.mcproject;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by snrao on 4/18/16.
 */
public class ProfileFragment extends Fragment{


    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        TextView username= (TextView) view.findViewById(R.id.username);
        return view;
    }
}
