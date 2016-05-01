package com.mobilecomputing.group3.mcproject;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    boolean b = true;
    boolean rb=true;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Trigger MeetService every minute to check for pending
        // meeting requests
        Calendar cal = Calendar.getInstance();
        Intent meetIntent = new Intent(this, MeetReceiver.class);
        Bundle i = getIntent().getExtras();
        meetIntent.putExtras(i);
        pendingIntent = PendingIntent.getBroadcast(this, 0, meetIntent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10 * 1000, pendingIntent);
    }

    public void onSearchBtnClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FilterFragment ff = (FilterFragment) fragmentManager.findFragmentById(R.id.filter_fragment);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if ( b ) {
            ff.refresh();
            fragmentTransaction.show(ff).commit();
            b = false;
        } else {
            fragmentTransaction.hide(ff).commit();
            b = true;
        }
    }


    public void onReqBtnClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        RequestsFragment rf = (RequestsFragment) fragmentManager.findFragmentById(R.id.requests_fragment);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if ( rb ) {
            rf.refresh();
            fragmentTransaction.show(rf).commit();
            rb = false;
        } else {
            fragmentTransaction.hide(rf).commit();
            rb = true;
        }
    }

    @Override
    public void onDestroy() {
        alarmManager.cancel(pendingIntent);
        super.onDestroy();
    }
}
