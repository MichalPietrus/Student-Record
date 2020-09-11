package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Data
@NoArgsConstructor
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

    public PlaceOfResident(String country, String city, String street, String houseNumber, int apartmentNumber, String postalCode, UserDetailsDB userDetailsDB) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.postalCode = postalCode;
        this.userDetailsDB = userDetailsDB;
    }

}
