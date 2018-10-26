package com.mengyu.retrofitUtils;

import java.io.Serializable;

public class NotifyMessage implements Serializable {
//    private static final long serialVersionUID = 769506604661731005L;
    /**
     * 登录通知
     **/
    public static final String AUTH_MSG_ON_LOGIN = "AUTH_MSG_ON_LOGIN";

    public static final String MESSAGE_OK = "0";
    /**
     * 错误码-正常
     */
    public static final int RET_OK = 0;

    /**
     * 数据解析出错
     */
    public static final int RET_ERROR_RESPONSE = -4;
    /**
     * 网络出错
     */
    public static final int RET_ERROR_NETWORK = -5;

}
