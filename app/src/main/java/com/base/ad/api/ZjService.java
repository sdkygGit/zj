package com.base.ad.api;

import com.base.ad.bean.UploadTokenRet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ZjService {

    /**
     * 登录
     *
     * @return
     */
    @POST("/api/public/zj/test/test")
    @FormUrlEncoded
    Call<UploadTokenRet> login(@Field("account") String account, @Field("deviceId") String deviceId, @Field("token") String token);
}
