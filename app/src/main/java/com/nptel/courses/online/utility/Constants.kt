package com.nptel.courses.online.utility

import android.content.Context

object Constants {
    var firebaseCoursesList = "/production/production/youtubeCourses"
    var firebaseVideosCollection = "/production/production/youtubeVideos"
    var feedbackForm = "https://goo.gl/kR2ds9"
    private const val appreciatePaymentPage = "https://nptel-198211.web.app/" //"https://rzp.io/l/XvXXNi3";
    var getFeedbackFormLongUrl = "https://docs.google.com/forms/d/e/1FAIpQLSf0XWAKgOxZp9aUZJW-EbR-tGIbvHGzFTZp0TfC4MItZRJNdQ/viewform"
    fun getAppreciatePaymentPage(context: Context): String {
        var email: String? = null
        var userId: String? = null
        if (getUser(context) != null) {
            email = getUser(context)?.email
            userId = getUser(context)?.id
        }
        var url = "$appreciatePaymentPage?"
        if (email != null) url = url + "email=" + email + "&"
        if (userId != null) url = url + "userId=" + userId
        return url
    }
}