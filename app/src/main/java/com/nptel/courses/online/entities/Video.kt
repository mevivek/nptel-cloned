package com.nptel.courses.online.entities

import androidx.annotation.Keep
import java.util.*

@Keep
data class Video(
    val id: String,
    var isFavorite: Boolean = false,
    var youtubeVideoId: String,
    var title: String = "No Title",
    var duration: String = "0",
    var image: String? = null,
    var description: String? = null,
    var playlistId: String? = null,
    var courseId: String? = null,
    var course: Course? = null,
    var playlist: Playlist? = null,
    var position: Int = 0,
    var search: Search? = null
) {

    @Keep
    class Search {
        var highlights: List<Highlight>? = null
    }

    @Keep
    class Highlight {
        var path: String? = null
        var score = 0f
        var texts: List<Text>? = null

        @Keep
        class Text {
            var value: String? = null
            var type: String? = null
        }
    }

    companion object {
        private val random = Random(50)

        @JvmStatic
        fun dummy() = Video("dummy" + random.nextInt(), youtubeVideoId = "").apply {
            title = "This is video title"
            duration = "PT${Random().nextInt(60)}M${Random().nextInt(60)}S"
            course = Course.dummy()
            playlist = Playlist.dummy()
        }

        @JvmStatic
        fun dummies(size: Int): List<Video> {
            val videos = mutableListOf<Video>()
            repeat(size) {
                videos.add(dummy())
            }
            return videos
        }
    }
}