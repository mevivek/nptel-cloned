package com.nptel.courses.online.entities;

import androidx.annotation.Keep;

import java.util.Random;

@Keep
public class Playlist {

    private static final Random random = new Random(50);

    private String id;
    private String title;
    private String description;
    private Integer videosCount;
    private String image;
    private String courseId;
    private Course course;

    public Playlist() {
    }

    public Playlist(String id) {
        this();
        this.id = id;

    }

    public static Playlist getDummy() {
        Playlist playlist = new Playlist("dummy" + random.nextInt());
        playlist.title = "dummy title";
        playlist.videosCount = 123;
        return playlist;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVideosCount() {
        return videosCount;
    }

    public void setVideosCount(Integer videosCount) {
        this.videosCount = videosCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
