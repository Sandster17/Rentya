package com.partypanda.partyrent.rentya.Model;

public class Reservation {

    private String rentalId;
    private String userId;
    private String date;
    private String time;
    private String hostId;

    public Reservation(){

    }
    public Reservation(String rentalId, String userId, String date, String time,String hostId){
        this.rentalId = rentalId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.hostId = hostId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
