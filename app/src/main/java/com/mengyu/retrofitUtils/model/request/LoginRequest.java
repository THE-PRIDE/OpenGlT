package com.mengyu.retrofitUtils.model.request;

public class LoginRequest {
    private String userIp;
    private String appId;
    private String entryIp;

    /**
     * 构造
     *
     * @param userAddress userAddress
     * @param appId       appId
     * @param entryIp     entryIp
     */
    public LoginRequest(String userAddress, String appId, String entryIp) {
        this.userIp = userAddress;
        this.appId = appId;
        this.entryIp = entryIp;
    }

    public String getUserIp() {
        return userIp;
    }

    public String getAppId() {
        return appId;
    }

    public String getEntryIp() {
        return entryIp;
    }
}
