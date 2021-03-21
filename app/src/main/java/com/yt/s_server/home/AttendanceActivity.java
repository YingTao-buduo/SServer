package com.yt.s_server.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yt.s_server.R;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {

    private Handler handler;
    private ListView attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = TMConnection.getInstance().getAttendance();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String json = msg.obj.toString();
                attendanceList = findViewById(R.id.lv_attendanceList);
                JsonObject schedulesJson = new JsonParser().parse(json).getAsJsonObject();

                ArrayList<Attendance> leaves = new Gson().fromJson(schedulesJson.get("rollcalls"), new TypeToken<ArrayList<Attendance>>(){}.getType());

                final ArrayAdapter adapter = new AttendanceAdapter(AttendanceActivity.this, R.layout.attendance_view, leaves, false);
                attendanceList.setAdapter(adapter);
            }
        };
    }
}
