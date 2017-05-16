package com.example.lbj.readContactList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LBJ on 2017/5/16.
 */
public class QueryContactsUtils {
    public static List<Contact> queryContacts(Context context){
        List<Contact> contactLists = new ArrayList<>();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"contact_id"}, null, null,null);
        while(cursor.moveToNext()){
            //查询联系人
            String contact_id = cursor.getString(0);
            if(contact_id!=null){
                Contact contact = new Contact();
                contact.setId(contact_id);
                //查询联系人包含的具体信息
                Cursor cursor1 = context.getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{contact_id}, null);
                while(cursor1.moveToNext()){
                    String data1 = cursor1.getString(0);
                    String mimetype = cursor1.getString(1);
                    //[3]根据mimetype 区分data1列的数据类型
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        contact.setName(data1);
                    }else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        contact.setPhone(data1);
                    }else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
                        contact.setEmail(data1);
                    }
                }
                contactLists.add(contact);

            }
        }

        return contactLists;
    }
}
