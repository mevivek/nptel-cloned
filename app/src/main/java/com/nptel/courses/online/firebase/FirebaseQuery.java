/*
package com.nptel.courses.online.firebase;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nptel.courses.online.utility.GenericResponse;

public class FirebaseQuery extends LiveData<GenericResponse<QuerySnapshot>> {

    private Query query;
    private OnCompleteListener<QuerySnapshot> listener = task -> {
        GenericResponse<QuerySnapshot> response = new GenericResponse<>();
        if (task.isSuccessful()) {
            response.setSuccess(true);
            response.setData(task.getResult());
        } else {
            response.setSuccess(false);
            response.setErrorMessage(task.getException().getMessage());
        }
        setValue(response);
    };

    public FirebaseQuery(Query query) {
        this.query = query;
    }

    @Override
    protected void onActive() {
        query.get().addOnCompleteListener(listener);
    }
}*/
