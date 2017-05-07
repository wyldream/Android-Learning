package com.example.lbj.createdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lbj.createdatabase.bean.InfoBean;
import com.example.lbj.createdatabase.dao.InfoDao;


//shift+F1定位到错误行
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Log.v("add","是不是级别的问题");
        //创建一个帮助类对象
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(mContext);
        //调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();

        //绑定点击事件
        findViewById(R.id.bt_add).setOnClickListener(this);
        findViewById(R.id.bt_del).setOnClickListener(this);
        findViewById(R.id.bt_update).setOnClickListener(this);
        findViewById(R.id.bt_query).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InfoDao infoDao = new InfoDao(mContext);
        switch (v.getId()){
            case R.id.bt_add:
                Log.d("add","干你妈的，到底能不能输出");
                InfoBean bean = new InfoBean();
                bean.name = "张三";
                bean.phone ="110";
                infoDao.add(bean);

                InfoBean bean1 = new InfoBean();
                bean1.name = "李四";
                bean1.phone ="120";
                infoDao.add(bean1);
                break;
            case R.id.bt_del:

                infoDao.del("张三");
                break;

            case R.id.bt_update:

                InfoBean bean2 = new InfoBean();
                bean2.name = "张三";
                bean2.phone ="119";
                infoDao.update(bean2);
                break;


            case R.id.bt_query:
                infoDao.query("张三");
                infoDao.query("李四");
                break;

            default:
                break;
        }
    }
}
