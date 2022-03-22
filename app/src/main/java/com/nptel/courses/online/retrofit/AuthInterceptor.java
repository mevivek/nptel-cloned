package com.nptel.courses.online.retrofit;


import android.content.Context;

import androidx.annotation.NonNull;

import com.nptel.courses.online.entities.User;
import com.nptel.courses.online.utility.SharedPreferencesUtility;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain)
            throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        /*if (SharedPreferencesUtils.getAuthToken() != null)
                builder.header("Authorization", "Bearer " + SharedPreferencesUtils.getAuthToken());
        if (SharedPreferencesUtils.getUser() != null && SharedPreferencesUtils.getSelectedRestaurant() != null)
            builder.header("restaurant-id", String.valueOf(SharedPreferencesUtils.getSelectedRestaurant().restaurant_id()));
        builder.header("android-package", BuildConfig.APPLICATION_ID);
        builder.header("android-version", String.valueOf(BuildConfig.VERSION_CODE));
        builder.header("android-debug", String.valueOf(BuildConfig.DEBUG));
        builder.header("android-signature", new AppSignatureHelper(SharedPreferencesUtils.getContext()).getAppSignatures().toString());*/
        User user = SharedPreferencesUtility.getUser(context);
        if (user != null)
            builder.header("userId", user.getId());
        return chain.proceed(builder.build());
    }
}
