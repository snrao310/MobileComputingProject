package com.mobilecomputing.group3.mcproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sureshgururajan on 4/19/16.
 */
public class SearchAdapter extends ArrayAdapter {
    private List list= new ArrayList();

    public SearchAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(SearchClass object) {
        list.add(object);
        super.add(object);
    }

    static class SearchHolder
    {
        TextView USERNAME;
        Button meet;
        Button add;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        SearchHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.userlist,parent,false);
            holder = new SearchHolder();
            holder.USERNAME = (TextView) row.findViewById(R.id.txtUsername);
            row.setTag(holder);
        }
        else
        {
            holder = (SearchHolder) row.getTag();
        }

        SearchClass FR = (SearchClass) getItem(position);
        holder.USERNAME.setText(FR.getUsername());
        return row;
    }
}
