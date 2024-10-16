package com.lal.b_connect.exception;

import org.springframework.http.HttpStatus;
import com.lal.b_connect.service.reponse.BaseResponse;

public class ErrorResponseUtil {

    public static <T extends BaseResponse> T createErrorResponse(T response, HttpStatus status, String message) {
        response.setResponseId(status.value());
        response.setResponseMessage(message);
        response.setResponseDescription(status.getReasonPhrase());
        return response;
    }

    public static <T extends BaseResponse> T createErrorResponse(T response, HttpStatus status, String message, String description) {
        T errorResponse = createErrorResponse(response, status, message);
        errorResponse.setResponseDescription(description);
        return errorResponse;
    }

    public static <T extends BaseResponse> T createErrorResponse(T response, int status, String message, String description) {
        response.setResponseId(status);
        response.setResponseMessage(message);
        response.setResponseDescription(description);
        return response;
    }

}