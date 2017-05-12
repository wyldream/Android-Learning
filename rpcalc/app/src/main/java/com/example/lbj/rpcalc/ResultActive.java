package com.example.lbj.rpcalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;

/**
 * Created by LBJ on 2017/5/12.
 */
public class ResultActive extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加载页面
        setContentView(R.layout.activity_result);
        // [1]找到控件
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        TextView tv_sex = (TextView) findViewById(R.id.tv_sex);
        TextView tv_score = (TextView) findViewById(R.id.tv_score);
        TextView tv_result = (TextView) findViewById(R.id.tv_result);
        //2获取开启次Activity的意图对象
        Intent intent = getIntent();
        //获取数据
        String name = intent.getStringExtra("name");
        byte[] bytes = null;
        try {
            bytes = name.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tv_name.setText(name);
        int sex = intent.getIntExtra("sex",0);
        System.out.println(sex);
        //显示性别
        switch (sex){
            case 1:
                tv_sex.setText("男");
                break;
            case 2:
                tv_sex.setText("女");
                break;
            case 3:
                tv_sex.setText("人妖");
                break;
        }
        //根据姓名计算得分
        int  total = 0;
        for (byte b : bytes) {     //0001 1000
            int number =b&0xff;             //1111 1111
            total+=number;
        }
        //算出得分
        int score = Math.abs(total)%100;
        tv_score.setText("您的的人品得分为："+score);
        if (score >90) {
            tv_result.setText("您的人品非常好 您家的祖坟都冒青烟了");
        }else if (score >70) {
            tv_result.setText("有你这样的人品算是不错了..");
        }else if (score >60) {
            tv_result.setText("您的人品刚刚及格");
        }else{
            tv_result.setText("您的人品不及格....");

        }
    }
}
