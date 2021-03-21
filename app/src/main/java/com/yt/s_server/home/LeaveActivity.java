package com.yt.s_server.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yt.s_server.R;

import java.util.ArrayList;

public class LeaveActivity extends AppCompatActivity {

    private Handler handler;
    private ListView leaveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        FloatingActionButton fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaveActivity.this, LeaveCreateActivity.class);
                startActivity(intent);
            }
        });

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = TMConnection.getInstance().getLeave();
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

                leaveList = findViewById(R.id.lv_leaveList);
                ArrayList<Leave> leaves = new Gson().fromJson(json, new TypeToken<ArrayList<Leave>>(){}.getType());

                final LeaveAdapter adapter = new LeaveAdapter(LeaveActivity.this, R.layout.view_leave, leaves, false);
                leaveList.setAdapter(adapter);

            }
        };
    }
}
