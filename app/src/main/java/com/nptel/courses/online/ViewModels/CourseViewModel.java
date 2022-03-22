/*
package com.nptel.courses.online.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.entities.Playlist;
import com.nptel.courses.online.repository.CoursesRepository;
import com.nptel.courses.online.repository.ModulesRepository;
import com.nptel.courses.online.utility.GenericResponseList;

public class CourseViewModel extends ViewModel {

    private LiveData<GenericResponseList<Course>> courseList;

    public LiveData<PagedList<Playlist>> getModuleList(String courseId) {
        return ModulesRepository.getModules(courseId);
    }

    public LiveData<GenericResponseList<Course>> getCourseList() {
        if (courseList == null)
            courseList = new CoursesRepository().getCourses();
        return courseList;
    }
}
*/
