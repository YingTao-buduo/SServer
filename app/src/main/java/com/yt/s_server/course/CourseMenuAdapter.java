package com.yt.s_server.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yt.s_server.R;

import java.util.ArrayList;

public class CourseMenuAdapter extends ArrayAdapter<String> {
    private int resourceId;

    public CourseMenuAdapter(Context context, int textViewResourceId, final ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }else {
            view = convertView;
        }
        final String menuItem = getItem(position);
        TextView courseMenuItem = view.findViewById(R.id.tv_courseMenuItem);
        courseMenuItem.setText(menuItem);
        return view;
    }
}
