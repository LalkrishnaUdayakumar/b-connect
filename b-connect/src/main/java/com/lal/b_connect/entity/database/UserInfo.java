package com.lal.b_connect.entity.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    byte[] imageBytes;private String phoneNumber;
    private String userName;
    private String place;
    private String gender;
    private String numberOfTimesDonates;
    private String lastDateOfDonation;
    private String password;
    private String role;
    private boolean donor;

}

