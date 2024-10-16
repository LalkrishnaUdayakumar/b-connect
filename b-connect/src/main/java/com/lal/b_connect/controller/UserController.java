package com.lal.b_connect.controller;


import com.lal.b_connect.service.UserInterface;
import com.lal.b_connect.service.reponse.BaseResponse;
import com.lal.b_connect.service.reponse.LoginUserResponse;
import com.lal.b_connect.service.request.CreateUserRequest;
import com.lal.b_connect.service.request.LoginUserRequest;
import com.lal.b_connect.service.request.SaveProfilePhotoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class UserController {

    @Autowired
    private UserInterface userInterface;

    private static final String CREATE_USER = "b-connect/signup";
    private static final String LOGIN_USER = "b-connect/login";
    private static final String SAVE_PROFILE_PHOTO = "b-connect/saveProfilePhoto";


    @PostMapping(value = CREATE_USER)
    public BaseResponse createUser(@RequestBody CreateUserRequest request,
                                   BindingResult bindingResult) throws Exception {
        return userInterface.createUser(request);
    }

    @PostMapping(value = LOGIN_USER)
    public LoginUserResponse loginUser(@RequestBody LoginUserRequest request,
                                       BindingResult bindingResult) throws Exception {
        return userInterface.loginUser(request);
    }
    @PostMapping(value = SAVE_PROFILE_PHOTO)
    public BaseResponse loginUser(@RequestBody SaveProfilePhotoRequest request,
                                              BindingResult bindingResult) throws Exception {
        return userInterface.saveProfilePhoto(request);
    }
}
