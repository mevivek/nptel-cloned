package com.nptel.courses.online.retrofit;

import com.google.gson.annotations.SerializedName;

public class SuccessResponse {

    private String status;
    @SerializedName("status_code")
    private String statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
