package com.lal.b_connect.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class UserManagementException extends BConnectException{

    @Serial
    private static final long serialVersionUID = 1L;
    private ValidationError error;
    public UserManagementException(String code) {
        super(code);
    }

    public UserManagementException(String code, String message) {
        super(code, message);
    }

    public UserManagementException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UserManagementException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }

    public UserManagementException(ValidationError error){
        super(error.getErrorMessage(),error.getErrorDescription());
        this.error=error;
    }

}
