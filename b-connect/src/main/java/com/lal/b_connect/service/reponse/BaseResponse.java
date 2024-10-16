package com.lal.b_connect.service.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BaseResponse {
    private int responseId;
    private String responseMessage;
    private String responseDescription;



}
