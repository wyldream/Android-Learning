package com.example.lbj.callphone;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //找到需要使用的控件
        final EditText et_number = (EditText) findViewById(R.id.et_number);
        Button bt_callphone = (Button) findViewById(R.id.bt_callphone);

        //设置按钮点击事件
        assert bt_callphone != null;
        bt_callphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取用户输入的电话号码
                assert et_number != null;
                String number = et_number.getText().toString().trim();
                //拨打电话号码
                Intent intent = new Intent();//创建一个意图对象
                intent.setAction(Intent.ACTION_CALL);//设置意图对象的动作
                intent.setData(Uri.parse("tel:"+number));//设置意图对象的数据（设置拨打电话的号码）
                startActivity(intent);//去启动一个意图对象
            }
        });

    }


}
