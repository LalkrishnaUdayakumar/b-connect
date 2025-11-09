package com.lal.b_connect.pojo.request.blooddrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequestDto {
    private Long donorId;
    private String bloodGroup;
    private String location;
}
