package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.dto.BaseResponse;
import com.daeseong.kakaopay.sprinkling.dto.SingleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    public <T>SingleResponse getSingleResponse(T data, int code, String msg) {
        SingleResponse singleResponse = new SingleResponse(data, code, msg);
        return singleResponse;
    }

    public <T>BaseResponse getSuccessResponse(int code, String msg) {
        BaseResponse baseResponse = new BaseResponse(code, msg);
        return baseResponse;
    }

    public <T>BaseResponse getFailResponse(int code, String msg) {
        BaseResponse baseResponse = new BaseResponse(code, msg);
        return baseResponse;
    }

}
