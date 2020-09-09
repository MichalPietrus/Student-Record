package com.studentrecord.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
public class PlaceOfResident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private int apartmentNumber;
    @Pattern(regexp = "[0-9]{2}-[0-9]{3}")
    private String postalCode;
    @OneToOne(mappedBy = "placeOfResident")
    private UserDetailsDB userDetailsDB;

    public PlaceOfResident() {
    }

    public PlaceOfResident(String country, String city, String street, String houseNumber, int apartmentNumber, String postalCode, UserDetailsDB userDetailsDB) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.postalCode = postalCode;
        this.userDetailsDB = userDetailsDB;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public UserDetailsDB getUserDetailsDB() {
        return userDetailsDB;
    }

    public void setUserDetailsDB(UserDetailsDB userDetailsDB) {
        this.userDetailsDB = userDetailsDB;
    }

    @Override
    public String toString() {
        return "PlaceOfResident{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", apartmentNumber=" + apartmentNumber +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
