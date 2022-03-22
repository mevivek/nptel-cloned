package com.nptel.courses.online.utility;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.entities.User;

public class SharedPreferencesUtility {

    private static final String SELECTED_COURSE = "selected course";
    private static final String USER = "user";
    private static final String LOGIN_SKIPPED = "login skipped";

    public static void setSelectedCourse(Context context, Course course) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SELECTED_COURSE, new Gson().toJson(course)).apply();
    }

    public static Course getSelectedCourse(Context context) {
        return new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(SELECTED_COURSE, null), Course.class);
    }

    public static void saveUser(Context context, User user) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER, new Gson().toJson(user)).apply();
    }

    public static User getUser(Context context) {
        return new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(USER, null), User.class);
    }

    public static void setLoginSkipped(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(LOGIN_SKIPPED, true).apply();
    }

    public static boolean isLoginSkipped(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(LOGIN_SKIPPED, false);
    }
}
