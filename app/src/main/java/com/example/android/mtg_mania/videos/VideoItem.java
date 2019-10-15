package com.example.android.mtg_mania.videos;

import android.graphics.Bitmap;
import android.net.Uri;

public class VideoItem {

//    private Integer id;
    private String title;
    private String playtime;
    private Uri uri;
    private Bitmap thumbnail;

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public VideoItem(String title, String playtime, Uri uri, Bitmap thumbnail) {
//        this.id = id;
        this.title = title;
        this.playtime = playtime;
        this.uri = uri;
        this.thumbnail = thumbnail;
    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
