package com.example.lbj.createdatabase.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

import com.example.lbj.createdatabase.MySqliteOpenHelper;
import com.example.lbj.createdatabase.bean.InfoBean;


/**
 * Created by LBJ on 2017/5/2.
 */
//封装数据库操作类
public class InfoDao {
    private MySqliteOpenHelper mySqliteOpenHelper;
    public InfoDao(Context context){
        //创建一个帮助类对象
        mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    public void add(InfoBean bean){
        //执行sql语句需要sqliteDatabase对象
        //调用getReadableDatabase（或getWriteableDatabase）方法来获取SQLiteDatabase对象
        Log.v("add","A");
        System.out.println("add");
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        Log.d("add","B");
        //执行插入语句，new Object[]{bean.name,bean.phone}创建一个数组并赋初值
        db.execSQL("insert into info(name,phone) values(?,?);",new Object[]{bean.name,bean.phone});
        //关闭数据库对象
        db.close();
    }

    public void del(String name){
        //执行sql语句需要sqliteDatabase对象
        //Log.d()
        //调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        System.out.println("B");
        //sql:sql语句，  bindArgs：sql语句中占位符的值
        db.execSQL("delete from info where name=?;", new Object[]{name});
        //关闭数据库对象
        db.close();
    }

    public void update(InfoBean bean){
        //执行sql语句需要sqliteDatabase对象
        //调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        //sql:sql语句，  bindArgs：sql语句中占位符的值
        db.execSQL("update info set phone=? where name=?;", new Object[]{bean.phone,bean.name});
        //关闭数据库对象
        db.close();
    }

    public void query(String name){
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select _id,name,phone from info where name = ?;", new String []{name});
        //
        if(cursor != null && cursor.getColumnCount() > 0){
            while(cursor.moveToNext()){//循环遍历获取每一行的内容
                int id = cursor.getInt(0);
                String name_str = cursor.getString(1);
                String phone = cursor.getString(2);
                System.out.println("_id"+id+";name"+name_str+";phone"+phone);
            }
        }
        db.close();
    }
}
