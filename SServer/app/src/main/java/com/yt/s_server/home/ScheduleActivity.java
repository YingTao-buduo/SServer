package com.yt.s_server.home;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yt.s_server.R;

import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private Handler handler;
    private int screenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("课程表");
        tv_title.setGravity(Gravity.CENTER);

        /*------------获取屏幕的高度和宽度----------------*/
        //定义DisplayMetrics 对象
        //getActivity().setContentView(R.layout.fragment_wo_de_ke_biao);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        screenWidth = dm.widthPixels;

        /*------------绘制背景表格----------------*/
        LinearLayout table = findViewById(R.id.ll_table);
        //循环表格为13行
        for (int i=0;i<13;i++) {
            //new 一个线性布局用来画每一行，设置它为水平
            LinearLayout varlayout = new LinearLayout(this);
            varlayout.setOrientation(LinearLayout.HORIZONTAL);
            //循环它每一行显示7条数据
            for (int j = 0; j < 7; j++) {
                TextView text = new TextView(this);
                text.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                final float scale = getResources().getDisplayMetrics().density;
                text.setHeight((int)(65 *scale));  //设置高度
                varlayout.addView(text);//添加到水平线性布局
                TextView reit = new TextView(this);
                //画表格竖线
                reit.setLayoutParams(new LinearLayout.LayoutParams(1,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                reit.setBackgroundColor(Color.rgb(128,189,171));
                varlayout.addView(reit);//把他添加到水平线性布局里
            }
            TextView reit = new TextView(this);
            //画横线
            reit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            reit.setBackgroundColor(Color.rgb(128,189,171));
            table.addView(varlayout, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            table.addView(reit);
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = TMConnection.getInstance().getSchedule();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                final RelativeLayout mon = findViewById(R.id.rl_monday);
                final RelativeLayout tue = findViewById(R.id.rl_tuesday);
                final RelativeLayout wed = findViewById(R.id.rl_wednesday);
                final RelativeLayout thu = findViewById(R.id.rl_thursday);
                final RelativeLayout fri = findViewById(R.id.rl_friday);
                final RelativeLayout sat = findViewById(R.id.rl_saturday);
                final RelativeLayout sun = findViewById(R.id.rl_sunday);

                super.handleMessage(msg);
                String json = msg.obj.toString();

                JsonObject schedulesJson = new JsonParser().parse(json).getAsJsonObject();
                JsonObject term = new JsonParser().parse(schedulesJson.get("term").toString()).getAsJsonObject();
                int currenWeek = term.get("currentWeek").getAsInt();
                //int week = 14;

                List<Schedule> schedules = new Gson().fromJson(schedulesJson.get("schedules"), new TypeToken<List<Schedule>>(){}.getType());

                for(Schedule s : schedules){
                    if(Integer.parseInt(s.getEndWeek()) < currenWeek)
                        continue;
                    if(Integer.parseInt(s.getOddEven()) != 0)
                        if(Integer.parseInt(s.getOddEven()) % 2 != (currenWeek % 2))
                            continue;
                    View view = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.course_block, null);
                    TextView courseName = view.findViewById(R.id.tv_courseName);
                    TextView type = view.findViewById(R.id.tv_type);
                    TextView classroom = view.findViewById(R.id.tv_classroom);
                    TextView teacher = view.findViewById(R.id.tv_teacher);
                    courseName.setText(s.getCourse());
                    type.setText(s.getCourseItem());
                    classroom.setText(s.getPlace());
                    teacher.setText(s.getTeacherName());
                    LinearLayout linearLayout = view.findViewById(R.id.ll_course);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)linearLayout.getLayoutParams();
                    final float scale = getResources().getDisplayMetrics().density;
                    params.topMargin = (int)((65 * scale + 0.5) * (Integer.parseInt(s.getStartSection()) - 1));
                    params.height = (int)((65 * scale - 0.5) * Integer.parseInt(s.getTotalSection()));
                    params.width = (screenWidth - 24) / 7;
                    linearLayout.setLayoutParams(params);
                    linearLayout.setBackgroundColor(getColor(s.getPlace().substring(0, 3)));
                    switch (s.getDayOfWeek()){
                        case "1":
                            mon.addView(view);
                            break;
                        case "2":
                            tue.addView(view);
                            break;
                        case "3":
                            wed.addView(view);
                            break;
                        case "4":
                            thu.addView(view);
                            break;
                        case "5":
                            fri.addView(view);
                            break;
                        case "6":
                            sat.addView(view);
                            break;
                        case "7":
                            sun.addView(view);
                            break;
                    }
                }
            }
        };
    }

    private int getColor(String s){
        switch (s){
            case "丽泽楼":
                return Color.rgb(240,230,140);//卡其布
            case "金声楼":
                return Color.rgb( 	64,224,208);//宝石绿
            case "木铎楼":
                return Color.rgb(255,140,0);//深橙色
            case "弘文楼":
                return Color.rgb( 	255,192,203);//粉红
            case "元白楼":
                return Color.rgb(220,220,220);//亮灰色
            case "乐育楼":
                return Color.rgb( 	144,238,144);//淡绿色
            case "励耘楼":
                return Color.rgb(135,206,250);//淡蓝色
            case "图书馆":
                return Color.rgb(238,130,238);//紫罗兰
            case "励教楼":
                return Color.rgb(255,255,0);//黄色
            case "南曦园":
                return Color.rgb(244,164,96);//沙棕色
            default:
                return Color.rgb(105,105,105);//暗灰
        }
    }
}
