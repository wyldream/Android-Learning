package com.example.lbj.readContactList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Contact> queryContacts = QueryContactsUtils.queryContacts(getApplicationContext());
        for (Contact contact : queryContacts) {
            System.out.println("contat:"+contact);

        }

    }
}
