package com.nptel.courses.online.interfaces

interface TaskListener<T> {
    fun onComplete(t: T)
}