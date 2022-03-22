package com.nptel.courses.online.utility

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.nptel.courses.online.entities.Course
import com.nptel.courses.online.entities.User

private const val SELECTED_COURSE = "selected course"
private const val USER = "user"
private const val LOGIN_SKIPPED = "login skipped"

fun setSelectedCourse(context: Context, course: Course) {
    PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SELECTED_COURSE, Gson().toJson(course)).apply()
}

fun getSelectedCourse(context: Context): Course? {
    return Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(SELECTED_COURSE, null), Course::class.java)
}

fun saveUser(context: Context, user: User) {
    PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER, Gson().toJson(user)).apply()
}

fun getUser(context: Context): User? {
    return Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(USER, null), User::class.java)
}

fun setLoginSkipped(context: Context) {
    PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(LOGIN_SKIPPED, true).apply()
}

fun isLoginSkipped(context: Context): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(LOGIN_SKIPPED, false)
}