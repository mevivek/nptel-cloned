/*
package com.nptel.courses.online.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nptel.courses.online.FirestoreDataSource;
import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.firebase.FirebaseDocument;
import com.nptel.courses.online.firebase.FirebaseQuery;
import com.nptel.courses.online.utility.Constants;
import com.nptel.courses.online.utility.GenericResponse;
import com.nptel.courses.online.utility.GenericResponseList;

public class VideoRepository {

    public VideoRepository() {
    }

    public static LiveData<PagedList<Video>> getPagedVideos(String playlistID) {
        CollectionReference reference = FirebaseFirestore
                .getInstance()
                .collection(Constants.firebaseVideosCollection);
        Query query = reference.whereEqualTo("playlistID", playlistID).orderBy("serialNumber");
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(5).setEnablePlaceholders(true).setInitialLoadSizeHint(5).build();
        DataSource.Factory<DocumentSnapshot, Video> factory = new FirestoreDataSource.Factory(query).map(documentSnapshot -> documentSnapshot.toObject(Video.class));
        return new LivePagedListBuilder<>(factory, config).build();
    }

    */
/*public static LiveData<PagedList<Video>> getPagedSearchedVideos(String search) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection(Constants.firebaseVideosCollection);
        Query query = reference.con("playlistID", playlistID).orderBy("serialNumber");
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(5).setEnablePlaceholders(true).setInitialLoadSizeHint(5).build();
        DataSource.Factory<DocumentSnapshot, Video> factory = new FirestoreDataSource.Factory(query).map(documentSnapshot -> documentSnapshot.toObject(Video.class));
        return new LivePagedListBuilder<>(factory, config).build();
    }*//*


    public LiveData<GenericResponseList<Video>> getVideos(String playlistID) {
        CollectionReference reference = FirebaseFirestore
                .getInstance()
                .collection(Constants.firebaseVideosCollection);
        Query query = reference.whereEqualTo("playlistID", playlistID).orderBy("serialNumber");
        FirebaseQuery firebaseQuery = new FirebaseQuery(query);
        return Transformations.map(firebaseQuery, response -> {
            GenericResponseList<Video> genericResponseList = new GenericResponseList<>();
            if (response.isSuccess()) {
                genericResponseList.setSuccess(true);
                genericResponseList.setData(response.getData().toObjects(Video.class));
            } else {
                genericResponseList.setSuccess(false);
                genericResponseList.setErrorMessage(response.getErrorMessage());
            }
            return genericResponseList;
        });
    }

    public LiveData<GenericResponse<Video>> getVideo(String videoId) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection(Constants.firebaseVideosCollection).document(videoId);
        FirebaseDocument firebaseDocument = new FirebaseDocument(reference);
        LiveData<GenericResponse<Video>> responseLiveData;
        responseLiveData = Transformations.map(firebaseDocument, response -> {
            GenericResponse<Video> genericResponse = new GenericResponse<>();
            if (response.isSuccess()) {
                genericResponse.setSuccess(true);
                genericResponse.setData(response.getData().toObject(Video.class));
            } else {
                genericResponse.setSuccess(false);
                genericResponse.setErrorMessage(response.getErrorMessage());
            }
            return genericResponse;
        });
        return responseLiveData;
    }
}
*/
