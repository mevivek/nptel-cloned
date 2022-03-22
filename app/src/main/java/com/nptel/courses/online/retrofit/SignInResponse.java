package com.nptel.courses.online.retrofit;


public class SignInResponse {

    private String token;
    private String refreshToken;
    private boolean newUser;
//    private User details;

    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    /*public User getDetails() {
        return details;
    }

    public void setDetails(User details) {
        this.details = details;
    }*/
}
