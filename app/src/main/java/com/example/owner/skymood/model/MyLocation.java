package com.example.owner.skymood.model;

/**
 * Created by owner on 10/04/2016.
 */
public class MyLocation {

    private long id;
    private String city;
    private String code;
    private String country;
    private String location;

    public MyLocation(String city, String code, String country, String location) {
        this.city = city;
        this.code = code;
        this.country = country;
        this.location = location;
    }

    public MyLocation(long id, String city, String code, String country, String location) {
        this(city, code, country, location);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return location;
    }
}
