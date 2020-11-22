package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;
import com.daeseong.kakaopay.sprinkling.entity.RoomJoinInfo;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.repository.RoomJoinInfoRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.daeseong.kakaopay.sprinkling.contants.BusinessStatusCode.*;

@Service
@RequiredArgsConstructor
public class SprinklingMoneyCheckingService {

    private final SprinklingMoneyRepository sprinklingMoneyRepository;
    private final RoomJoinInfoRepository roomJoinInfoRepository;

    public SprinklingMoney execute(String roomId, Long userId, String token){
        List<SprinklingMoney> sprinklingMoney = sprinklingMoneyRepository.findByTokenAndRoomIdForRead(token, roomId);
        validate(roomId, userId, token, sprinklingMoney);
        return sprinklingMoney.get(0);
    }

    private void validate(String roomId, Long userId, String token, List<SprinklingMoney> sprinklingMoney) {
        List<RoomJoinInfo> roomJoinInfos = roomJoinInfoRepository.findByRoomIdAndUserId(roomId, userId);

        if (roomJoinInfos.size() == 0){
            throw new BusinessException(BE1001.getCode(), BE1001.getMsg());
        }

        if (sprinklingMoney.size() == 0){
            throw new BusinessException(BE3003.getCode(), BE3003.getMsg());
        }

        if (sprinklingMoney.get(0).getCreator().getId() != userId) {
            throw new BusinessException(BE1002.getCode(), BE1002.getMsg());
        }


    }
}
