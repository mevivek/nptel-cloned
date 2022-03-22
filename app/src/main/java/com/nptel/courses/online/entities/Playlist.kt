package com.nptel.courses.online.entities

import androidx.annotation.Keep
import java.util.*

@Keep
data class Playlist(
    val id: String,
    var title: String,
    var videosCount: Int,
    var description: String? = null,
    var image: String? = null,
    var courseId: String? = null,
    var course: Course? = null
) {

    companion object {
        private val random = Random(50)

        @JvmStatic
        fun dummy() = Playlist(
            "dummy" + random.nextInt(),
            "Dummy Title",
            Random().nextInt(100)
        ).apply {
            description =
                "This is a description for playlist of videos. This is a description for playlist of videos. This is a description for playlist of videos. This is a description for playlist of videos."
        }
    }
}