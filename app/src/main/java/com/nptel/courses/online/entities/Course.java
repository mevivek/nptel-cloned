package com.nptel.courses.online.entities;


import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Random;

@Keep
public class Course implements Serializable {

    private static final Random random = new Random(100);

    private String id;
    private String title;
    private String courseImage;
    private Integer courseImageResourceID;
    private Integer playlistsCount;
    private Integer videosCount;

    public Course() {

    }

    public Course(String id) {
        this.id = id;
    }

    public static Course getDummy() {
        Course course = new Course();
        course.id = "dummy" + random.nextInt();
        course.title = "abcdefgh ijsjd djd hfhd hvnfhf f fhd fdfhdjfhdjhf";
        course.courseImage = "abc";
        course.playlistsCount = 12;
        course.videosCount = 123;
        return course;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public Integer getCourseImageResourceID() {
        return courseImageResourceID;
    }

    public void setCourseImageResourceID(Integer courseImageResourceID) {
        this.courseImageResourceID = courseImageResourceID;
    }

    public Integer getPlaylistsCount() {
        return playlistsCount;
    }

    public void setPlaylistsCount(Integer playlistsCount) {
        this.playlistsCount = playlistsCount;
    }

    public Integer getVideosCount() {
        return videosCount;
    }

    public void setVideosCount(Integer videosCount) {
        this.videosCount = videosCount;
    }
}
