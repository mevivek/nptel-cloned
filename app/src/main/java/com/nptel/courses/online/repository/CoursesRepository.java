/*
package com.nptel.courses.online.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.firebase.FirebaseQuery;
import com.nptel.courses.online.utility.GenericResponseList;

public class CoursesRepository {

    public CoursesRepository() {
    }

    public static LiveData<GenericResponseList<Course>> getCourses() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("/production/production/youtubeCourses");
        Query query = reference.orderBy("courseName");
        FirebaseQuery firebaseQuery = new FirebaseQuery(query);
        LiveData<GenericResponseList<Course>> responseListLiveData;
        responseListLiveData = Transformations.map(firebaseQuery, response -> {
            GenericResponseList<Course> genericResponseList = new GenericResponseList<>();
            if (response.isSuccess()) {
                genericResponseList.setSuccess(true);
                genericResponseList.setData(response.getData().toObjects(Course.class));
            } else {
                genericResponseList.setSuccess(false);
                genericResponseList.setErrorMessage(response.getErrorMessage());
            }
            return genericResponseList;
        });
        return responseListLiveData;
    }
}
*/
