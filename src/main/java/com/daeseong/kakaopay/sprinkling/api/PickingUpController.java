package com.daeseong.kakaopay.sprinkling.api;

import com.daeseong.kakaopay.sprinkling.contants.HttpStatusCode;
import com.daeseong.kakaopay.sprinkling.dto.BaseResponse;
import com.daeseong.kakaopay.sprinkling.dto.ResponseDataForPickingUp;
import com.daeseong.kakaopay.sprinkling.dto.SingleResponse;
import com.daeseong.kakaopay.sprinkling.service.PickingUpService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"3. 머니 줍기"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PickingUpController{

    private final PickingUpService pickingUpService;

    @PutMapping("/pickup/{token}")
    public BaseResponse pickUpMoney(@RequestHeader(name = "X-ROOM-ID") String roomId,
                                    @RequestHeader(name = "X-USER-ID") Long userId,
                                    @PathVariable("token") String token){
        int amount = pickingUpService.execute(roomId, userId, token);
        ResponseDataForPickingUp data = new ResponseDataForPickingUp(amount);
        int code = HttpStatusCode.HTTP_200_OK.getCode();
        String msg = HttpStatusCode.HTTP_200_OK.getMsg();
        return new SingleResponse(data, code, msg);
    }
}
