package com.yt.s_server.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yt.s_server.R;

import java.util.ArrayList;

public class AttendanceAdapter extends ArrayAdapter<Attendance> {
    private int resourceId;

    public AttendanceAdapter(Context context, int textViewResourceId, final ArrayList<Attendance> objects, boolean dm) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Attendance attendance = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }

        TextView time = view.findViewById(R.id.tv_attendanceTime);
        TextView type = view.findViewById(R.id.tv_attendanceType);
        TextView course = view.findViewById(R.id.tv_attendanceCourse);

        switch (attendance.getType()) {
            case "1":
                type.setText("旷课");
                type.setBackgroundColor(Color.rgb(220,53,69));
                break;
            case "2":
                type.setText("迟到");
                type.setBackgroundColor(Color.rgb(255,193,7));
                break;
            case "3":
                type.setText("早退");
                type.setBackgroundColor(Color.rgb(255,193,7));
                break;
            case "4":
                type.setText("调课");
                type.setBackgroundColor(Color.rgb(84,91,98));
                break;
            default:
                type.setText("未知");
                type.setBackgroundColor(Color.rgb(255,255,255));
        }

        time.setText("第" + attendance.getWeek() + "周 星期" + attendance.getDayOfWeek() + "\n" + attendance.getStartSection() + "-" + (attendance.getStartSection()+attendance.getTotalSection())+ "节");
        course.setText(attendance.getCourse() + "【" + attendance.getCourseItem() + "】" + attendance.getTeacher());

        if(attendance.getStudentLeaveFormId() != null || attendance.getFreeListenFormId() != null){
            time.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            course.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            type.setBackgroundColor(Color.rgb(84,91,98));
        }

        return view;
    }
}
