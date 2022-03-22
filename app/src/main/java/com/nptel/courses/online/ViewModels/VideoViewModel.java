/*
package com.nptel.courses.online.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.firestore.DocumentSnapshot;
import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.repository.VideoRepository;
import com.nptel.courses.online.utility.GenericResponse;
import com.nptel.courses.online.utility.GenericResponseList;

public class VideoViewModel extends ViewModel {
    private LiveData<GenericResponseList<Video>> videoListLiveData;
    private LiveData<GenericResponse<Video>> videoLiveData;

    private LiveData<PagedList<Video>> videoPagedListLiveData;

    public LiveData<PagedList<Video>> getVideoPagedListLiveData(String playlistID) {
        if (videoPagedListLiveData == null)
            videoPagedListLiveData = VideoRepository.getPagedVideos(playlistID);
        return videoPagedListLiveData;
    }

    public LiveData<GenericResponseList<Video>> getVideoListLiveData(String playlistId) {
        if (videoListLiveData == null)
            videoListLiveData = new VideoRepository().getVideos(playlistId);
        return videoListLiveData;
    }

    public LiveData<GenericResponse<Video>> getVideoLiveData(String videoId) {
        if (videoLiveData == null)
            videoLiveData = new VideoRepository().getVideo(videoId);
        return videoLiveData;
    }
}
*/
