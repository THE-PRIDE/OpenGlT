package com.mengyu.retrofitUtils;

public class SystemConfig {

    private static final Object LOCKOBJECT = new Object();
    private static SystemConfig ins;

    private String cookie = "";

    /**
     * 构造
     *
     * @return SystemConfig
     */
    public static SystemConfig getInstance() {
        synchronized (LOCKOBJECT) {
            if (null == ins) {
                ins = new SystemConfig();
            }
            return ins;
        }
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    private static void releaseIns() {
        synchronized (LOCKOBJECT) {
            ins = null;
        }
    }

    /**
     * 清除信息
     */
    public void clear() {
        releaseIns();
    }

}
