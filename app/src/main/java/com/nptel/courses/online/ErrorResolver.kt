package com.nptel.courses.online

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

fun Throwable.errorMessage() = when (this) {
    is UnknownHostException -> "You might be not connected to the internet."
    is HttpException -> "Code: ${this.code()}, message: ${this.message()}, response: ${this.response()}"
    is IOException -> "Error in parsing server response. ${if (BuildConfig.DEBUG) this.message else ""}"
    else -> if (BuildConfig.DEBUG) this.toString() else "Something went wrong"
}