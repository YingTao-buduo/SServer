package com.yt.s_server.home;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yt.s_server.R;
import java.util.ArrayList;

public class LeaveCreateActivity extends AppCompatActivity {

    Handler handler;

    RadioGroup typeGroup;
    RadioButton rType_1;
    RadioButton rType_2;
    RadioButton rType_3;
    Button apply;
    EditText reason;
    Spinner week;
    ListView courseList;
    String leaveType;
    String leaveWeek;
    String curentWeek;
    String maxWeek;

    ArrayList<String> selectedCourse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_create);

        typeGroup = findViewById(R.id.rg_type);
        rType_1 = findViewById(R.id.rbtn_type_1);
        rType_2 = findViewById(R.id.rbtn_type_2);
        rType_3 = findViewById(R.id.rbtn_type_3);
        apply = findViewById(R.id.btn_apply);
        reason = findViewById(R.id.et_apply_reason);
        week = findViewById(R.id.sp_week);
        courseList = findViewById(R.id.lv_apply_course);
        final ArrayList<String> weeks = new ArrayList<>();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = TMConnection.getInstance().getApplyCourse();
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
                String result = msg.obj.toString();
                JsonObject schedulesJson = new JsonParser().parse(result).getAsJsonObject();

                ArrayList<ApplyCourse> leaves_1 = new Gson().fromJson(schedulesJson.get("schedules"), new TypeToken<ArrayList<ApplyCourse>>(){}.getType());
                final ArrayAdapter adapter = new ApplyCourseAdapter(LeaveCreateActivity.this, R.layout.view_apply_course, leaves_1, false);
                courseList.setAdapter(adapter);
                Tweek tweeks = new Gson().fromJson(schedulesJson.get("term"),Tweek.class);
                curentWeek = tweeks.getCurrentWeek();
                maxWeek = tweeks.getEndWeek();
                for(int i = 1; i <= Integer.parseInt(maxWeek); i++){
                    weeks.add(String.valueOf(i));
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(LeaveCreateActivity.this, android.R.layout.simple_spinner_item, weeks);

                week.setAdapter(spinnerAdapter);
                week.setSelection(Integer.parseInt(curentWeek) - 1);
            }
        };

        week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leaveWeek = weeks.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                leaveWeek = curentWeek;
            }
        });

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rType_1.getId()) {
                    leaveType = "1";
                } else if(checkedId == rType_2.getId()){
                    leaveType = "2";
                }else if(checkedId == rType_3.getId()) {
                    leaveType = "3";
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < courseList.getAdapter().getCount(); i++) {
                    String s = courseList.getAdapter().getItem(i).toString();
                    if(s.contains("true")) {
                        selectedCourse.add(s.substring(s.indexOf("id=") + 4, s.indexOf("id=") + 40));
                    }
                }
                for(int i = 0; i < selectedCourse.size(); i++){
                    System.out.println(selectedCourse.get(i));
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        try {
                            String leaveId = TMConnection.getInstance().postApply(leaveType, reason.getText().toString(), leaveWeek, selectedCourse);
                            leaveId = leaveId.substring(leaveId.indexOf("id") + 4, leaveId.indexOf("}"));
                            String teacherId = TMConnection.getInstance().getApprovers(leaveId);
                            System.out.println(teacherId + "-" + String.valueOf(teacherId.indexOf("id")) +String.valueOf(teacherId.indexOf("\"")));
                            teacherId = teacherId.substring(teacherId.indexOf("id") + 5, teacherId.lastIndexOf("\""));
                            System.out.println(leaveId+"!!"+teacherId + "??");
                            String rlst = TMConnection.getInstance().toSubmit(leaveId, teacherId);
                            System.out.println(leaveId+"!!"+teacherId + "??" + rlst);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }
}

class Tweek {
    private String currentWeek;
    private String endWeek;
    private String startWeek;

    public Tweek(String currentWeek, String endWeek, String startWeek) {
        this.currentWeek = currentWeek;
        this.endWeek = endWeek;
        this.startWeek = startWeek;
    }

    public String getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(String currentWeek) {
        this.currentWeek = currentWeek;
    }

    public String getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(String endWeek) {
        this.endWeek = endWeek;
    }

    public String getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(String startWeek) {
        this.startWeek = startWeek;
    }
}