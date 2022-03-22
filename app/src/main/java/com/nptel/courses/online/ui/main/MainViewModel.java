package com.nptel.courses.online.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nptel.courses.online.entities.Course;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Course> selectedCourseLiveData;

    public MutableLiveData<Course> getSelectedCourse() {
        if (selectedCourseLiveData == null)
            selectedCourseLiveData = new MutableLiveData<>();
        return selectedCourseLiveData;
    }

    public void setSelectedCourse(Course selectedCourse) {
        selectedCourseLiveData.setValue(selectedCourse);
    }
}
