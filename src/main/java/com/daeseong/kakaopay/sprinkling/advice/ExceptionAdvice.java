package com.daeseong.kakaopay.sprinkling.advice;

import com.daeseong.kakaopay.sprinkling.dto.BaseResponse;
import com.daeseong.kakaopay.sprinkling.dto.CHttpStatusCode;
import com.daeseong.kakaopay.sprinkling.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.daseong.kakaopay.sprinkling.api")
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected BaseResponse defaultException(HttpServletRequest request, Exception e) {
        int code = CHttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.getCode();
        String msg = CHttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.getMsg();

        return responseService.getFailResponse(code, msg);
    }
}