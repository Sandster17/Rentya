package com.partypanda.partyrent.rentya.Model;

public class Rating {

    private String rentalID;
    private String userId;
    private float rating;

    public Rating(){

    }

    public Rating(String rentalID, String userId, float rating) {
        this.rentalID = rentalID;
        this.userId = userId;
        this.rating = rating;
    }

    public String getRentalID() {
        return rentalID;
    }

    public void setRentalID(String rentalID) {
        this.rentalID = rentalID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }



}
