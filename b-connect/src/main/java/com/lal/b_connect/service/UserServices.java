package com.lal.b_connect.service;


import com.lal.b_connect.auth.JwtService;
import com.lal.b_connect.auth.UserDetailsUserService;
import com.lal.b_connect.entity.database.UserInfo;
import com.lal.b_connect.entity.repository.UserRepo;
import com.lal.b_connect.exception.ErrorCode;
import com.lal.b_connect.exception.ErrorResponseUtil;
import com.lal.b_connect.exception.UserManagementException;
import com.lal.b_connect.pojo.reponse.BaseResponse;
import com.lal.b_connect.pojo.reponse.FindDonorResponse;
import com.lal.b_connect.pojo.reponse.LoginUserResponse;
import com.lal.b_connect.pojo.reponse.UserData;
import com.lal.b_connect.pojo.request.usermangement.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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
                        UserData userData = new UserData();
                        resp.setToken(token);
                        userData.setImageBytes(user.getImageBytes());
                        userData.setUserName(user.getUserName());
                        userData.setPhoneNumber(user.getPhoneNumber());
                        userData.setGender(user.getGender());
                        userData.setPlace(user.getPlace());
                        userData.setNumberOfTimesDonates(user.getNumberOfTimesDonates());
                        userData.setLastDateOfDonation(user.getLastDateOfDonation());
                        userData.setDonor(user.isDonor());
                        userData.setBloodGroup(user.getBloodGroup());
                        resp.setUserdetails(userData);
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
                resp.setResponseMessage("success");
                resp.setResponseDescription("Image successfully saved..!");
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

    @Override
    public FindDonorResponse findDonor(FindDonorRequest request) {
        FindDonorResponse resp = new FindDonorResponse();
        try {
            // Validate blood group
            String bloodGroup = validateBloodGroup(request.getBloodGroup());

            // Find eligible donors
            List<UserInfo> eligibleDonors = repository.findByBloodGroup(bloodGroup);

            if (eligibleDonors.isEmpty()) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.NO_ELIGIBLE_DONORS));
            }

            resp.setUserInfo(eligibleDonors);
            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("Eligible donors found");
            resp.setResponseDescription("Successfully retrieved eligible donor details");

        } catch (UserManagementException e) {
            // Handle specific user management exceptions
            log.error("Donor search error", e);
            resp.setResponseId(e.getError().getErrorId());
            resp.setResponseMessage(e.getError().getErrorMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            log.error("Unexpected error in donor search", e);
            resp.setResponseId(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setResponseMessage("Internal server error");
        }
        return resp;
    }

    @Override
    public BaseResponse forgetPassword(ForgetPasswordRequest request) {
        BaseResponse resp = new BaseResponse();
        try {
            // Find user by phone number
            UserInfo user = repository.findByPhoneNumber(request.getPhoneNumber());
            if (user == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            }

            // Update new password
            user.setPassword(request.getNewPassword());
            repository.save(user);

            log.info("Password reset successfully for phone number: {}", request.getPhoneNumber());
            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("Password reset successfully");
            resp.setResponseDescription("You can now login with your new password");

        } catch (UserManagementException e) {
            log.error("Forget password error - {}", e.getError().getErrorMessage());
            return ErrorResponseUtil.createErrorResponse(
                    resp,
                    e.getError().getErrorId(),
                    e.getError().getErrorMessage(),
                    e.getError().getErrorDescription()
            );
        } catch (Exception e) {
            log.error("Unexpected error while resetting password", e);
            return ErrorResponseUtil.createErrorResponse(
                    resp,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error while resetting password"
            );
        }
        return resp;
    }


    @Override
    public BaseResponse changePassword(ChangePasswordRequest request) {
        BaseResponse resp = new BaseResponse();
        try {
            // Get authenticated user
            String phoneNumber = getAuthenticatedUserPhoneNumber();
            UserInfo user = repository.findByPhoneNumber(phoneNumber);
            if (user == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            }

            // Verify old password
            if (!user.getPassword().equals(request.getOldPassword())) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.WRONG_PASSWORD));
            }

            // Save new password
            user.setPassword(request.getNewPassword());
            repository.save(user);

            log.info("Password changed successfully for phone: {}", phoneNumber);
            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("Password changed successfully");
            resp.setResponseDescription("Use your new password for next login");

        } catch (UserManagementException e) {
            log.error("Change password error - {}", e.getError().getErrorMessage());
            return ErrorResponseUtil.createErrorResponse(
                    resp,
                    e.getError().getErrorId(),
                    e.getError().getErrorMessage(),
                    e.getError().getErrorDescription()
            );
        } catch (Exception e) {
            log.error("Unexpected error while changing password", e);
            return ErrorResponseUtil.createErrorResponse(
                    resp,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error while changing password"
            );
        }
        return resp;
    }


    @Override
    public BaseResponse updateUser(UpdateUserRequest request) {
        BaseResponse resp = new BaseResponse();
        try {
            // Get logged-in user's phone
            String phoneNumber = getAuthenticatedUserPhoneNumber();
            UserInfo user = repository.findByPhoneNumber(phoneNumber);
            if (user == null) {
                throw new UserManagementException(ErrorCode.getError(ErrorCode.USER_NOT_EXIST));
            }

            // Update details
            user.setUserName(request.getUserName());
            user.setPlace(request.getPlace());
            user.setGender(request.getGender());
            user.setBloodGroup(request.getBloodGroup());
            user.setDonor(request.isDonor());
            user.setNumberOfTimesDonates(request.getNumberOfTimesDonates());
            user.setLastDateOfDonation(request.getLastDateOfDonation());
            repository.save(user);

            log.info("User profile updated successfully for {}", phoneNumber);
            resp.setResponseId(HttpStatus.OK.value());
            resp.setResponseMessage("Profile updated successfully");
            resp.setResponseDescription("Your profile details have been updated successfully");

        } catch (UserManagementException e) {
            log.error("Update user error - {}", e.getError().getErrorMessage());
            return ErrorResponseUtil.createErrorResponse(
                    resp,
                    e.getError().getErrorId(),
                    e.getError().getErrorMessage(),
                    e.getError().getErrorDescription()
            );
        } catch (Exception e) {
            log.error("Unexpected error while updating user", e);
            return ErrorResponseUtil.createErrorResponse(
                    resp,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error while updating user details"
            );
        }
        return resp;
    }


    private String validateBloodGroup(String bloodGroup) throws UserManagementException {
        if (!isValidBloodGroup(bloodGroup)) {
            throw new UserManagementException(ErrorCode.getError(ErrorCode.INVALID_BLOOD_GROUP));
        }
        return bloodGroup.toUpperCase();
    }

    private boolean isValidBloodGroup(String bloodGroup) {
        if (bloodGroup == null) {
            return false;
        }

        // Regex pattern for valid blood groups
        String bloodGroupRegex = "^(A|B|AB|O)[+-]$";

        return bloodGroup.trim().toUpperCase().matches(bloodGroupRegex);
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
