package com.yt.s_server.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yt.s_server.R;

import java.util.ArrayList;

public class ApplyCourseAdapter extends ArrayAdapter<ApplyCourse> {

    private int resourceId;

    public ApplyCourseAdapter(Context context, int textViewResourceId, final ArrayList<ApplyCourse> objects, boolean dm){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ApplyCourse applyCourse = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }


        TextView name = view.findViewById(R.id.tv_apply_course_name);
        TextView teacher = view.findViewById(R.id.tv_apply_course_teacher);
        TextView time = view.findViewById(R.id.tv_apply_course_time);
        TextView classroom = view.findViewById(R.id.tv_apply_course_classroom);
        CheckBox checkBox = view.findViewById(R.id.is_done);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    applyCourse.setChecked(true);
                else
                    applyCourse.setChecked(false);
            }
        });

        String weekday = applyCourse.getDayOfWeek();
        switch(weekday){
            case "1":
                weekday = "星期一 ";
                break;
            case "2":
                weekday = "星期二 ";
                break;
            case "3":
                weekday = "星期三 ";
                break;
            case "4":
                weekday = "星期四 ";
                break;
            case "5":
                weekday = "星期五 ";
                break;
            case "6":
                weekday = "星期六 ";
                break;
            case "7":
                weekday = "星期日 ";
                break;
        }

        name.setText(applyCourse.getCourse());
        teacher.setText(applyCourse.getTeacherName());
        time.setText(weekday + applyCourse.getStartSection() + "-" + String.valueOf(Integer.parseInt(applyCourse.getStartSection()) + Integer.parseInt(applyCourse.getTotalSection()) - 1) + "节");
        classroom.setText(applyCourse.getPlace());

        return view;
    }
}
