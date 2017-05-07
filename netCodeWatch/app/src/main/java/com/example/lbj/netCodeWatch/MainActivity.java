package com.example.lbj.netCodeWatch;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 网络源码查看器
 * Android中耗时的操作（请求网络，数据库操作等）应该在子线程中去操作
 * 子线程中不能对UI进行修改
 * 所以需要handler在子线程和主线程之间进行消息传递
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_url;
    private TextView tv_source;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //一、获取控件
        et_url = (EditText) findViewById(R.id.et_url);
        Button bt_looksource = (Button) findViewById(R.id.bt_looksource);
        tv_source = (TextView) findViewById(R.id.tv_source);
        //二、绑定点击事件
        bt_looksource.setOnClickListener(this);
        System.out.println("oncreate方法线程："+Thread.currentThread().getName());
    }

    //使用handler在子线程和主线程之间传递消息的步骤
    //a.在主线程中创建一个handler对象
    private Handler handler = new Handler(){
        //b重写handler对象中的handlerMessage方法，用来接收子线程中发来的消息
        @Override
        public void handleMessage(Message msg) {
            //e.接收子线程发来的消息，处理消息
            String result = (String)msg.obj;
            System.out.println("获取到信息");
            //五、更新UI
            tv_source.setText(result);
        }
    };

    @Override
    public void onClick(View view) {
        //ctrl+alt+t快速添加try catch块
        try {
            //三、获取输入的url地址
            final String url_str = et_url.getText().toString().trim();
            if(TextUtils.isEmpty(url_str)){
                //???
                Toast.makeText(mContext,"url不能为空",Toast.LENGTH_SHORT).show();
                return ;
            }

            System.out.println("onclick方法线程："+Thread.currentThread().getName());

            //四、创建一个子线程做网络请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("oclick方法runnable线程："+Thread.currentThread().getName());
                        //使用UrlConnection请求一个url地址获取内容
                        //1.根据url地址创建一个URL对象
                        URL url = new URL(url_str);
                        //2.获取一个URLConnection对象
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(1000*10);
                        System.out.println("第一次请求的请求码"+connection.getResponseCode());
                        //获取头部信息，获取到重定向后的地址（为了解决返回码为302的问题）
                        String location = connection.getHeaderField("Location");
                        System.out.println("location "+location+"");
                        //3.为URLConnection设置请求参数（请求方式，连接的超时时间等）
                        url  = new URL(location);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(1000*10);
                        //4.判断响应码
                        int code = connection.getResponseCode();
                        System.out.println(code);
                        if(code == 200){
                            //5.获取有效数据，并将数据解析为字符串
                            InputStream inputStream = connection.getInputStream();
                            String result = StreamUtils.streamToString(inputStream);
                            //c.子线程中创建一个Message对象，用于将子线程中的数传递给主线程
                            Message message = new Message();
                            message.obj = result;
                            //d.使用handler将数据从子线程传递到主线程
                            handler.sendMessage(message);
                        }else{
                            System.out.println("请求url失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
