package com.example.lbj.netCodeWatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LBJ on 2017/5/7.
 */
//将字节流转换为字符串
public class StreamUtils {
    public static String streamToString(InputStream in){
        String result = "";
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = in.read(buffer)) != -1){
                out.write(buffer,0,length);
                out.flush();
            }
            result = out.toString();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;


    }
}
