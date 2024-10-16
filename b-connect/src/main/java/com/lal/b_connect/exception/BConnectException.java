package com.lal.b_connect.exception;

public abstract class BConnectException extends Exception {

    private static final long serialVersionUID = 1L;

    private String code;

    public BConnectException(String code) {
        this.code = code;
    }

    public BConnectException(String code, String message) {
        super(message);
        this.code = code;

    }

    public BConnectException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;

    }

    public BConnectException(String code, String message, Throwable cause, boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}



