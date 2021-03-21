package com.yt.s_server.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yt.s_server.R;

import java.util.ArrayList;

public class LeaveAdapter extends ArrayAdapter<Leave> {
    private int resourceId;

    public LeaveAdapter(Context context, int textViewResourceId, final ArrayList<Leave> objects, boolean dm) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Leave leave = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }

        TextView type = view.findViewById(R.id.tv_leaveType);
        TextView id = view.findViewById(R.id.tv_leaveId);
        TextView status = view.findViewById(R.id.tv_leaveStatus);
        TextView time = view.findViewById(R.id.tv_leaveTime);
        TextView reason = view.findViewById(R.id.tv_leaveReason);

        switch (leave.getType()) {
            case "1":
                type.setText("事假");
                break;
            case "2":
                type.setText("病假");
                break;
            case "3":
                type.setText("公假");
                break;
            default:
                type.setText("未知");
        }
        id.setText(leave.getId());
        //格式转换时间
        String[] arr = leave.getDateCreated().split(String.valueOf('T'));
        String[] darr = arr[1].split(String.valueOf('Z'));
        String shijian = arr[0] + "\n" + darr[0];
        time.setText(shijian);
        reason.setText(leave.getReason());
        switch (leave.getStatus()){
            case "CREATED":
                status.setText("未提交");
                status.setBackgroundColor(Color.rgb(185,185,185));
                break;
            case "SUBMITTED":
                status.setText("待审核");
                status.setBackgroundColor(Color.rgb(240,180,100));
                break;
            case "APPROVED":
                status.setText("已审批");
                status.setBackgroundColor(Color.rgb(94,169,128));
                break;
            case "REJECTED":
                status.setText("退回");
                status.setBackgroundColor(Color.rgb(200,65,70));
                break;
            case "FINISHED":
                status.setText("完成");
                status.setBackgroundColor(Color.rgb(33,127,67));
                break;
            default:
                status.setText("未知");
                status.setBackgroundColor(Color.rgb(69,75,79));
        }

        return view;
    }
}
