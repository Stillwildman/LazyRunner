package com.stillwildman.lazyrunner.model;

/**
 * Created by vincent.chang on 2017/5/4.
 */

public class ItemsFireChats {

    public String uid;
    public String name;
    public String message;
    public String photo_url;
    public long timestamp;
    public String key;

    public ItemsFireChats() {

    }

    public ItemsFireChats(String uid, String name, String message, String photo_url, long timestamp, String key) {
        this.uid = uid;
        this.name = name;
        this.message = message;
        this.photo_url = photo_url;
        this.timestamp = timestamp;
        this.key = key;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
