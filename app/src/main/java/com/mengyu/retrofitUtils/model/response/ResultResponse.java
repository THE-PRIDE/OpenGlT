package com.mengyu.retrofitUtils.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultResponse {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("retcode")
    @Expose
    private String retcode;

    @SerializedName("result")
    @Expose
    private String result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
