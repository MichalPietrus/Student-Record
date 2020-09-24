package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Entity
public class UserDetailsDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Pattern(regexp = "^$|^(?:[+][0-9]{2}\\s?[0-9]{3}[-]?[0-9]{3,}|(?:[(][0-9]{3}[)]|[0-9]{3})\\s*[-]?\\s*[0-9]{3}[-][0-9]{4})(?:\\s*x\\s*[0-9]+)?")
    private String phoneNumber;
    @Pattern(regexp = "^$|^(0[1-9]|1[012])[-/.](0[1-9]|[12][0-9]|3[01])[-/.](19|20)\\d\\d$")
    private String birthday;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_of_resident_id")
    private PlaceOfResident placeOfResident;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Parent parent;
    @OneToOne(mappedBy = "userDetailsDB")
    @ToString.Exclude
    private User user;

}
