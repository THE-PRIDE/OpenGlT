package com.mengyu.opengl2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.meng.openglt.R;
import com.mengyu.MyApplication;
import com.mengyu.retrofitUtils.NotifyMessage;
import com.mengyu.retrofitUtils.model.BroadMsg;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {


    //    String absolutePath = Environment.getExternalStorageDirectory() + "/0000/纪念章";
    String absolutePath = Environment.getExternalStorageDirectory() + "/1111/金条";

    private void deleteOldFile(File modelPath) {
        if (modelPath.isDirectory()) {
            File[] files = modelPath.listFiles();
            for (File f : files) {
                deleteOldFile(f);
            }
            boolean a = modelPath.delete();
        } else if (modelPath.exists()) {
            boolean b = modelPath.delete();
        }
    }

    public String getInetAddress(String host) {
        String ipAddress = "";
        InetAddress result;

        try {
            result = InetAddress.getByName(host);
            if (null != result) {
                return result.getHostAddress();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Runtime r = Runtime.getRuntime();
        int M = 1024*1024;
        Log.e("TEST","最大可用内存"+r.maxMemory()/M+"M");
        Log.e("TEST","当前可用内存"+r.totalMemory()/M+"M");
        Log.e("TEST","当前空闲内存"+r.freeMemory()/M+"M");
        Log.e("TEST","当前已使用内存"+((r.totalMemory()/M)-(r.freeMemory()/M))+"m");
//        localBroadcastManager = LocalBroadcastManager.getInstance(this);
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(NotifyMessage.AUTH_MSG_ON_LOGIN);
//        localBroadcastManager.registerReceiver(receiver,filter);
//
//        new Thread(new Runnable() {
//            @Override
//
//            public void run() {
//                InetAddress ip = null;
//                try {
//                    ip = InetAddress.getByName("220.181.111.188");
//                    BroadMsg broadMsg = new BroadMsg(NotifyMessage.AUTH_MSG_ON_LOGIN);
//                    MyApplication.getInstance().sendBroadcast(broadMsg);
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//                Log.e("TEST", ip.getHostAddress());
//
//            }
//        }).start();


//        String savePath = Environment.getExternalStorageDirectory() + "/citicBank/vrmodel/" + "android_1540429200_3030600738" + ".zip";
//        String unZipPath = Environment.getExternalStorageDirectory() + "/citicBank/vrmodel/" + "android_1540429200_3030600738";
//
//        deleteOldFile(new File(savePath));
//        deleteOldFile(new File(unZipPath));


        Intent intent = new Intent(MainActivity.this, ModelActivity.class);
        Bundle b = new Bundle();
        b.putString("assetDir", "models");
//        b.putString("assetFilename", "xianglian.obj");
        b.putString("assetFilename", "jinianzhang_006.obj");
//        b.putString("assetFilename", "jinianzhang_006.obj");
//        String unZipPath = Environment.getExternalStorageDirectory() + "/0000/纪念章/jinianzhang_006.obj";
//        Log.e("TEST", unZipPath);
//        String fileName = getFileName(absolutePath);/**/
//        b.putString("currentDir", getFileName(absolutePath));

//        Log.e("TEST", unZipPath);
//        this.paramAssetDir = b.getString("assetDir");
//        this.paramAssetFilename = b.getString("assetFilename");
//        this.paramFilename = b.getString("uri");
        intent.putExtras(b);
        startActivity(intent);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(NotifyMessage.AUTH_MSG_ON_LOGIN)){
                Toast.makeText(MainActivity.this,"Login",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        localBroadcastManager.unregisterReceiver(receiver);
    }

    public static String getFileName(String fileAbsolutePath) {
        File file = new File(fileAbsolutePath);
        String fileName = "";

        if (!file.exists() || !file.isDirectory()) {
            return "";
        }
        File[] subFile = file.listFiles();

        if (subFile == null) {
            return "";
        }

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                fileName = subFile[iFileLength].getName();
//                fileName = fileName.endsWith(".obj") ?  "a" : "a";
                if (fileName.endsWith(".obj"))
                    break;
            }
        }
        return fileAbsolutePath + File.separator + fileName;
    }
}
