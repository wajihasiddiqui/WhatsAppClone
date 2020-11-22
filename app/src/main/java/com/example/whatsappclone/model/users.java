package com.example.whatsappclone.model;

public class users {

    private String userId;
    private String userName;
    private String userPhone;
    private String email;
    private String gernder;
    private String imageCover;
    private String imageProfile;
    private String dateofBirth;
    private String bio;
    private String status;


    public users() {
    }

    public users(String userId, String userName, String userPhone, String email, String gernder, String imageCover, String imageProfile, String dateofBirth, String bio, String status) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.email = email;
        this.gernder = gernder;
        this.imageCover = imageCover;
        this.imageProfile = imageProfile;
        this.dateofBirth = dateofBirth;
        this.bio = bio;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGernder() {
        return gernder;
    }

    public void setGernder(String gernder) {
        this.gernder = gernder;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
