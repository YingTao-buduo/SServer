package com.yt.s_server.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yt.s_server.R;
import com.yt.s_server.user.Score;
import com.yt.s_server.user.ScoreDBHelper;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    ListView scoreList;
    TextView totalCredit;
    TextView gpa;
    Button chooseTerm;

    AlertDialog alertDialog3;
    ArrayList<Score> scores;
    ArrayList<Score> scores_0;
    ScoreAdapter scoreAdapter;

    int creditSum = 0;
    float _gpa = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreList = findViewById(R.id.ll_score);

        totalCredit = findViewById(R.id.tv_total_credit);
        gpa = findViewById(R.id.tv_gpa);
        chooseTerm = findViewById(R.id.btn_choose_term);

        try{
            ScoreDBHelper dbHelper = new ScoreDBHelper(ScoreActivity.this,"ESS", null, 1);
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
            scores = new ArrayList<>();
            scores_0 = new ArrayList<>();
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
                creditSum += credit;
                _gpa += (credit * mark);
                scores.add(new Score(year, term, id, name, type_1, type, credit, mark, gp, type_2));
                scores_0.add(new Score(year, term, id, name, type_1, type, credit, mark, gp, type_2));
            }
            cursor.close();
            totalCredit.setText("学分：" + String.valueOf(creditSum));
            gpa.setText("成绩：" + String.valueOf(_gpa / creditSum));
            scoreAdapter = new ScoreAdapter(ScoreActivity.this, R.layout.view_score, scores, false);
            scoreList.setAdapter(scoreAdapter);
        } catch (Exception e){
            e.printStackTrace();
        }

        chooseTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = new ArrayList<Score>(scores_0);
                System.out.println(scores.size());
                System.out.println(scoreAdapter.getCount());
                showMultiAlertDialog(v);
            }
        });
    }

    public void showMultiAlertDialog(View view){
        final String[] items = {"大一上学期", "大一下学期", "大二上学期", "大二下学期", "大三上学期", "大三下学期", "大四上学期", "大四下学期"};
        final ArrayList<Integer> terms = new ArrayList<>();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择学期");
        alertBuilder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked){
                    terms.add(i);
                }else {
                    for(int j = 0; j < terms.size(); j++){
                        if(terms.get(j) == i){
                            terms.remove(j);
                            break;
                        }
                    }
                }
            }
        });

        final SharedPreferences sharedPreferences = getSharedPreferences("studentInfo", MODE_PRIVATE);
        String ln = sharedPreferences.getString("stuId", "");
        int userYear = Integer.parseInt(ln.substring(0, 2));
        final ArrayList<String> termNum = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            termNum.add("20" + String.valueOf(userYear + i) + "-20" + String.valueOf(userYear + 1 + i) + "1");
            termNum.add("20" + String.valueOf(userYear + i) + "-20" + String.valueOf(userYear + 1 + i) + "2");
        }




        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int count = 0;
                for(Score s : scores_0){
                    String termId = s.getYear()+s.getTerm();
                    boolean flag = true;
                    for(Integer t : terms){
                        if(termId.equals(termNum.get(t))){
                            count = count + 1;
                            flag = false;
                        }
                    }
                    if(flag){
                        scores.remove(count);
                        scoreAdapter.remove(s);
                        System.out.println(s.getName());
                    }
                }

                alertDialog3.dismiss();
                System.out.println(scoreAdapter.getCount());
                System.out.println(scores.size());
                scoreAdapter.notifyDataSetChanged();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog3.dismiss();
            }
        });

        alertDialog3 = alertBuilder.create();
        alertDialog3.show();
    }
}
