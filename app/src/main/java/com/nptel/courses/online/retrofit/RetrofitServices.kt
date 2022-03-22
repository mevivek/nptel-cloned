package com.nptel.courses.online.retrofit

import com.nptel.courses.online.entities.*
import com.nptel.courses.online.entities.Collection
import com.nptel.courses.online.ui.launch.SplashActivity.Update
import retrofit2.http.*

interface RetrofitServices {

    @GET("courses?filter[order]=title ASC")
    suspend fun courses(): List<Course>

    @GET("playlists")
    suspend fun getPlaylists(@Query("filter[where][courseId]") courseId: String): List<Playlist>


    // order will be in the form of <fieldName ASC/DESC>
    @GET("videos")
    suspend fun getVideos(
        @Query("filter[where][playlistId]") playlistId: String,
        @Query("filter[order]") order: String
    ): List<Video>

    @GET("videos?filter[include][]=course&filter[include][]=playlist")
    suspend fun getSharedVideos(@Query("filter[where][youtubeVideoId]") youtubeVideoId: String): List<Video>

    @GET("videos/{id}?filter[include][]=course&filter[include][]=playlist")
    suspend fun getSearchedVideo(@Path("id") id: String?): Video

    @POST("users")
    suspend fun updateUser(@Query("provider") provide: String, @Query("token") token: String): User

    @PATCH("users/collections/{collection}/add")
    suspend fun addToCollection(
        @Path("collection") collectionName: String,
        @Query("videoId") videoId: String
    )

    @PATCH("users/collections/{collection}/remove")
    suspend fun removeFromCollection(
        @Path("collection") collectionName: String,
        @Query("videoId") videoId: String
    )

    @GET("users/collections/{collection}?filter[include][]=course&filter[include][]=playlist")
    suspend fun getCollection(@Path("collection") collectionName: String): List<Video>

    @GET("users/collections")
    suspend fun getCollections(): List<Collection>

    @PATCH("users/collections/{collection}")
    suspend fun updateCollection(
        @Path("collection") collectionName: String,
        @Query("newCollectionName") newName: String
    )

    @DELETE("users/collections/{collection}")
    suspend fun deleteCollection(@Path("collection") collectionName: String)

    @GET("videos/search")
    suspend fun searchVideos(
        @Query("query") query: String?,
        @Query("course") course: String?
    ): List<Video>

    @GET("update")
    suspend fun checkForUpdate(): Update
}