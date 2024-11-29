package com.lal.b_connect.service;


import com.lal.b_connect.exception.UserManagementException;
import com.lal.b_connect.service.reponse.BaseResponse;
import com.lal.b_connect.service.reponse.FindDonorResponse;
import com.lal.b_connect.service.reponse.LoginUserResponse;
import com.lal.b_connect.service.request.CreateUserRequest;
import com.lal.b_connect.service.request.FindDonorRequest;
import com.lal.b_connect.service.request.LoginUserRequest;
import com.lal.b_connect.service.request.SaveProfilePhotoRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserInterface {

    BaseResponse createUser(CreateUserRequest request);

    LoginUserResponse loginUser(LoginUserRequest request) throws UserManagementException;

    BaseResponse saveProfilePhoto(SaveProfilePhotoRequest request);

    FindDonorResponse findDonor(FindDonorRequest request);
}
