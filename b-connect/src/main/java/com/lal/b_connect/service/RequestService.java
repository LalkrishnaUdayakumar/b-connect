package com.lal.b_connect.service;

import com.lal.b_connect.enums.RequestStatus;
import com.lal.b_connect.pojo.reponse.BaseResponse;
import com.lal.b_connect.pojo.request.blooddrequest.BloodRequestDto;

public interface RequestService {
    BaseResponse requestBlood( BloodRequestDto requestDto);
    BaseResponse approveRequest(Long requestId);
    BaseResponse rejectRequest(Long requestId);
    BaseResponse getPendingRequests( );
    BaseResponse getRequestHistory(RequestStatus status);
}
