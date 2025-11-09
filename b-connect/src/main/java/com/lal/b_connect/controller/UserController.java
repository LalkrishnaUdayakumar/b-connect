package com.lal.b_connect.controller;


import com.lal.b_connect.pojo.request.usermangement.*;
import com.lal.b_connect.service.UserInterface;
import com.lal.b_connect.pojo.reponse.BaseResponse;
import com.lal.b_connect.pojo.reponse.FindDonorResponse;
import com.lal.b_connect.pojo.reponse.LoginUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
public class UserController {

    @Autowired
    private UserInterface userInterface;

    private static final String CREATE_USER = "b-connect/signup";
    private static final String LOGIN_USER = "b-connect/login";
    private static final String SAVE_PROFILE_PHOTO = "b-connect/saveProfilePhoto";
    private static final String FORGET_PASSWORD = "b-connect/forgetPassword";
    private static final String CHANGE_PASSWORD = "b-connect/changePassword";
    private static final String FIND_DONOR = "b-connect/findDonor";


    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }

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

    @PostMapping(value = FORGET_PASSWORD)
    public BaseResponse forgetPassword(@RequestBody ForgetPasswordRequest request,
                                       BindingResult bindingResult) throws Exception {
        return userInterface.forgetPassword(request);
    }

    @PostMapping(value = CHANGE_PASSWORD)
    public BaseResponse changePassword(@RequestBody ChangePasswordRequest request,
                                       BindingResult bindingResult) throws Exception {
        return userInterface.changePassword(request);
    }

    @PostMapping(value = "b-connect/updateUser")
    public BaseResponse updateUser(@RequestBody UpdateUserRequest request,
                                   BindingResult bindingResult) throws Exception {
        return userInterface.updateUser(request);
    }


    @PostMapping(value = SAVE_PROFILE_PHOTO)
    public BaseResponse saveProfilePhoto(@RequestBody SaveProfilePhotoRequest request,
                                              BindingResult bindingResult) throws Exception {
        return userInterface.saveProfilePhoto(request);
    }

    @PostMapping(value = FIND_DONOR)
    public  FindDonorResponse findDonor(@RequestBody FindDonorRequest request,
                                             BindingResult bindingResult) throws Exception {
        return userInterface.findDonor(request);
    }


}
