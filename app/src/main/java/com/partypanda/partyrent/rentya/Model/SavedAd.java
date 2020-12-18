package com.partypanda.partyrent.rentya.Model;

public class SavedAd {

    private String userId;
    private String rentalId;
    public SavedAd(){

    }

    public SavedAd(String userId, String rentalId) {
        this.userId = userId;
        this.rentalId = rentalId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRentalId() {
        return rentalId;
    }



}
