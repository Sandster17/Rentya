package com.partypanda.partyrent.rentya.Model;

public class User {

    private String firstname;
    private String lastname;
    private String profileUrl;
    private String phoneNo;
    private boolean verified;
    private boolean isHost;

    public User(){

    }

    public User(String firstname, String lastname, String profileUrl,String phoneNo, boolean verified, boolean isHost) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.profileUrl = profileUrl;
        this.verified = verified;
        this.isHost = isHost;
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setHost(boolean host) {
        isHost = host;
    }
}
