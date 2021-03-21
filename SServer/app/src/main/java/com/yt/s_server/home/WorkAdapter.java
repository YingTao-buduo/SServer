package com.yt.s_server.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yt.s_server.R;

import java.util.ArrayList;

public class WorkAdapter extends ArrayAdapter<Work> {
    private int resourceId;

    public WorkAdapter(Context context, int textViewResourceId, final ArrayList<Work> objects, boolean dm) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Work work = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }


        TextView type = view.findViewById(R.id.tv_workType);
        TextView time = view.findViewById(R.id.tv_workTime);
        TextView from = view.findViewById(R.id.tv_workFrom);

        type.setText(work.getType());
        //格式转换时间
        String[] arr = work.getDateCreated().split(String.valueOf('T'));
        String[] darr = arr[1].split(String.valueOf('Z'));
        String shijian = arr[0] + " " + darr[0];
        time.setText(shijian);
        from.setText(work.getTitle() + "  " + work.getFromUser());

        return view;
    }
}
