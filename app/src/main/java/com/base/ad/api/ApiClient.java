package com.base.ad.api;

import android.app.Application;

import com.base.ad.AppContext;
import com.base.ad.R;
import com.base.ad.utils.Utils;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit CLIENT;
    public static String API_URL = "http://192.168.1.111";
//    public static String API_URL = "http://192.168.1.111";
    private ApiClient() {

    }

    /**
     */
    public static void init(final Application context) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(7, TimeUnit.SECONDS)
                .readTimeout(7, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String token = "sss";
                        Request request = chain.request().newBuilder()
                                .addHeader("versionCode", Utils.getVersionCode(context) + "")
                                .addHeader("key", token == null ? "null" : token)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))
                .build();

        CLIENT = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
//        OkHttpUtils.initClient(okHttpClient);
    }

    public static Retrofit getCLIENT() {
        if (CLIENT == null) {
            init(AppContext.getInstance());
        }
        return CLIENT;
    }
}
