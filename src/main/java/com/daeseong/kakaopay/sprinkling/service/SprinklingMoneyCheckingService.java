package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.InValidInputDataException;
import com.daeseong.kakaopay.sprinkling.entity.Room;
import com.daeseong.kakaopay.sprinkling.entity.RoomJoinInfo;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoneyDistributionInfo;
import com.daeseong.kakaopay.sprinkling.repository.RoomJoinInfoRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SprinklingMoneyCheckingService {

    private final SprinklingMoneyRepository sprinklingMoneyRepository;
    private final RoomJoinInfoRepository roomJoinInfoRepository;

    public SprinklingMoney execute(String roomId, Long userId, String token){
        List<SprinklingMoney> sprinklingMoney = sprinklingMoneyRepository.findByTokenAndRoomIdForRead(token, roomId);
        validate(roomId, userId, sprinklingMoney);
        return sprinklingMoney.get(0);
    }

    private void validate(String roomId, Long userId, List<SprinklingMoney> sprinklingMoney) {
        List<RoomJoinInfo> roomJoinInfos = roomJoinInfoRepository.findByRoomIdAndUserId(roomId, userId);

        if (sprinklingMoney.size() == 0){
            throw new InValidInputDataException("뿌리기 정보가 없거나 조회 가능한 기간이 지났습니다.");
        }

        if (roomJoinInfos.size() == 0){
            throw new InValidInputDataException("참여하지 않은 채팅방입니다.");
        }

        if (sprinklingMoney.get(0).getCreator().getId() != userId) {
            throw new InValidInputDataException("뿌리기를 한 사람만 조회 가능합니다.");
        }

    }
}
