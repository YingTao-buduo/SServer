package com.yt.s_server.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yt.s_server.R;
import com.yt.s_server.user.Exam;

import java.util.ArrayList;

public class ExamAdapter extends ArrayAdapter<Exam> {
    private int resourceId;

    public ExamAdapter(Context context, int textViewResourceId, final ArrayList<Exam> objects, boolean dm) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Exam exam = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }

        TextView course = view.findViewById(R.id.tv_exam_course);
        TextView time = view.findViewById(R.id.tv_exam_time);
        TextView classroom = view.findViewById(R.id.tv_exam_classroom);
        TextView seat = view.findViewById(R.id.tv_exam_seat);
        TextView teacher = view.findViewById(R.id.tv_exam_teacher);

        course.setText(String.format("%s", exam.getCourse()));
        time.setText(String.format("%s", exam.getTime()));
        classroom.setText(String.format("%s", exam.getClassroom()));
        seat.setText(String.format("%s", exam.getSeat()));
        teacher.setText(String.format("%s", exam.getTeacher()));

        return view;
    }
}
