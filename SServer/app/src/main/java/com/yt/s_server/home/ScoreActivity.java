package com.yt.s_server.home;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.yt.s_server.R;
import com.yt.s_server.user.Score;
import com.yt.s_server.user.ScoreDBHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    ListView scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("成绩查询");
        tv_title.setGravity(Gravity.CENTER);

        scoreList = findViewById(R.id.ll_score);

        try{
            ScoreDBHelper dbHelper = new ScoreDBHelper(ScoreActivity.this,"ESS", null, 1);
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
            ArrayList<Score> scores = new ArrayList<Score>();
            Cursor cursor =  sqliteDatabase.rawQuery("select * from score", new String[]{});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String year = cursor.getString(1);
                String term = cursor.getString(2);
                String id = cursor.getString(3);
                String name = cursor.getString(4);
                String type_1 = cursor.getString(5);
                String type = cursor.getString(6);
                int credit = cursor.getInt(7);
                int mark = cursor.getInt(8);
                float gp = cursor.getFloat(9);
                String type_2 = cursor.getString(10);
                cursor.moveToNext();
                scores.add(new Score(year, term, id, name, type_1, type, credit, mark, gp, type_2));
            }
            cursor.close();
            ScoreAdapter scoreAdapter = new ScoreAdapter(ScoreActivity.this, R.layout.score_view, scores, false);
            scoreList.setAdapter(scoreAdapter);
        } catch (Exception e){

        }
    }
}
