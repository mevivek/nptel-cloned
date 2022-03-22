package com.nptel.courses.online.entities

import androidx.annotation.Keep
import com.nptel.courses.online.entities.Playlist
import com.nptel.courses.online.entities.Video.Search
import java.util.*

@Keep
data class Collection(val id: String = UUID.randomUUID().toString(), var name: String, var videos: List<String>? = null)