package com.lal.b_connect.pojo.request.usermangement;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
    private String phoneNumber;
    private String newPassword;
}
