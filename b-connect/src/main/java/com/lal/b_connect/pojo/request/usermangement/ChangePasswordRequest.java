package com.lal.b_connect.pojo.request.usermangement;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
