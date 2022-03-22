/*
package com.nptel.courses.online.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nptel.courses.online.FirestoreDataSource;
import com.nptel.courses.online.entities.Playlist;
import com.nptel.courses.online.utility.Constants;

public class ModulesRepository {

    public ModulesRepository() {
    }

    public static LiveData<PagedList<Playlist>> getModules(String courseID) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection(Constants.firebaseCoursesList + "/" + courseID + "/" + "playlist");
        Query query = reference.orderBy("serialNumber");
        DataSource.Factory<DocumentSnapshot, Playlist> factory = new FirestoreDataSource.Factory(query).map(documentSnapshot -> documentSnapshot.toObject(Playlist.class));
        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true).setInitialLoadSizeHint(5).setPageSize(5).build();
        return new LivePagedListBuilder<>(factory, config).build();
    }
}
*/
