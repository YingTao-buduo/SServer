package com.yt.s_server.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yt.s_server.R;

import java.util.ArrayList;

public class HomeworkAdapter extends ArrayAdapter<Homework> {
    private int resourceId;
    public HomeworkAdapter(Context context, int textViewResourceId, final ArrayList<Homework> objects) {
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
        final Homework homework = getItem(position);
        TextView title = view.findViewById(R.id.tv_title);
        TextView ddl = view.findViewById(R.id.tv_ddl);
        TextView creator = view.findViewById(R.id.tv_creator);
        TextView score = view.findViewById(R.id.tv_hw_score);
        title.setText(homework.getTitle());
        ddl.setText(homework.getDdl());
        creator.setText(homework.getCreator());
        score.setText(homework.getScore());
        return view;
    }
}
