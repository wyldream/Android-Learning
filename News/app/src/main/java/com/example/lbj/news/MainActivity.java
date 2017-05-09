package com.example.lbj.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lbj.news.adapt.NewsAdapter;
import com.example.lbj.news.bean.NewsBean;
import com.example.lbj.news.utils.NewsUtils;

import java.io.LineNumberReader;
import java.util.ArrayList;
//新闻网络版
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView lv_news;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ArrayList<NewsBean> allNews = (ArrayList<NewsBean>) msg.obj;
            if(allNews != null&&allNews.size() > 0){
                NewsAdapter newsAdapter = new NewsAdapter(allNews,mContext);
                lv_news.setAdapter(newsAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //1、找到新闻控件
        lv_news = (ListView) findViewById(R.id.lv_news);
        //2.取数据库中获取缓存的数据
        ArrayList<NewsBean> allnews_database = NewsUtils.getAllNewsForDatabase(mContext);

        //System.out.println("A");
        if(allnews_database != null&&allnews_database.size() > 0){
            //若数据库中有数据，将其加载到页面中
            NewsAdapter newsAdapter = new NewsAdapter(allnews_database,mContext);
            lv_news.setAdapter(newsAdapter);
        }
        //2.请求服务器，获取新的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<NewsBean> allNews = NewsUtils.getAllNewsForNetWork(mContext);
                Message msg = Message.obtain();
                System.out.println("B"+allNews);
                msg.obj = allNews;
                handler.sendMessage(msg);
            }
        }).start();

    }

    //给条目设置点击事件（实现一个OnItemClickListener接口）
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        NewsBean bean = (NewsBean)adapterView.getItemAtPosition(i);
        String news_url = bean.news_url;
        //跳转
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(news_url));
        startActivity(intent);
    }
}
