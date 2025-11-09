package com.lal.b_connect.controller;

import com.lal.b_connect.enums.RequestStatus;
import com.lal.b_connect.pojo.reponse.BaseResponse;
import com.lal.b_connect.pojo.request.blooddrequest.BloodRequestDto;
import com.lal.b_connect.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("b-connect/request")
public class BloodRequestController {

    @Autowired
    private RequestService bloodRequestService;

    @PostMapping("/send")
    public BaseResponse requestBlood(@RequestBody BloodRequestDto requestDto) {
        return bloodRequestService.requestBlood(requestDto);
    }

    @PutMapping("/{requestId}/approve")
    public BaseResponse approveRequest(@PathVariable Long requestId) {
        return bloodRequestService.approveRequest(requestId);
    }

    @PutMapping("/{requestId}/reject")
    public BaseResponse rejectRequest(@PathVariable Long requestId) {
        return bloodRequestService.rejectRequest(requestId);
    }

    @GetMapping("/pending")
    public BaseResponse getPendingRequests() {
        return bloodRequestService.getPendingRequests();
    }

    @GetMapping("/history")
    public BaseResponse getRequestHistory(@RequestParam(required = false) RequestStatus status) {
        return bloodRequestService.getRequestHistory(status);
    }


}
