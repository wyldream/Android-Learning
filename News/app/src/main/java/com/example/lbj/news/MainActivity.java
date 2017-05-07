package com.example.lbj.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //1、找到新闻控件
        ListView lv_news = (ListView) findViewById(R.id.lv_news);
        //2、获取新闻内容list集合
        ArrayList<NewsBean> news = NewsUtils.getAllNews(mContext);
        //3、将adapt设置到listView中
        NewsAdapter newsAdapter = new NewsAdapter(news, mContext);
        //if(lv_news!=null)
        lv_news.setAdapter(newsAdapter);
        //4、设置条目点击事件
        lv_news.setOnItemClickListener(this);

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
