package com.partypanda.partyrent.rentya.Model;

public class Rental {

    private String type;
    private int guests;
    private String term;
    private String region;
    private String title;
    private String description;
    private int rooms;
    private int bathrooms;
    private boolean renovated;
    private String nearestUniversity;
    private String nearestMetro;
    private double fee;
    private boolean negotiable;
    private String address;
    private String status;
    private String hostId;

    public Rental(String type, int guests, String term, String region, String title, String description, int rooms, int bathrooms, boolean renovated, String nearestUniversity, String nearestMetro, double fee, boolean negotiable, String address,String status,String hostId) {

        this.type = type;
        this.guests = guests;
        this.term = term;
        this.region = region;
        this.title = title;
        this.description = description;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.renovated = renovated;
        this.nearestUniversity = nearestUniversity;
        this.nearestMetro = nearestMetro;
        this.fee = fee;
        this.negotiable = negotiable;
        this.address = address;
        this.status = status;
        this.hostId = hostId;
    }

    public Rental(){

    }

    public String getType() {
        return type;
    }

    public int getGuests() {
        return guests;
    }

    public String getTerm() {
        return term;
    }

    public String getRegion() {
        return region;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getRooms() {
        return rooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public boolean isRenovated() {
        return renovated;
    }

    public String getNearestUniversity() {
        return nearestUniversity;
    }

    public String getNearestMetro() {
        return nearestMetro;
    }

    public double getFee() {
        return fee;
    }

    public boolean isNegotiable() {
        return negotiable;
    }

    public String getAddress() {
        return address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public void setRenovated(boolean renovated) {
        this.renovated = renovated;
    }

    public void setNearestUniversity(String nearestUniversity) {
        this.nearestUniversity = nearestUniversity;
    }

    public void setNearestMetro(String nearestMetro) {
        this.nearestMetro = nearestMetro;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setNegotiable(boolean negotiable) {
        this.negotiable = negotiable;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHostId(){return hostId;}

    public void setHostId(String hostId){this.hostId = hostId;}
}
