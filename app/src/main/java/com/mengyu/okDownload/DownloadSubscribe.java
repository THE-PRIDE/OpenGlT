package com.mengyu.okDownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LMY on 18/8/24.
 * <p>
 * 测试  subscribe
 */

public class DownloadSubscribe implements ObservableOnSubscribe<DownloadInfo> {

    private DownloadInfo downloadInfo;
    private OkHttpClient mClient;

    public DownloadSubscribe(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    @Override
    public void subscribe(ObservableEmitter<DownloadInfo> e) throws Exception {

        String url = downloadInfo.getUrl();
        long downloadLength = downloadInfo.getProgress();
        long contentLength = downloadInfo.getTotal();
        e.onNext(downloadInfo);

        Request request = new Request.Builder()
                //确定下载的范围，添加此头，服务器就可以跳过已经下载好的部分
                .addHeader("RANGE", "bytes = " + downloadLength + "-" + contentLength)
                .url(url)
                .build();

        Call call = mClient.newCall(request);
//        downCalls.put(url,call);//添加进call中,用于取消
        Response response = call.execute();

        File file = new File("", downloadInfo.getFileName());
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {

            inputStream = response.body().byteStream();
            fileOutputStream = new FileOutputStream(file, true);
            byte[] buffer = new byte[2048];//缓冲数据2KB
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
                downloadLength += len;
                downloadInfo.setProgress(downloadLength);
                e.onNext(downloadInfo);
            }

            fileOutputStream.flush();
//        downCall.remove(url);
        }finally {
            inputStream.close();
            fileOutputStream.close();
        }
        e.onComplete();
    }
}
