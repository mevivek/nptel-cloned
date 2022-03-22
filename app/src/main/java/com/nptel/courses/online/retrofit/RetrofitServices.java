package com.nptel.courses.online.retrofit;

import com.nptel.courses.online.entities.Collection;
import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.entities.Playlist;
import com.nptel.courses.online.entities.User;
import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.ui.launch.SplashActivity;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitServices {


    @GET("courses?filter[order]=title ASC")
    Call<List<Course>> getCourses();

    @GET("playlists")
    Call<List<Playlist>> getPlaylists(@Query("filter[where][courseId]") String courseId);

    @GET("playlists/count")
    Call<Map<String, Integer>> getPlaylistsCount(@Query("[where][courseId]") String courseId);

    // order will be in the form of <fieldName ASC/DESC>
    @GET("videos")
    Call<List<Video>> getVideos(@Query("filter[where][playlistId]") String playlistId, @Query("filter[order]") String order);

    @GET("videos?filter[include][]=course&filter[include][]=playlist")
    Call<List<Video>> getSharedVideos(@Query("filter[where][youtubeVideoId]") String youtubeVideoId);

    @GET("videos/{id}?filter[include][]=course&filter[include][]=playlist")
    Call<Video> getSearchedVideo(@Path("id") String id);

    @GET("videos/count")
    Call<Map<String, Integer>> getVideosCount(@Query("[where][courseId]") String courseId, @Query("[where][playlistId]") String playlistId);

    @POST("users")
    Call<User> updateUser(@Query("provider") String provide, @Query("token") String token);

    @PATCH("users/collections/{collection}/add")
    Call<Void> addToCollection(@Path("collection") String collectionName, @Query("videoId") String videoId);

    @PATCH("users/collections/{collection}/remove")
    Call<Void> removeFromCollection(@Path("collection") String collectionName, @Query("videoId") String videoId);

    @GET("users/collections/{collection}?filter[include][]=course&filter[include][]=playlist")
    Call<List<Video>> getCollection(@Path("collection") String collectionName);

    @GET("users/collections")
    Call<List<Collection>> getCollections();

    @PATCH("users/collections/{collection}")
    Call<Void> updateCollection(@Path("collection") String collectionName, @Query("newCollectionName") String newName);

    @DELETE("users/collections/{collection}")
    Call<Void> deleteCollection(@Path("collection") String collectionName);

    @GET("videos/search")
    Call<List<Video>> searchVideos(@Query("query") String query, @Query("course") String course);

    @GET("update")
    Call<SplashActivity.Update> checkForUpdate();

}
