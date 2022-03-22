package com.nptel.courses.online.interfaces;

public interface TaskListener<T> {

    void onComplete(T t);
}
