package com.nptel.courses.online.retrofit;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nptel.courses.online.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static Retrofit retrofit = null;

    private static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            initialize(context);
        }
        return retrofit;
    }

    public static RetrofitServices getRetrofitService(Context context) {
        return getRetrofit(context).create(RetrofitServices.class);
    }

    public static void initialize(Context context) {
        ChuckerInterceptor chuckerInterceptor = new ChuckerInterceptor.Builder(context).build();
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new AuthInterceptor(context)).addInterceptor(chuckerInterceptor).build();
        Gson gson = new GsonBuilder().create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://loopback-dot-nptel-198211.uc.r.appspot.com")
//                .baseUrl("http://192.168.0.103:8080")
//                .baseUrl("https://loopback-staging-dot-nptel-198211.uc.r.appspot.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client);
        if (!BuildConfig.DEBUG)
            builder.baseUrl("https://loopback-dot-nptel-198211.uc.r.appspot.com");
        retrofit = builder.build();
    }
}