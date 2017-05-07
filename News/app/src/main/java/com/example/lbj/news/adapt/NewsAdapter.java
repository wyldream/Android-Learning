package com.example.lbj.news.adapt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lbj.news.R;
import com.example.lbj.news.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by LBJ on 2017/5/6.
 */
public class NewsAdapter extends BaseAdapter{

    private ArrayList<NewsBean> list;
    private Context context;

    public NewsAdapter(ArrayList<NewsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    //返回条目的返回信息（给条目设置点击事件时会用到）
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    //返回条目的位置
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        //1、通过服用convertView创建一个view，做为返回条目
        if(convertView != null){
            view=convertView;
        }else{
            //context:上下文, resource:要转换成view对象的layout的id, root:将layout用root(ViewGroup)包一层作为codify的返回值,一般传null
     		view = View.inflate(context, R.layout.tems_news_layout, null);//将一个布局文件转换成一个view对象
            //通过context获取服务得到LayoutInflate对象，通过LayoutInflate对象将xml文件转换为view对象
            //LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            //view = layoutInflater.inflate(R.layout.tems_news_layout,null);
        }
        //2、获取控件(ctrl+alt+v获取返回类型快捷键)
        ImageView item_img_icon = (ImageView) view.findViewById(R.id.item_img_icon);
        TextView item_tv_des = (TextView) view.findViewById(R.id.item_tv_des);
        TextView item_tv_title = (TextView) view.findViewById(R.id.item_tv_title);
        //3、获取position位置的条目对应的list集合中的新闻数据，Bean对象
        NewsBean newsBean = list.get(i);
        //4、将内容和控件绑定
        item_img_icon.setImageDrawable(newsBean.icon);
        item_tv_title.setText(newsBean.title);
        item_tv_des.setText(newsBean.des);
        //这里注意一定要改返回值，否则报错，且不会定位错误
        return view;
    }
}
