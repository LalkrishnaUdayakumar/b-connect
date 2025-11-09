package com.lal.b_connect.pojo.request.usermangement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequest {
    private String password;
    private String phoneNumber;
}
