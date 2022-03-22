package com.nptel.courses.online.ui.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nptel.courses.online.NetworkRequestStatus
import com.nptel.courses.online.Status
import com.nptel.courses.online.entities.Course
import com.nptel.courses.online.errorMessage
import com.nptel.courses.online.retrofit.RetrofitServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseSelectionViewModel @Inject constructor(val retrofitServices: RetrofitServices) :
    ViewModel() {

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    private val _networkRequestStatus =
        MutableLiveData<NetworkRequestStatus>(NetworkRequestStatus(Status.Loading))
    val networkRequestStatus: LiveData<NetworkRequestStatus> = _networkRequestStatus

    fun getCourses() {
        viewModelScope.launch {
            try {
                val courses = retrofitServices.courses()
                _courses.value = courses
                _networkRequestStatus.value = NetworkRequestStatus(Status.Loaded)
            } catch (throwable: Throwable) {
                _courses.value = null
                _networkRequestStatus.value =
                    NetworkRequestStatus(Status.Error, throwable.errorMessage())

            }
        }
    }

    init {
        getCourses()
    }
}