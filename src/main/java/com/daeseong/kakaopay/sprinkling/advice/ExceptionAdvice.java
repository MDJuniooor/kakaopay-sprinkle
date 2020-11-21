package com.daeseong.kakaopay.sprinkling.advice;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;

import com.daeseong.kakaopay.sprinkling.contants.HttpStatusCode;
import com.daeseong.kakaopay.sprinkling.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity businessException(BusinessException e){
        int code = e.getCode();
        String msg = e.getMsg();

        return ResponseEntity
                .status(HttpStatusCode.HTTP_400_BAD_REQUEST.getCode())
                .body(responseService.getFailResponse(code, msg));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity defaultException(Exception e) {
        int code = HttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.getCode();
        String msg = HttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.getMsg();

        return ResponseEntity
                .status(HttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.getCode())
                .body(responseService.getFailResponse(code, msg));
    }


}