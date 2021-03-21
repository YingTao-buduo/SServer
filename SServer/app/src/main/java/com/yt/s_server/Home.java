package com.yt.s_server;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.yt.s_server.home.AttendanceActivity;
import com.yt.s_server.home.ExamActivity;
import com.yt.s_server.home.LeaveActivity;
import com.yt.s_server.home.ScheduleActivity;
import com.yt.s_server.home.ScoreActivity;
import com.yt.s_server.home.TMConnection;
import com.yt.s_server.home.WorkActivity;
import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btnSchedule = getActivity().findViewById(R.id.btn_schedule);
        Button btnLeave = getActivity().findViewById(R.id.btn_leave);
        Button btnAttendance = getActivity().findViewById(R.id.btn_attendance);
        Button btnWork = getActivity().findViewById(R.id.btn_work);
        Button btnScore = getActivity().findViewById(R.id.btn_score);
        Button btnExam = getActivity().findViewById(R.id.btn_exam);

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScheduleActivity.class);
                startActivity(intent);
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LeaveActivity.class);
                startActivity(intent);
            }
        });

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AttendanceActivity.class);
                startActivity(intent);
            }
        });

        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WorkActivity.class);
                startActivity(intent);
            }
        });

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScoreActivity.class);
                startActivity(intent);
            }
        });

        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ExamActivity.class);
                startActivity(intent);
            }
        });

        new Thread(){
            @Override
            public void run(){
                try{
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("studentInfo", MODE_PRIVATE);
                    String LN = sharedPreferences.getString("stuId", ""); //登录名
                    String LP = sharedPreferences.getString("stuPassword", ""); //密码
                    TMConnection.getInstance().Connect(LN, LP);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
