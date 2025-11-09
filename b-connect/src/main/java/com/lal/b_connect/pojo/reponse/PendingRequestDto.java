package com.lal.b_connect.pojo.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingRequestDto {
    private Long requestId;
    private Long requesterId;
    private String requesterName;
    private String bloodGroup;
    private String location;
    private String status;
    private LocalDateTime createdAt;
}
