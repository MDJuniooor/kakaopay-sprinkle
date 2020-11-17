package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.dto.BaseResponse;
import com.daeseong.kakaopay.sprinkling.dto.CHttpStatusCode;
import com.daeseong.kakaopay.sprinkling.dto.MultipleResponse;
import com.daeseong.kakaopay.sprinkling.dto.SingleResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResponseServiceTest {

    @Autowired
    ResponseService responseService;

    @Test
    public void failBasicResponse() {
        int code = CHttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.getCode();
        String msg  = CHttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.getMsg();

        BaseResponse baseResponse =  responseService.getFailResponse(code, msg);

        assertEquals(baseResponse.getCode(), code);
        assertEquals(baseResponse.getMsg(), msg);
    }

    @Test
    public void successBasicResponse() {
        int code = CHttpStatusCode.HTTP_200_OK.getCode();
        String msg = CHttpStatusCode.HTTP_200_OK.getMsg();

        BaseResponse baseResponse = responseService.getSuccessResponse(code, msg);

        assertEquals(baseResponse.getCode(), code);
        assertEquals(baseResponse.getMsg(), msg);

    }

    @Test
    public void successSingleResponse() {
        int code = CHttpStatusCode.HTTP_200_OK.getCode();
        String msg = CHttpStatusCode.HTTP_200_OK.getMsg();
        String data = "test";

        SingleResponse singleResponse = responseService.getSingleResponse(data, code, msg);

        assertEquals(singleResponse.getData(), data);
        assertEquals(singleResponse.getCode(), code);
        assertEquals(singleResponse.getMsg(), msg);

    }

    @Test
    public void successMultipleResponse() {
        int code = CHttpStatusCode.HTTP_200_OK.getCode();
        String msg = CHttpStatusCode.HTTP_200_OK.getMsg();
        List<String> data = new ArrayList<String>();
        data.add("test");

        MultipleResponse multipleResponse = responseService.getMultipleResponse(data, code, msg);

        assertEquals(multipleResponse.getData(), data);
        assertEquals(multipleResponse.getCode(), code);
        assertEquals(multipleResponse.getMsg(), msg);
    }
}