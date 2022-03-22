package com.nptel.courses.online.entities;

import androidx.annotation.Keep;

import java.util.List;
import java.util.UUID;

@Keep
public class Collection {

    private String id;
    private String name;
    private List<String> videos;

    public Collection(String name) {
        id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }
}
