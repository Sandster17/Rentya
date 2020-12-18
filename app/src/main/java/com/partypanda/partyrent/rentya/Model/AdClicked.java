package com.partypanda.partyrent.rentya.Model;

public class AdClicked {

    private String dateClicked;
    private String rentalId;
    private String userId;
    public AdClicked(String dateClicked,String rentalId,String userId)
    {
        this.dateClicked = dateClicked;
        this.rentalId = rentalId;
        this.userId = userId;
    }
    public AdClicked(){

    }
    public String getDateClicked() {
        return dateClicked;
    }

    public void setDateClicked(String dateClicked) {
        this.dateClicked = dateClicked;
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
}

