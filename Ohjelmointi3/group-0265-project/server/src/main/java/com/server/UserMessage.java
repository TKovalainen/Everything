package com.server;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class UserMessage {
    
    private String locationName;
    private String locationDescription;
    private String locationCity;
    private ZonedDateTime postingTime;
    public ZonedDateTime sent;
    public Double latitude;
    public Double longitude;
    private String streetAddress;
    private String locationCountry;
    private String originalPoster;

    public UserMessage(String name, String desc, String city,   String locationCountry, String streetAddress,String originalPoster, ZonedDateTime postingTime, Double latitude, Double longitude){
        this.locationName = name;
        this.locationDescription = desc;
        this.locationCity = city;
        this.sent = postingTime;
        this.streetAddress = streetAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationCountry = locationCountry;
        this.originalPoster = originalPoster;

    }

    public void setOriginalPoster(String poster){
        this.originalPoster = poster;
    }

    public String getOriginalPoster(){
        return originalPoster;
    }

    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public Double getLatitude(){
        return latitude;
    }
    
    public void setLocationStreetAddress(String address){
        this.streetAddress = address;
    }

    public String getLocationStreetAddress(){
        return streetAddress;
    }

    public void setLocationCountry(String country){
        this.locationCountry = country;
    }

    public String getLocationCountry(){
        return locationCountry;
    }

    public String getLocationName(){
        return locationName;
    }

    public String getLocationDescription(){
        return locationDescription;
    }

    public String getLocationCity(){
        return locationCity;
    }

    public void setLocationName(String name){
        this.locationName = name;
    }

    public void setLocationDescription(String desc){
        this.locationDescription = desc;
    }

    public void setLocationCity(String city){
        this.locationCity = city;
    }

    public void setPostingTime(ZonedDateTime postingTime){
        this.postingTime = postingTime;
    }

    public ZonedDateTime getPostingTime(){
        return postingTime;
    }

    public long dateAsInt() {
        return sent.toInstant().toEpochMilli();
    }

    public void setSent(long epoch) {
        sent = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(""+getLocationCity()+","+getLocationDescription()+","+getLocationCity()+"");
        return sb.toString();
    }



}
