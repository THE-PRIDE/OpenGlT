package com.mengyu.retrofitUtils;

import android.util.Log;

import com.mengyu.MyApplication;
import com.mengyu.retrofitUtils.model.BroadMsg;
import com.mengyu.retrofitUtils.model.RequestCode;
import com.mengyu.retrofitUtils.model.RequestInfo;
import com.mengyu.retrofitUtils.model.request.LoginRequest;
import com.mengyu.retrofitUtils.model.response.ResultResponse;
import com.mengyu.retrofitUtils.tools.StringUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ICSService {

    private static final Object LOCKOBJECT = new Object();
    private static final String TAG = "ICSService";
    private Retrofit localRetrofit = null;
    private OkHttpClient client = new OkHttpClient();
    private static int DEFAULT_TIMEOUT = 15000;

    private static ICSService instance;

    /**
     * @return ICSService
     */
    public static ICSService getInstance() {
        synchronized (LOCKOBJECT) {
            if (null == instance) {
                instance = new ICSService();
            }
            return instance;
        }
    }

    /**
     * 登录
     *
     * @param name name
     */
    public void login(String name) {
        Retrofit retrofit = getRetrofit();
        if (null == retrofit) {
            return;
        }
        RequestManager requestManager = retrofit.create(RequestManager.class);
        Call call = requestManager.login(name, "",
                new LoginRequest("", "", ""));
        if (null == call) {
            return;
        }
        call.enqueue(new Callback<ResultResponse>() {
            private BroadMsg broadMsg = new BroadMsg(NotifyMessage.AUTH_MSG_ON_LOGIN);

            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                ResultResponse model = response.body();

                if (null == model) {
                    //返回信息出为空
                    sendBroadcastWithError(broadMsg, NotifyMessage.RET_ERROR_RESPONSE);
                    return;
                }

                Log.e(TAG, "login" + " " + model.getMessage());

                String retcode = model.getRetcode();

                if (!NotifyMessage.MESSAGE_OK.equals(retcode)) {
                    //返回信息出错
                    sendBroadcast(broadMsg, retcode);
                    return;
                }

//                SystemConfig.getInstance().setLogin(true);
                Log.e(TAG, "login" + " " + "login success");
                Log.e(TAG, "login" + " " + "Retcode:" + model.getRetcode() + "\n Result:" + model.getResult());

                if (null == response.headers()) {
                    Log.e(TAG, "header is null");
                    return;
                }

//                String guid = getGUID(response.headers());
                String cookie = getCookies(response.headers());

//                if (!StringUtils.isEmpty(guid)) {
//                    SystemConfig.getInstance().setGuid(guid);
//                }

                if (!StringUtils.isEmpty(cookie)) {
                    SystemConfig.getInstance().setCookie(cookie);
                }

                //发送登录成功的广播
                sendBroadcast(broadMsg, retcode);

                //登陆成功后开始轮询
//                beginTask();
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                //网络访问异常
                Log.e(TAG, "login" + " " + "login -> t = " + t.getMessage());
                sendBroadcastWithError(broadMsg, NotifyMessage.RET_ERROR_NETWORK);
            }
        });
    }

    /**
     * 获取 Retrofit 实例
     *
     * @return Retrofit
     */
    private Retrofit getRetrofit() {
        client = createOkhttp();
        localRetrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create()).client(client)
                .build();
//        Log.e(TAG, "path " + SystemConfig.getInstance().getTransSecurity() + SystemConfig.getInstance().getServerIp()
//                + ":" + SystemConfig.getInstance().getServerPort());
        return localRetrofit;
    }


    /**
     * 获取OkHttpClient
     *
     * @return OkHttpClient
     */
    private OkHttpClient createOkhttp() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new MyTrustManager()};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = new Tls12SocketFactory(sslContext.getSocketFactory());

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new MyHostnameVerifier());

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "createOkhttp failed due to NoSuchAlgorithmException.");
            return null;
        } catch (KeyManagementException e) {
            Log.e(TAG, "createOkhttp failed due to KeyManagementException.");
            return null;
        } catch (RuntimeException exception) {
            throw exception;
        }
    }

    /**
     * 发送广播
     *
     * @param broadMsg  BroadMsg
     * @param errorCode int
     */
    private void sendBroadcastWithError(BroadMsg broadMsg, int errorCode) {
        RequestCode requestCode = new RequestCode();
        requestCode.setErrorCode(errorCode);
        broadMsg.setRequestCode(requestCode);
        MyApplication.getInstance().sendBroadcast(broadMsg);
    }

    /**
     * 发送广播
     *
     * @param broadMsg BroadMsg
     */
    private void sendBroadcast(BroadMsg broadMsg) {
        sendBroadcast(broadMsg, "");
    }

    /**
     * 发送广播
     *
     * @param broadMsg BroadMsg
     * @param retCode  String
     */
    private void sendBroadcast(BroadMsg broadMsg, String retCode) {
        sendBroadcast(broadMsg, retCode, "");
    }

    /**
     * 发送广播
     *
     * @param broadMsg BroadMsg
     * @param retCode  String
     * @param msg      String
     */
    private void sendBroadcast(BroadMsg broadMsg, String retCode, String msg) {
        RequestCode requestCode = new RequestCode();
        requestCode.setRCode(retCode);
        broadMsg.setRequestCode(requestCode);

        if (!StringUtils.isEmpty(msg)) {
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setMsg(msg);
            broadMsg.setRequestInfo(requestInfo);
        }
        MyApplication.getInstance().sendBroadcast(broadMsg);
    }

    /**
     * 获取cookie
     *
     * @param headers Headers
     * @return cookies String
     */
    private String getCookies(Headers headers) {
        StringBuilder builder = new StringBuilder();
        List<String> cookies = headers.values("Set-Cookie");
        if (null == cookies) {
            return "";
        }

        //对多个cookie进行拼接，以“;”隔开
        for (String s : cookies) {
            builder.append(s);
            builder.append(";");
        }

        //删除最后一次拼接的多余的“;”
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private static class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    }

    private static class MyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }
}
