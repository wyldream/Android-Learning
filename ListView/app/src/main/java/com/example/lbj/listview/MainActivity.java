package com.example.lbj.listview;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * listview 滚动列表
 */
public class MainActivity extends AppCompatActivity {

    private Context mContext;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //1、找到listview控件
        ListView lv_simple = (ListView) findViewById(R.id.lv_simple);
        //2、创建listAdapter对象
        MyListAdapter listAdapter = new MyListAdapter();
        //3、将adapter对象设置给listview
        lv_simple.setAdapter(listAdapter);
    }


    //导入（赋写）方法alt+insert
    //创建一个adapt类继承自BaseAdapter
    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            //告诉listview显示多少个条目
            return 20;
        }

        //根据postion获取listview上条目对应的Bean数据，该方法不影响数据的展示，可以先不实现
        @Override
        public Object getItem(int i) {
            return null;
        }

        //getItemId:用来获取条目postion行的id，该方法不影响数据的展示，可以先不实现
        @Override
        public long getItemId(int i) {
            return 0;
        }

        //getview:告诉listview条目上显示的内容；返回一个View对象作为条目上的内容展示，
        //该方法返回什么样的view,Listview的条目上就显示什么样的view。必须实现
        //屏幕上每显示一个条目getview方法就会被调用一次;convertView:曾经使用过的view对象，可以被重复使用,使用前要判断。
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            TextView view = null;
            if(convertView!=null){//判断是否创建过view，若创建过就不再创建，避免创建过多view占用内存
                view = (TextView)convertView;
            }else{//为创建过view，创建新的view
                view = new TextView(mContext);
            }
            view.setText("position"+i);//设置view的值
            view.setTextSize(40);//设置view的大小

            return view;
        }
    }
}
