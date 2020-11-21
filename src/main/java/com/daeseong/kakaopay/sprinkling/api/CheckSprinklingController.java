package com.daeseong.kakaopay.sprinkling.api;

import com.daeseong.kakaopay.sprinkling.contants.HttpStatusCode;
import com.daeseong.kakaopay.sprinkling.dto.ResponseDataForChecking;
import com.daeseong.kakaopay.sprinkling.dto.SingleResponse;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.service.SprinklingMoneyCheckingService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"2. 머니 뿌리기 상태 조회"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class CheckSprinklingController {

    private final SprinklingMoneyCheckingService sprinklingMoneyCheckingService;

    @GetMapping("/sprinkle/{token}")
    public SingleResponse getSprinklingMoneyInfo(@RequestHeader(name = "X-ROOM-ID") String roomId,
                                                 @RequestHeader(name = "X-USER-ID") Long userId,
                                                 @PathVariable("token") String token){
        SprinklingMoney sprinklingMoney = sprinklingMoneyCheckingService.execute(roomId, userId, token);
        ResponseDataForChecking data = new ResponseDataForChecking(sprinklingMoney);
        int code = HttpStatusCode.HTTP_200_OK.getCode();
        String msg = HttpStatusCode.HTTP_200_OK.getMsg();
        return new SingleResponse(data, code, msg);
    }
}
