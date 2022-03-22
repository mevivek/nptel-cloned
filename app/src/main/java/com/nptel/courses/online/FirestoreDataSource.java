/*
package com.nptel.courses.online;


import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class FirestoreDataSource extends PageKeyedDataSource<DocumentSnapshot, DocumentSnapshot> {

    private Query baseQuery;
    private Status status;

    private FirestoreDataSource(Query baseQuery) {
        this.baseQuery = baseQuery;
        status = Status.LOADING;
    }

    public Status getStatus() {
        return status;
    }

    public static class Factory extends DataSource.Factory<DocumentSnapshot, DocumentSnapshot> {

        private Query query;

        public Factory(Query query) {
            this.query = query;
        }

        @NonNull
        @Override
        public DataSource<DocumentSnapshot, DocumentSnapshot> create() {
            return new FirestoreDataSource(query);
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<DocumentSnapshot> params, @NonNull LoadInitialCallback<DocumentSnapshot, DocumentSnapshot> callback) {
        status = Status.LOADING;
        baseQuery.limit(params.requestedLoadSize)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (params.requestedLoadSize > queryDocumentSnapshots.size())
                        status = Status.DONE;
                    callback.onResult(
                            queryDocumentSnapshots.getDocuments(),
                            null,
                            params.requestedLoadSize == queryDocumentSnapshots.size() ? queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1) : null);
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<DocumentSnapshot> params, @NonNull LoadCallback<DocumentSnapshot, DocumentSnapshot> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<DocumentSnapshot> params, @NonNull LoadCallback<DocumentSnapshot, DocumentSnapshot> callback) {
        baseQuery.limit(params.requestedLoadSize)
                .startAfter(params.key)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (params.requestedLoadSize > queryDocumentSnapshots.size())
                        status = Status.DONE;
                    callback.onResult(
                            queryDocumentSnapshots.getDocuments(),
                            params.requestedLoadSize == queryDocumentSnapshots.size() ? queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1) : null);
                });
    }


    enum Status {
        LOADING, ERROR, DONE
    }
}
*/
