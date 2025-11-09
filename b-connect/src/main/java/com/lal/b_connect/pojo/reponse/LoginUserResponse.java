package com.lal.b_connect.pojo.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserResponse extends BaseResponse{

    private UserData userdetails;
    private String token;
}
