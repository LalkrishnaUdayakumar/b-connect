package com.lal.b_connect.service;


import com.lal.b_connect.auth.JwtService;
import com.lal.b_connect.auth.UserDetailsUserService;
import com.lal.b_connect.entity.database.UserInfo;
import com.lal.b_connect.entity.repository.UserRepo;
import com.lal.b_connect.exception.ErrorCode;
import com.lal.b_connect.exception.ErrorResponseUtil;
import com.lal.b_connect.exception.UserManagementException;
import com.lal.b_connect.service.reponse.BaseResponse;
import com.lal.b_connect.service.reponse.LoginUserResponse;
import com.lal.b_connect.service.request.CreateUserRequest;
import com.lal.b_connect.service.request.LoginUserRequest;
import com.lal.b_connect.service.request.SaveProfilePhotoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServices implements UserInterface {

    @Autowired
    UserRepo repository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    UserDetailsUserService userDetailsService;


    @Override
    public BaseResponse createUser(CreateUserRequest request) {
        BaseResponse resp = new BaseResponse();
        try {
            UserInfo user = repository.findByPhoneNumber(request.getPhoneNumber());
            if (user != null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_ALREADY_EXIST));
            } else {
                user = new UserInfo();
                user.setUserName(request.getUserName());
                user.setPassword(request.getPassword());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setDonor(false);
                user.setRole("USER");
                log.info("SignUp successfully..!");
                repository.save(user);
                resp.setResponseId(HttpStatus.OK.value());
                resp.setResponseMessage("SignUp successfully..!");
                resp.setResponseDescription("Signup Completed, Please login using these credentials");
            }
        } catch (UserManagementException e) {
            log.error("Error creating user - Status: {}, Message: {}, Description: {}",
                    e.getError().getErrorId(), e.getError().getErrorMessage(), e.getError().getErrorDescription());
            return ErrorResponseUtil.createErrorResponse(resp, e.getError().getErrorId(), e.getError().getErrorMessage(), e.getError().getErrorDescription());
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ErrorResponseUtil.createErrorResponse(resp, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred, while creating user");
        }
        return resp;
    }

    @Override
    public LoginUserResponse loginUser(LoginUserRequest request) {
        UserInfo user = repository.findByPhoneNumber(request.getPhoneNumber());
        LoginUserResponse resp = new LoginUserResponse();
        try {
            if (user == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            } else {
                if (user.getPhoneNumber().equals(request.getPhoneNumber())) {
                    if (user.getPassword().equals(request.getPassword())) {
                        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                request.getPhoneNumber(), request.getPassword()));
                        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhoneNumber());
                        String token = jwtService.generateToken(userDetails);
                        resp.setToken(token);
                        resp.setUserName(user.getUserName());
                        resp.setDonor(user.isDonor());
                        log.info("Login successfully..!");
                        resp.setResponseId(HttpStatus.OK.value());
                        resp.setResponseMessage("Login successfully..!");
                        resp.setResponseDescription("You have successfully logged in. Welcome back to your account!");
                    } else {
                        throw new UserManagementException(ErrorCode.getError(ErrorCode.WRONG_PASSWORD));
                    }
                } else {
                    throw new UserManagementException(ErrorCode.getError(ErrorCode.INVALID_PHONE_NUMBER));
                }
            }
        } catch (UserManagementException e) {
            log.error("Error while Logging - Status: {}, Message: {}, Description: {}",
                    e.getError().getErrorId(), e.getError().getErrorMessage(), e.getError().getErrorDescription());
            return ErrorResponseUtil.createErrorResponse(resp, e.getError().getErrorId(), e.getError().getErrorMessage(), e.getError().getErrorDescription());
        } catch (Exception e) {
            log.error("Error while Logging", e);
            return ErrorResponseUtil.createErrorResponse(resp, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred, while user logging");
        }
        return resp;
    }

    @Override
    public BaseResponse saveProfilePhoto(SaveProfilePhotoRequest request) {
        BaseResponse resp = new BaseResponse();
        try {
            UserInfo user = repository.findByPhoneNumber(getAuthenticatedUserPhoneNumber());
            if (user == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            } else {
                user.setImageBytes(request.getImageBytes());
                repository.save(user);
                log.info("Image successfully saved..!");
                resp.setResponseId(HttpStatus.OK.value());
                resp.setResponseMessage("Image successfully saved..!");
                resp.setResponseDescription("Your profile photo successfully saved in database!");
            }
        } catch (UserManagementException e) {
            log.error("Error while save photo - Status: {}, Message: {}, Description: {}",
                    e.getError().getErrorId(), e.getError().getErrorMessage(), e.getError().getErrorDescription());
            return ErrorResponseUtil.createErrorResponse(resp, e.getError().getErrorId(), e.getError().getErrorMessage(), e.getError().getErrorDescription());
        } catch (Exception e) {
            log.error("Error while saving photo", e);
            return ErrorResponseUtil.createErrorResponse(resp, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred, while saving photo");
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
}
