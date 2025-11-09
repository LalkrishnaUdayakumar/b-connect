package com.lal.b_connect.service;


import com.lal.b_connect.exception.UserManagementException;
import com.lal.b_connect.pojo.reponse.BaseResponse;
import com.lal.b_connect.pojo.reponse.FindDonorResponse;
import com.lal.b_connect.pojo.reponse.LoginUserResponse;
import com.lal.b_connect.pojo.request.usermangement.*;
import org.springframework.stereotype.Service;


@Service
public interface UserInterface {

    BaseResponse createUser(CreateUserRequest request);

    LoginUserResponse loginUser(LoginUserRequest request) throws UserManagementException;

    BaseResponse saveProfilePhoto(SaveProfilePhotoRequest request);

    BaseResponse forgetPassword(ForgetPasswordRequest request);

    BaseResponse changePassword(ChangePasswordRequest request);

    BaseResponse updateUser(UpdateUserRequest request);


    FindDonorResponse findDonor(FindDonorRequest request);


}
