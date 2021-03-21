package com.yt.s_server.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDBHelper extends SQLiteOpenHelper {
    public ScoreDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句并执行
        String sql="create table score(" +
                "id_key integer primary key autoincrement," +
                "year varchar(20)," +
                "term varchar(20)," +
                "id varchar(20)," +
                "name varchar(50)," +
                "type_1 varchar(20)," +
                "type varchar(20)," +
                "credit integer," +
                "mark integer," +
                "gp float," +
                "type_2 varchar(20))";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
