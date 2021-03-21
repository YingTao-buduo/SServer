package com.yt.s_server.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yt.s_server.R;

import java.util.ArrayList;

public class WorkActivity extends AppCompatActivity {

    private Handler handler;
    private ListView workList;
    private ArrayList<Work> openWorks;
    private ArrayList<Work> closeWorks;
    WorkAdapter openAdapter;
    WorkAdapter closeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("待办事项");
        tv_title.setGravity(Gravity.CENTER);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Message message = new Message();
                    message.what = 1;
                    String[] str = new String[2];
                    str[0] = TMConnection.getInstance().getWorkOpen();
                    str[1] = TMConnection.getInstance().getWorkClosed();
                    message.obj = str;
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


                workList = findViewById(R.id.lv_work);

                String[] str = (String[]) msg.obj;

                String open = str[0];
                String close = str[1];

                openWorks = new Gson().fromJson(open, new TypeToken<ArrayList<Work>>(){}.getType());
                closeWorks = new Gson().fromJson(close, new TypeToken<ArrayList<Work>>(){}.getType());
                openAdapter = new WorkAdapter(WorkActivity.this, R.layout.work_view, openWorks, false);
                closeAdapter = new WorkAdapter(WorkActivity.this, R.layout.work_view, closeWorks, false);
                workList.setAdapter(openAdapter);

            }
        };
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_open:
                    workList.setAdapter(openAdapter);
                    return true;
                case R.id.navigation_close:
                    workList.setAdapter(closeAdapter);
                    return true;
            }
            return false;
        }
    };
}
