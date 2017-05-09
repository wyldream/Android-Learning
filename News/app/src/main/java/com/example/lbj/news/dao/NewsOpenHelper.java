package com.example.lbj.news.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LBJ on 2017/5/8.
 */
//数据库操作帮助类
public class NewsOpenHelper extends SQLiteOpenHelper{
    public NewsOpenHelper(Context context) {
        super(context, "heimanews1", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        db.execSQL("create table news (_id integer  ,title varchar(200),des varchar(300),icon_url varchar(200),news_url varchar(200)," +
                " type integer , time varchar(100),comment integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
