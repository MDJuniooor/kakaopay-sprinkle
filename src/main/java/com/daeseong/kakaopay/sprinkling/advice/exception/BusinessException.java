package com.daeseong.kakaopay.sprinkling.advice.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private int code;
    private String msg;

    public BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
