package com.lal.b_connect.service;

import com.lal.b_connect.auth.JwtService;
import com.lal.b_connect.auth.UserDetailsUserService;
import com.lal.b_connect.entity.database.BloodRequest;
import com.lal.b_connect.entity.database.UserInfo;
import com.lal.b_connect.entity.repository.BloodRequestRepo;
import com.lal.b_connect.entity.repository.UserRepo;
import com.lal.b_connect.enums.RequestStatus;
import com.lal.b_connect.exception.ErrorCode;
import com.lal.b_connect.exception.ErrorResponseUtil;
import com.lal.b_connect.exception.UserManagementException;
import com.lal.b_connect.pojo.reponse.BaseResponse;
import com.lal.b_connect.pojo.reponse.PendingRequestDto;
import com.lal.b_connect.pojo.request.blooddrequest.BloodRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    UserRepo repository;
    @Autowired
    BloodRequestRepo bloodRequestRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    UserDetailsUserService userDetailsService;

    @Override
    public BaseResponse requestBlood(BloodRequestDto requestDto) {
        BaseResponse resp = new BaseResponse();
        try {
            // ✅ Extract authenticated user's phone number
            String requesterPhone = getAuthenticatedUserPhoneNumber();

            // ✅ Find user by phone number
            UserInfo requester = repository.findByPhoneNumber(requesterPhone);
            if (requester == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            }

            // ✅ Create and save request
            BloodRequest request = new BloodRequest();
            request.setRequesterId(requester.getId());
            request.setDonorId(requestDto.getDonorId());
            request.setBloodGroup(requestDto.getBloodGroup());
            request.setLocation(requestDto.getLocation());
            request.setStatus(RequestStatus.PENDING);
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());

            bloodRequestRepo.save(request);

            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("SUCCESS");
            resp.setResponseDescription("Blood request sent successfully.");

        } catch (UserManagementException e) {
            log.error("Error while creating blood request - {}", e.getError().getErrorMessage());
            return ErrorResponseUtil.createErrorResponse(resp, e.getError().getErrorId(),
                    e.getError().getErrorMessage(), e.getError().getErrorDescription());
        } catch (Exception e) {
            log.error("Unexpected error while creating blood request", e);
            return ErrorResponseUtil.createErrorResponse(resp,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred while creating blood request");
        }
        return resp;
    }
    private String getAuthenticatedUserPhoneNumber() throws UserManagementException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            throw new UserManagementException(ErrorCode.getError(ErrorCode.ERR_UNAUTHORIZED_ACCESS));
        }
    }


    @Override
    public BaseResponse approveRequest(Long requestId) {
        BaseResponse resp = new BaseResponse();
        try {
            BloodRequest request = bloodRequestRepo.findById(requestId).orElse(null);
            if (request == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            }
            request.setStatus(RequestStatus.ACCEPTED);
            request.setUpdatedAt(LocalDateTime.now());
            bloodRequestRepo.save(request);

            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("SUCCESS");
            resp.setResponseDescription("Blood request approved successfully.");
        } catch (Exception e) {
            log.error("Error while approving request", e);
            return ErrorResponseUtil.createErrorResponse(resp, HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred while approving request");
        }
        return resp;
    }

    @Override
    public BaseResponse rejectRequest(Long requestId) {
        BaseResponse resp = new BaseResponse();
        try {
            BloodRequest request = bloodRequestRepo.findById(requestId).orElse(null);
            if (request == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            }
            request.setStatus(RequestStatus.REJECTED);
            request.setUpdatedAt(LocalDateTime.now());
            bloodRequestRepo.save(request);

            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("SUCCESS");
            resp.setResponseDescription("Blood request rejected successfully.");
        } catch (Exception e) {
            log.error("Error while rejecting request", e);
            return ErrorResponseUtil.createErrorResponse(resp, HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred while rejecting request");
        }
        return resp;
    }

    @Override
    public BaseResponse getPendingRequests( ) {
        BaseResponse resp = new BaseResponse();
        try {
            // ✅ Extract authenticated user's phone number
            String requesterPhone = getAuthenticatedUserPhoneNumber();

            // ✅ Find user by phone number
            UserInfo requester = repository.findByPhoneNumber(requesterPhone);
            if (requester == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            }

            List<BloodRequest> requests = bloodRequestRepo.findByDonorIdAndStatus(requester.getId(), RequestStatus.PENDING);

            List<PendingRequestDto> dtoList = requests.stream().map(req -> {
                // Fetch requester name using UserRepo
                String requesterName = repository.findById(req.getRequesterId())
                        .map(UserInfo::getUserName)
                        .orElse("Unknown");

                return new PendingRequestDto(
                        req.getId(),
                        req.getRequesterId(),
                        requesterName,
                        req.getBloodGroup(),
                        req.getLocation(),
                        req.getStatus().name(),
                        req.getCreatedAt()
                );
            }).collect(Collectors.toList());

            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("SUCCESS");
            resp.setResponseDescription("Pending requests fetched successfully.");
            resp.setData(dtoList);

        } catch (Exception e) {
            log.error("Error fetching pending requests", e);
            return ErrorResponseUtil.createErrorResponse(
                    resp,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred while fetching pending requests"
            );
        }
        return resp;
    }

    @Override
    public BaseResponse getRequestHistory(RequestStatus status) {
        BaseResponse resp = new BaseResponse();
        try {
            // ✅ Extract authenticated user's phone number
            String requesterPhone = getAuthenticatedUserPhoneNumber();

            // ✅ Find user by phone number
            UserInfo requester = repository.findByPhoneNumber(requesterPhone);
            if (requester == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            }

            List<BloodRequest> history;

            // ✅ If status is provided, filter by userId + status
            if (status != null) {
                history = bloodRequestRepo.findByRequesterIdAndStatus(requester.getId(), status);
            } else {
                // ✅ Otherwise fetch all requests for that user
                history = bloodRequestRepo.findByRequesterId(requester.getId());
            }

            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("SUCCESS");
            resp.setResponseDescription(
                    status != null
                            ? status.name() + " requests fetched successfully."
                            : "All requests fetched successfully."
            );
            resp.setData(history);

        } catch (Exception e) {
            log.error("Error fetching request history", e);
            return ErrorResponseUtil.createErrorResponse(
                    resp,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred while fetching request history"
            );
        }
        return resp;
    }


}
