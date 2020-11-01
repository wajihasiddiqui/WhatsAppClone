package com.example.whatsappclone.model;

public class calllist {

    private  String userId;
    private  String userName;
    private String date;
    private String UrlProfile;
    private String CallType;

    public calllist(String userId, String userName, String date, String urlProfile, String callType) {
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        UrlProfile = urlProfile;
        CallType = callType;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlProfile() {
        return UrlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        UrlProfile = urlProfile;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

    public calllist() {
    }
}
