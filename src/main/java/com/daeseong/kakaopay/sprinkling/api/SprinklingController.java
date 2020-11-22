package com.daeseong.kakaopay.sprinkling.api;

import com.daeseong.kakaopay.sprinkling.dto.ResponseDataForSprinkling;
import com.daeseong.kakaopay.sprinkling.dto.SingleResponse;
import com.daeseong.kakaopay.sprinkling.dto.SprinkleMoneyRequest;
import com.daeseong.kakaopay.sprinkling.service.SprinklingMoneyService;
import com.daeseong.kakaopay.sprinkling.contants.HttpStatusCode;
import com.daeseong.kakaopay.sprinkling.contants.SprinklingMoneyConstant;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Api(tags = {"1. 머니 뿌리기"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class SprinklingController {

    private final SprinklingMoneyService sprinklingMoneyService;

    @PostMapping("/sprinkle")
    public SingleResponse sprinkleMoney(@RequestHeader(name = "X-ROOM-ID") String roomId,
                                        @RequestHeader(name = "X-USER-ID") Long userId,
                                        @RequestBody @Valid SprinkleMoneyRequest request){
        ResponseDataForSprinkling responseDataForSprinkling;
        int code;
        String msg;
        String token = sprinklingMoneyService.execute(roomId, userId, request.getNumberForPickingUp(), request.getAmount());

        if (token != SprinklingMoneyConstant.TOKEN_CREATE_FAILURE){
            responseDataForSprinkling = new ResponseDataForSprinkling(token);
            code = HttpStatusCode.HTTP_200_OK.getCode();
            msg = HttpStatusCode.HTTP_200_OK.getMsg();
        } else {
            responseDataForSprinkling = new ResponseDataForSprinkling();
            code = HttpStatusCode.HTTP_404_NOT_FOUND.getCode();
            msg = HttpStatusCode.HTTP_404_NOT_FOUND.getMsg();
        }
        return new SingleResponse(responseDataForSprinkling, code, msg);

    }


//    @Data
//    static class SprinkleMoneyRequest {
//        private int numberForPickingUp;
//        private int amount;
//    }
}
