/*
package com.nptel.courses.online.firebase;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.nptel.courses.online.utility.GenericResponse;

public class FirebaseDocument extends LiveData<GenericResponse<DocumentSnapshot>> {

    private DocumentReference documentReference;
    private ListenerRegistration listenerRegistration;
    private EventListener<DocumentSnapshot> eventListener = (documentSnapshot, exception) -> {
        GenericResponse<DocumentSnapshot> response = new GenericResponse<>();
        if (exception == null) {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                response.setData(documentSnapshot);
                response.setSuccess(true);

            } else {
                response.setSuccess(false);
                response.setErrorMessage("No data found.");
            }
        } else {
            response.setSuccess(false);
            response.setErrorMessage(exception.getMessage());
        }
        setValue(response);
    };

    public FirebaseDocument(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    @Override
    protected void onActive() {
        listenerRegistration = documentReference.addSnapshotListener(eventListener);
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
    }
}
*/
