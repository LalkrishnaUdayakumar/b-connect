package com.lal.b_connect.service.reponse;

import com.lal.b_connect.entity.database.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FindDonorResponse extends BaseResponse {
   List<UserInfo> userInfo ;


    public FindDonorResponse(String userName, String place, String gender, String numberOfTimesDonates, String lastDateOfDonation, String bloodGroup) {
    }
}
