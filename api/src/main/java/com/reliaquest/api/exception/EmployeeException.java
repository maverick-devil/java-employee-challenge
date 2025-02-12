package com.reliaquest.api.exception;

public class EmployeeException extends RuntimeException {

    private static final long serialVersionUID = 7693408945601229211L;

    private final int errorCode;

    public EmployeeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public EmployeeException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
