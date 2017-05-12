package com.example.lbj.rpcalc;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_name;
    private RadioGroup rg_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_name = (EditText) findViewById(R.id.et_name);
        rg_group = (RadioGroup) findViewById(R.id.rg_group);
    }

    public void click(View v){
        //一获取姓名
        String name = et_name.getText().toString().trim();
        //二获取性别代号
        int sexId = rg_group.getCheckedRadioButtonId();
        //System.out.println(sexId);
        //三判断性别
        int sex = 0;
        switch (sexId){
            case R.id.rb_male:
                sex=1;
                break;
            case R.id.rb_female:
                sex = 2;
                break;
            case R.id.rb_other:
                sex = 3;
                break;
        }
        if(sex == 0){
            Toast.makeText(getApplicationContext(), "请选择性别 ", Toast.LENGTH_LONG).show();
        }
        //四设置意图
        Intent intent = new Intent(this, ResultActive.class);
        //设置数据
        intent.putExtra("name",name);
        intent.putExtra("sex",sex);
        //五跳转
        startActivity(intent);
        //
    }
}
