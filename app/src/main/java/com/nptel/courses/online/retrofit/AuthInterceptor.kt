package com.nptel.courses.online.retrofit

import android.content.Context
import com.nptel.courses.online.utility.getUser
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(@ApplicationContext val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        getUser(context)?.run { builder.header("userId", id) }
        return chain.proceed(builder.build())
    }
}