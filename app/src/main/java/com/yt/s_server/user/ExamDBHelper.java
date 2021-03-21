package com.yt.s_server.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamDBHelper extends SQLiteOpenHelper {
    public ExamDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table classroom(" +
                "id_key integer primary key autoincrement," +
                "num varchar(50)," +
                "college varchar(50)," +
                "id varchar(50)," +
                "course varchar(50)," +
                "startWeek varchar(50)," +
                "endWeek varchar(50)," +
                "time varchar(200)," +
                "classroom varchar(50)," +
                "seat varchar(50)," +
                "remarks varchar(50)," +
                "delay varchar(50)," +
                "teacher varchar(50))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
