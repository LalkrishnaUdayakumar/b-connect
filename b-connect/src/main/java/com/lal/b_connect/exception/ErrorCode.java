package com.lal.b_connect.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
    private ErrorCode() {
    }

    private static final Map<Integer, ValidationError> errorCodeMap = new HashMap<>();
    public static final int USER_ALREADY_EXIST = 500;
    public static final int WRONG_PASSWORD = 501;
    public static final int INVALID_PHONE_NUMBER = 502;
    public static final int USER_NOT_EXIST = 503;
    public static final int ERR_UNAUTHORIZED_ACCESS = 504;

    private static void populateMap() {
        addError(new ValidationError(USER_ALREADY_EXIST, "USER ALREADY EXIST",
                "Failed to create new user, user already exist with these user details"));

        addError(new ValidationError(WRONG_PASSWORD, "INCORRECT PASSWORD",
                "Entered incorrect password. Please check your password and try again"));

        addError(new ValidationError(INVALID_PHONE_NUMBER, "INVALID PHONE NUMBER",
                "Please enter a valid phone number"));

        addError(new ValidationError(USER_NOT_EXIST, "USER NOT FOUND",
                "User not found, Please enter a valid user details"));

        addError(new ValidationError(ERR_UNAUTHORIZED_ACCESS, "ERR_UNAUTHORIZED_ACCESS",
                "Unauthorized Access, User is not authorized to perform this action."));
    }

    private static void addError(ValidationError error) {
        errorCodeMap.put(error.getErrorId(), error);
    }

    public static ValidationError getError(Integer errorId) {
        if (errorCodeMap.isEmpty()) {
            populateMap();
        }
        return errorCodeMap.get(errorId);
    }

}
