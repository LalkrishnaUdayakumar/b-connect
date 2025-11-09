package com.lal.b_connect.pojo.request.usermangement;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String userName;
    private String place;
    private String gender;
    private String bloodGroup;
    private String numberOfTimesDonates;
    private String lastDateOfDonation;
    private boolean donor;
}
