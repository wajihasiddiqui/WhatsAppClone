package com.example.whatsappclone.model;

public class chatlist {
    private String userId;
    private String username;
    private String description;
    private String date;
    private String urlprofile;

    public chatlist() {

    }

    public chatlist(String userId, String username, String description, String date, String urlprofile) {
        this.userId = userId;
        this.username = username;
        this.description = description;
        this.date = date;
        this.urlprofile = urlprofile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlprofile() {
        return urlprofile;
    }

    public void setUrlprofile(String urlprofile) {
        this.urlprofile = urlprofile;
    }
}
