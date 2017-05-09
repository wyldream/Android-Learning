package com.example.lbj.news.utils;

import android.content.Context;

import com.example.lbj.news.R;
import com.example.lbj.news.bean.NewsBean;
import com.example.lbj.news.dao.NewsDaoUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by LBJ on 2017/5/6.
 */
public class NewsUtils {
    //将数据封装到list中并返回
    public static String newsPath_url = "http://192.168.43.245:8080/itheima74/servlet/GetNewsServlet";
    //请求服务器获取新闻数据
    public static ArrayList<NewsBean> getAllNewsForNetWork(Context context){
        ArrayList<NewsBean> arrayList = new ArrayList<NewsBean>();
        try {
            URL url = new URL(newsPath_url);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10*1000);
            int code = connection.getResponseCode();
            //System.out.println("C    "+code);
            if(code == 200){
                //获取请求到的新闻数据到List集合中
                InputStream inputStream = connection.getInputStream();
                String result = StreamUtils.streamToString(inputStream);
                //返回的是JSON对象，解析JSON获取的新闻数据到List集合中
                JSONObject root_json = new JSONObject(result);
                System.out.println("E         "+root_json);
                JSONArray jsonArray = root_json.getJSONArray("newss");
                for (int i = 0 ;i < jsonArray.length();i++){//循环遍历jsonArray
                    JSONObject news_json = jsonArray.getJSONObject(i);//获取一条新闻的json

                    NewsBean newsBean = new NewsBean();

                    newsBean. id = news_json.getInt("id");
                    newsBean. comment = news_json.getInt("comment");//评论数
                    newsBean. type = news_json.getInt("type");//新闻的类型，0 ：头条 1 ：娱乐 2.体育
                    newsBean. time = news_json.getString("time");
                    newsBean. des = news_json.getString("des");
                    newsBean. title = news_json.getString("title");
                    newsBean. news_url = news_json.getString("news_url");
                    newsBean. icon_url = news_json.getString("icon_url");

                    System.out.println("D         "+newsBean);
                    arrayList.add(newsBean);

                }
                //3.清楚数据库中的旧数据，将新的数据缓存到数据库中
                new NewsDaoUtils(context).delete();
                new NewsDaoUtils(context).saveNews(arrayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;

    }

    //从数据库中获取上次缓存的新闻数据做listview的展示
    public  static ArrayList<NewsBean> getAllNewsForDatabase(Context context) {

        return new NewsDaoUtils(context).getNews();

    }
}
