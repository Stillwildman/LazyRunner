package com.stillwildman.lazyrunner.model;

/**
 * Created by vincent.chang on 2017/5/4.
 */

public class ItemsMessage {

    public String photo_url;
    public String name;
    public String message;
    public long timestamp;
    public boolean had_read;

    public ItemsMessage() {

    }

    public ItemsMessage(String photo_url, String name, String message, long timestamp, boolean had_read) {
        this.photo_url = photo_url;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
        this.had_read = had_read;
    }
}
