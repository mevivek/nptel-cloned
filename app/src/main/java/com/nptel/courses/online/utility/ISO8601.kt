package com.nptel.courses.online.utility

import android.util.Log

object ISO8601 {
    @JvmStatic
    fun getTime(time_iso8601: String?): String? {
        if (time_iso8601 == null || time_iso8601.length == 0) return time_iso8601
        val time = StringBuilder()
        val proceed = time_iso8601[0] == 'P' && time_iso8601[1] == 'T'
        if (!proceed) return time.toString()
        for (i in 0 until time_iso8601.length) {
            if (time_iso8601[i] == 'P' || time_iso8601[i] == 'T') continue
            if (time_iso8601[i] == 'H' || time_iso8601[i] == 'M') {
                time.append(if (time_iso8601[i] == 'H') "hr" else "min").append(" : ")
                continue
            }
            if (time_iso8601[i] == 'S') {
                time.append("sec")
                continue
            }
            time.append(time_iso8601[i])
        }
        return time.toString()
    }

    fun getTimeInSeconds(time_iso8601: String): Int {
        val proceed = time_iso8601[0] == 'P' && time_iso8601[1] == 'T'
        if (!proceed) return -1
        var seconds = 0
        var intvalue = ""
        for (i in 0 until time_iso8601.length) {
            if (time_iso8601[i] == 'P' || time_iso8601[i] == 'T') continue
            if (time_iso8601[i] == 'H') {
                seconds = seconds + intvalue.toInt() * 3600
                intvalue = ""
                continue
            }
            if (time_iso8601[i] == 'M') {
                seconds = seconds + intvalue.toInt() * 60
                intvalue = ""
                continue
            }
            if (time_iso8601[i] == 'S') {
                seconds = seconds + intvalue.toInt()
                continue
            }
            try {
                intvalue = intvalue + (time_iso8601[i].toString() + "").toInt()
            } catch (e: NumberFormatException) {
                Log.e("ISO8601 to seconds", e.toString())
            }
        }
        return seconds
    }
}