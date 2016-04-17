package com.influans.sp.exception;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = -2812473108740945538L;

    private final CustomErrorCode customErrorCode;

    public CustomException(CustomErrorCode errorCode) {
        super(errorCode.getMessage());
        this.customErrorCode = errorCode;
    }

    public CustomException(CustomErrorCode errorCode, String message) {
        super(message);
        this.customErrorCode = errorCode;
    }

    public CustomException(CustomErrorCode errorCode, Throwable e) {
        super(errorCode.getMessage(), e);
        this.customErrorCode = errorCode;
    }

    public CustomException(CustomErrorCode errorCode, String message, Exception e) {
        super(message, e);
        this.customErrorCode = errorCode;
    }

    public CustomErrorCode getCustomErrorCode() {
        return customErrorCode;
    }
}