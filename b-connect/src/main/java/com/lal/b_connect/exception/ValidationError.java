package com.lal.b_connect.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidationError {

    public static final transient int SIZE = 4;
    protected int errorId;
    protected String errorMessage;
    protected String errorDescription;

    public ValidationError(int errorId, String errorMessage, String errorDescription) {
        this.errorId = errorId;
        this.errorMessage = errorMessage;
        this.errorDescription = errorDescription;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "errorId=" + errorId +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }
}
