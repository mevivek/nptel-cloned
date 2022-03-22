package com.nptel.courses.online.entities;

import androidx.annotation.Keep;

import java.util.List;
import java.util.Random;

@Keep
public class Video {

    private static final Random random = new Random(50);

    private String id;
    private boolean isFavorite;
    private String youtubeVideoId;
    private String title;
    private String duration;
    private String image;
    private String description;
    private String playlistId;
    private String courseId;
    private Course course;
    private Playlist playlist;
    private int position;

    private Search search;

    public static Video getDummy() {
        Video video = new Video();
        video.id = "dummy" + random.nextInt();
        video.duration = "PT54M12S";
        return video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
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

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }





    @Keep
    public static class Search {

        private List<Highlight> highlights;

        public List<Highlight> getHighlights() {
            return highlights;
        }

        public void setHighlights(List<Highlight> highlights) {
            this.highlights = highlights;
        }
    }


    @Keep
    public static class Highlight {
        private String path;
        private float score;
        private List<Text> texts;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public List<Text> getTexts() {
            return texts;
        }

        public void setTexts(List<Text> texts) {
            this.texts = texts;
        }

        @Keep
        public static class Text {
            private String value;
            private String type;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
