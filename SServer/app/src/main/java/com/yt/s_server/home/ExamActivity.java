package com.yt.s_server.home;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;

import com.yt.s_server.R;
import com.yt.s_server.user.Exam;
import com.yt.s_server.user.ExamDBHelper;

import java.util.ArrayList;

public class ExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("考试信息");
        tv_title.setGravity(Gravity.CENTER);

        ListView examList = findViewById(R.id.ll_exam);

        try{
            ExamDBHelper dbHelper = new ExamDBHelper(ExamActivity.this,"ESC", null, 1);
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
            ArrayList<Exam> exams = new ArrayList<Exam>();
            Cursor cursor =  sqliteDatabase.rawQuery("select * from classroom", new String[]{});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String num = cursor.getString(1);
                String college = cursor.getString(2);
                String id = cursor.getString(3);
                String course = cursor.getString(4);
                String startWeek = cursor.getString(5);
                String endWeek = cursor.getString(6);
                String time = cursor.getString(7);
                String classroom = cursor.getString(8);
                String seat = cursor.getString(9);
                String remarks = cursor.getString(10);
                String delay = cursor.getString(11);
                String teacher = cursor.getString(12);
                cursor.moveToNext();
                exams.add(new Exam(num, college, id, course, startWeek, endWeek, time, classroom, seat, remarks, delay, teacher));
            }
            cursor.close();
            ExamAdapter examAdapter = new ExamAdapter(ExamActivity.this, R.layout.exam_view, exams, false);
            examList.setAdapter(examAdapter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
