package com.nptel.courses.online.entities

import androidx.annotation.Keep
import java.util.*
import kotlin.math.roundToInt

@Keep
data class Course(
    val id: String,
    var title: String,
    var playlistsCount: Int,
    var videosCount: Int,
    var image: String? = null,
    var courseImage: String? = null,
) {

    companion object {
        private val random = Random(100)
        fun dummy() = Course(
            "dummy" + random.nextInt(),
            "Computer Science",
            (Math.random() * 50).roundToInt(),
            (Math.random() * 200).roundToInt()
        )
    }
}