package com.example.lbj.news.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by LBJ on 2017/5/9.
 */
//自定义的ImgView控件,用于展示从网络上获取的图片
//继承一个ImageView并实现它的三个构造方法
public class MyImageView extends ImageView{

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context) {
        super(context);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;

            setImageBitmap(bitmap);
        }
    };

    public void setImageUrl(final String url_str){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(url_str);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setConnectTimeout(10*1000);
                    connection.setRequestMethod("GET");
                    int code = connection.getResponseCode();
                    if(code == 200){
                        InputStream inputStream =connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        handler.sendMessage(msg);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
