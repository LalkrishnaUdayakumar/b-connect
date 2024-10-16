package com.lal.b_connect.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProfilePhotoRequest {
    private byte[] imageBytes;
    private String phoneNumber;
}
