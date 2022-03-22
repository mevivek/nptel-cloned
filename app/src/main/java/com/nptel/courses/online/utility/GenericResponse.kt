package com.nptel.courses.online.utility

import com.google.gson.annotations.SerializedName

class GenericResponse<T> {
    var isSuccess = false
    var status: String? = null

    @SerializedName("status_code")
    var statusCode = 0

    @SerializedName("status_message")
    var statusMessage: String? = null

    @SerializedName("error_message")
    var errorMessage: String? = null

    @SerializedName("error_name")
    var errorName: String? = null

    @SerializedName("error_data")
    var errorData: String? = null
    var data: T? = null
        private set

    fun setData(data: T) {
        this.data = data
    }
}