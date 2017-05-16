package com.example.lbj.download;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText et_threadCount;
    private static Context mContext;
    private LinearLayout ll_progress_layout;

    private  static int threadCount = 0;//开启3个线程
    private  static int blockSize = 0;//每个线程下载的大小
    private  static int runningTrheadCount = 0;//当前运行的线程数
    private  static String path = "http://192.168.43.245:8080/itheima74/feiq.exe";
    private static Map<Integer,ProgressBar> map = new HashMap<Integer, ProgressBar>();//用于存放进度条控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById(R.id.bt_download).setOnClickListener(this);
        ll_progress_layout = (LinearLayout) findViewById(R.id.ll_progress_layout);
        et_threadCount = (EditText) findViewById(R.id.et_threadCount);
    }

    @Override
    public void onClick(View view) {
        //获取用户输入的线程数
        String threadCount_str = et_threadCount.getText().toString().trim();
        if(threadCount_str != null&&threadCount_str.length() > 0){
            threadCount = Integer.parseInt(threadCount_str);
        }else{
            Toast.makeText(mContext,"请输入线程数",Toast.LENGTH_LONG);
        }

        //清空控件中的所有子控件
        ll_progress_layout.removeAllViews();

        //根据线程数添加相应数量的ProgressBar
        for(int i =0 ;i < threadCount;i++ ){

            ProgressBar progressbar = (ProgressBar) View.inflate(mContext, R.layout.child_progressbar_layout, null);
            map.put(i, progressbar);//将ProgressBar放到map中，方便在线程中获取并设置进度
            //child: 子控件
            ll_progress_layout.addView(progressbar);
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                download();
            }
        }).start();
    }

    public void download(){
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10*1000);
            int code = connection.getResponseCode();
            if(code == 200){
                //1、获取资源的大小
                int contentLength = connection.getContentLength();
                //2、在本地创建一个和要下载资源一样大小的文件
                //用RandomAccessFile创建一个文件可以实现暂停（断点续传功能）
                RandomAccessFile raf = new RandomAccessFile(new File(getFileName(path)), "rw");
                raf.setLength(contentLength);//设置文件大小

                //3、分配每个线程下载文件的开始和结束位置
                blockSize = contentLength/threadCount;//计算出每个线程要下载的大小
                int threadId;
                int startIndex = 0;
                int endIndex = 0;
                for(threadId=0;threadId<threadCount;threadId++){
                    //每个线程开始下载位置
                    startIndex = threadId * blockSize;
                    //每个线程结束下载位置
                    endIndex = (threadId+1) * blockSize-1;
                    //最后一个线程的结束位置要单独计算
                    if(threadId == threadId-1){
                        endIndex = contentLength - 1;
                    }
                    //4、开启线程执行下载
                    new DownloadThread(threadId, startIndex, endIndex).start();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //封装下载操作
    public class DownloadThread extends Thread{

        private int threadId;//线程ID
        private int startIndex;//开始下载位置
        private int endIndex;//结束位置
        private int lastPosition;//上次下载到的位置
        private int currentThreadTotalProgress;//

        public DownloadThread(int threadId, int startIndex, int endIndex) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.currentThreadTotalProgress = endIndex - startIndex +1;
        }

        @Override
        public void run() {
            //System.out.println("threadId     "+threadId);
            ProgressBar progressBar = map.get(threadId);
            synchronized (DownloadThread.class){
                runningTrheadCount = runningTrheadCount +1;//开启一线程，线程数加1
            }
            try {
                URL url = new URL(path);
                HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
                openConnection.setRequestMethod("GET");
                openConnection.setConnectTimeout(10*1000);

                //实现断点续传功能
                File file2 = new File(threadId + ".txt");//存放之前下载记录的文件
                if(file2.exists()){//当已经被下载一部分时
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file2)));
                    String lastPostion_str = bufferedReader.readLine();
                    //if(lastPostion_str != null&&lastPostion_str.length() > 0){
                        lastPosition = Integer.parseInt(lastPostion_str);//读取文件获取上次下载的位置
                    //}else{
                       // Toast.makeText(mContext,"请输入线程数",Toast.LENGTH_SHORT);
                   // }
                    //说明该线程已经下载完成
                    if(lastPosition == endIndex+1){

                        progressBar.setProgress(currentThreadTotalProgress);
                        runningTrheadCount = runningTrheadCount -1;

                    }

                    //设置分段下载的头信息。  Range:做分段数据请求用的。
                    //Connection.setRequestProperty("Range", "bytes:"+lastPostion+"-"+endIndex);//bytes:0-500:请求服务器资源中0-500之间的字节信息  501-1000:
                    System.out.println("实际下载：  线程："+threadId+"，开始位置："+lastPosition+";结束位置:"+endIndex);
                    bufferedReader.close();
                }else{//之前没有下载过
                    lastPosition = startIndex;
                    //设置分段下载的头信息。  Range:做分段数据请求用的。
                    //enConnection.setRequestProperty("Range", "bytes:"+lastPostion+"-"+endIndex);//bytes:0-500:请求服务器资源中0-500之间的字节信息  501-1000:
                    System.out.println("实际下载：  线程："+threadId+"，开始位置："+lastPosition+";结束位置:"+endIndex);
                }
                openConnection.setRequestProperty("Range","bytes:"+lastPosition+"-"+endIndex);

                if(openConnection.getResponseCode() ==206 ){
                    InputStream inputStream = openConnection.getInputStream();
                    RandomAccessFile randomAccessFile = new RandomAccessFile(new File(getFileName(path)), "rw");
                    randomAccessFile.seek(lastPosition);//设置随机文件从哪个位置开始写。
                    byte[] buffer = new byte[1024*1024];
                    int length = -1;
                    int total = 0;//记录本次线程下载的总大小

                    while((length= inputStream.read(buffer)) !=-1){
                        randomAccessFile.write(buffer, 0, length);
                        total = total+ length;
                        //去保存当前线程下载的位置，保存到文件中
                        int currentThreadPostion = lastPosition + total;//计算出当前线程本次下载到位置
                        //创建随机文件保存当前线程下载的位置
                        File file = new File(getFilePath()+threadId+".txt");
                        RandomAccessFile accessfile = new RandomAccessFile(file, "rwd");
                        accessfile.write(String.valueOf(currentThreadPostion).getBytes());
                        accessfile.close();
                        //计算线程下载的进度并设置进度
                        int currentprogress = currentThreadPostion - startIndex;
                        //System.out.println("progressBar      "+progressBar);
                        progressBar.setMax(currentThreadTotalProgress);//设置进度条的最大值
                        progressBar.setProgress(currentprogress);//设置进度条当前进度
                    }
                    //关闭相关的流信息
                    inputStream.close();
                    randomAccessFile.close();

                    System.out.println("线程："+threadId+"，下载完毕");

                    //当所有线程下载结束，删除存放下载位置的文件。
                    synchronized (DownloadThread.class) {
                        runningTrheadCount = runningTrheadCount -1;//标志着一个线程下载结束。
                        if(runningTrheadCount == 0 ){
                            runOnUiThread(new Runnable() {//不能在一个静态环境中运行
                                @Override
                                public void run() {

                                    Toast.makeText(mContext,"下载完成",Toast.LENGTH_LONG);
                                }
                            });
                            System.out.println("所有线程下载完成");
                            for(int i =0 ;i< threadCount;i++){
                                File file = new File(i+".txt");
                                System.out.println(file.getAbsolutePath());
                                file.delete();
                            }
                        }

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  static String getFileName(String url){

        return  Environment.getExternalStorageDirectory() + "/"+ url.substring(url.lastIndexOf("/"));

    }

    public static String getFilePath(){

        return Environment.getExternalStorageDirectory() + "/";
    }
}
