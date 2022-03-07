package com.wanmi.osd.ExceptionHandle;

public class OSDException extends RuntimeException{

    private String errorMessage;
    private String errorCode;

    public OSDException(){
        super();
    }


    public OSDException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
    public OSDException(String errorCode,String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
