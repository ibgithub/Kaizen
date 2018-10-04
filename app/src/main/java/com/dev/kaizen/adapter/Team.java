package com.dev.kaizen.adapter;

public class Quotes {
    private int id;
    private String title, tagline, urlPhoto, description, urlVideo;

    public Quotes() {

    }

    public Quotes(int id, String title, String tagline, String urlPhoto, String description, String urlVideo) {
        this.id = id;
        this.title = title;
        this.tagline = tagline;
        this.urlPhoto = urlPhoto;
        this.description = description;
        this.urlVideo = urlVideo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
