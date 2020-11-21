package com.example.whatsappclone.model;

public class users {

    private String userId;
    private String username;
    private String bio;
    private String urlprofile;




    public users(String userId, String username, String bio, String urlprofile) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
        this.urlprofile = urlprofile;
    }

    public users() {

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUrlprofile() {
        return urlprofile;
    }

    public void setUrlprofile(String urlprofile) {
        this.urlprofile = urlprofile;
    }
}
