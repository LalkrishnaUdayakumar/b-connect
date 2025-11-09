package com.lal.b_connect.pojo.request.usermangement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    private String userName;
    private String password;
    private String phoneNumber;
    private boolean donor;
}
