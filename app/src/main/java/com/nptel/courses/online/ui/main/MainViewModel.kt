package com.nptel.courses.online.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nptel.courses.online.entities.Course
import com.nptel.courses.online.entities.Playlist
import com.nptel.courses.online.retrofit.RetrofitServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    var retrofitServices: RetrofitServices,
    var selectedCourse: Course
) : ViewModel() {

    private val _status = MutableLiveData<Int>()
    val status: LiveData<Int> = _status

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    fun getPlaylists() {
        viewModelScope.launch {
            try {
                val playlists = retrofitServices.getPlaylists(selectedCourse.id)
                _status.value = 1
                _playlists.value = playlists
            } catch (throwable: Throwable) {
                _status.value = 1
                _playlists.value = null
            }
        }
    }

    init {
        getPlaylists()
    }
}