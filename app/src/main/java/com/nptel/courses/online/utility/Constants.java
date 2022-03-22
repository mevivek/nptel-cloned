package com.nptel.courses.online.utility;

import android.content.Context;

public class Constants {

    public static String firebaseCoursesList = "/production/production/youtubeCourses";
    public static String firebaseVideosCollection = "/production/production/youtubeVideos";
    public static String feedbackForm = "https://goo.gl/kR2ds9";
    private static String appreciatePaymentPage = "https://nptel-198211.web.app/";//"https://rzp.io/l/XvXXNi3";
    public static String getFeedbackFormLongUrl = "https://docs.google.com/forms/d/e/1FAIpQLSf0XWAKgOxZp9aUZJW-EbR-tGIbvHGzFTZp0TfC4MItZRJNdQ/viewform";

    public static String getAppreciatePaymentPage(Context context) {
        String email = null;
        String userId = null;

        if (SharedPreferencesUtility.getUser(context) != null) {
            email = SharedPreferencesUtility.getUser(context).getEmail();
            userId = SharedPreferencesUtility.getUser(context).getId();
        }

        String url = appreciatePaymentPage + "?";
        if (email != null)
            url = url + "email=" + email + "&";
        if (userId != null)
            url = url + "userId=" + userId;
        return url;
    }
}
