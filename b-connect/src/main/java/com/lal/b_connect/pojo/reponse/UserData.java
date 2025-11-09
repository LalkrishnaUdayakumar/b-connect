package com.lal.b_connect.pojo.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private byte[] imageBytes;
    private String phoneNumber;
    private String userName;
    private String place;
    private String gender;
    private String numberOfTimesDonates;
    private String lastDateOfDonation;
    private boolean donor;
    private String bloodGroup;
}
