package com.example.lbj.news.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lbj.news.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by LBJ on 2017/5/8.
 */
public class NewsDaoUtils {
    private NewsOpenHelper newsOpenHelper;

    public NewsDaoUtils(Context context){
        newsOpenHelper = new NewsOpenHelper(context);
    }

    //删除数据库中缓存的旧数据
    public void delete(){
        SQLiteDatabase db = newsOpenHelper.getReadableDatabase();
        db.delete("news",null,null);
        db.close();
    }

    public void saveNews(ArrayList<NewsBean> list){
        SQLiteDatabase db = newsOpenHelper.getReadableDatabase();
        for(NewsBean newsBean : list){
            //ContentValues类和Hashtable比较类似，它也是负责存储一些名值对，
            // 但是它存储的名值对当中的名是一个String类型，而值都是基本类型
            ContentValues values = new ContentValues();
            values.put("_id", newsBean.id);
            values.put("title", newsBean.title);
            values.put("des", newsBean.des);
            values.put("icon_url", newsBean.icon_url);
            values.put("news_url", newsBean.news_url);
            values.put("type", newsBean.type);
            values.put("time", newsBean.time);
            values.put("comment", newsBean.comment);
            db.insert("news", null, values);
        }
        db.close();
    }
    //从数据库中获取缓存的新闻数据
    public ArrayList<NewsBean> getNews(){
        ArrayList<NewsBean> list = new ArrayList<NewsBean>();
        SQLiteDatabase db = newsOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from news", null);
        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()){

                NewsBean newsBean = new NewsBean();
                newsBean. id = cursor.getInt(0);
                newsBean. title = cursor.getString(1);
                newsBean. des =	cursor.getString(2);
                newsBean. icon_url =	cursor.getString(3);
                newsBean. news_url =	cursor.getString(4);
                newsBean. 	type = cursor.getInt(5);
                newsBean. time =	cursor.getString(6);
                newsBean. 	comment = cursor.getInt(7);

                list.add(newsBean);
            }
        }
        db.close();
        cursor.close();
        return list;
    }
}
