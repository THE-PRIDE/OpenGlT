package com.mengyu.okDownload;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LMY on 18/8/24.
 * 下载管理类
 */

public class DownloadManager {

    private static final AtomicReference<DownloadManager> INSTANCE = new AtomicReference<>();

    private HashMap<String, Call> downCalls;//用于存放各个下载的请求；
    private OkHttpClient mClient;

    public static DownloadManager getInstance() {
        for (; ; ) {
            DownloadManager current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new DownloadManager();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private DownloadManager() {
        downCalls = new HashMap<>();
        mClient = new OkHttpClient.Builder().build();
    }


    public void download(String url, DownloadObserver downloadObserver) {

        Observable.just(url)
//                .map(new )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(downloadObserver);
    }

    public void cancel(String url) {
        Call call = downCalls.get(url);
        if (call != null) {
            call.cancel();
        }
        downCalls.remove(url);
    }

    private DownloadInfo createDownInfo(String url) {
        DownloadInfo downloadInfo = new DownloadInfo(url);
        long contentLength = 1;
        downloadInfo.setTotal(contentLength);
        String fileName = url.substring(url.lastIndexOf("/"));
        downloadInfo.setFileName(fileName);
        return downloadInfo;
    }

    private DownloadInfo getRealFileName(DownloadInfo downloadInfo) {
        String fileName = downloadInfo.getFileName();
        long downloadLength = 0, contentLength = downloadInfo.getTotal();
        File file = new File("", fileName);
        if (file.exists()) {
            //找到了文件，代表已经下载过，则获取其长度
            downloadLength = file.length();
        }
        //之前下载过，需要重新下一个
        int i = 1;
        while (downloadLength >= contentLength) {
            int dotIndex = fileName.lastIndexOf(".");
            String fileNameOther;
            if (dotIndex == -1) {
                fileNameOther = fileName + "(" + i + ")";
            }else {
                fileNameOther = fileName.substring(0,dotIndex)
                        + "(" + i + ")" + fileName.substring(dotIndex);
            }

            File newFile = new File("",fileNameOther);
            file = newFile;
            downloadLength = newFile.length();
            i++;
        }

        //设置改变过的文件名的大小
        downloadInfo.setProgress(downloadLength);
        downloadInfo.setFileName(file.getName());
        return downloadInfo;
    }

    private long getContentLength(String downloadUrl){
        Request request = new Request.Builder().url(downloadUrl).build();

        try {
            Response response = mClient.newCall(request).execute();
            if (response != null && response.isSuccessful()){
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength == 0 ? DownloadInfo.TOTAL_ERROR : contentLength;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return DownloadInfo.TOTAL_ERROR;
    }
}
