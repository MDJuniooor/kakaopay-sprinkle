package com.daeseong.kakaopay.sprinkling.advice.exception;

public class InValidInputDataException extends RuntimeException{

    public InValidInputDataException() {
        super();
    }

    public InValidInputDataException(String message) {
        super(message);
    }

    public InValidInputDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InValidInputDataException(Throwable cause) {
        super(cause);
    }

}
