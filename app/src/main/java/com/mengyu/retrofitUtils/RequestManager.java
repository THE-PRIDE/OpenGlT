package com.mengyu.retrofitUtils;

import com.mengyu.retrofitUtils.model.request.LoginRequest;
import com.mengyu.retrofitUtils.model.response.ResultResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestManager {

    /**
     * 登录
     *
     * @param username username
     * @param vdnId    vdnId
     * @param body     body
     * @return call
     */
    @Headers({
            "Accept: application/json",
            "Accept-Encoding: gzip,deflate",
            "Content-Type: application/json",
            "guid: "
    })
    @POST("/icsgateway/resource/onlinewecc/{vdnId}/{username}/login")
    Call<ResultResponse> login(@Path("username") String username, @Path("vdnId") String vdnId,
                               @Body LoginRequest body);
}
