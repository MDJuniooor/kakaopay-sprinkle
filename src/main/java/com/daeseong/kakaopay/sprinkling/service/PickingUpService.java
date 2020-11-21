package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.InValidInputDataException;
import com.daeseong.kakaopay.sprinkling.entity.*;
import com.daeseong.kakaopay.sprinkling.repository.RoomJoinInfoRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyDistributionInfoRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import com.daeseong.kakaopay.sprinkling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PickingUpService {

    private final SprinklingMoneyRepository sprinklingMoneyRepository;
    private final SprinklingMoneyDistributionInfoRepository sprinklingMoneyDistributionInfoRepository;
    private final UserRepository userRepository;
    private final RoomJoinInfoRepository roomJoinInfoRepository;


    public int execute(String roomId, Long userId, String token){
        List<SprinklingMoney> sprinklingMoney = sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, roomId);

        validate(roomId, userId, sprinklingMoney);

        List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos = sprinklingMoneyDistributionInfoRepository
                .findBySprinklingMoneyDistributionIdAndValidPickingUp(sprinklingMoney.get(0).getId());

        Long DistributionInfoId = pickUp(sprinklingMoneyDistributionInfos, userId);
        SprinklingMoneyDistributionInfo distributionInfo = sprinklingMoneyDistributionInfoRepository.findOne(DistributionInfoId);
        return distributionInfo.getAmount();
    }

    private void validate(String roomId, Long userId, List<SprinklingMoney> sprinklingMoney) {
        List<RoomJoinInfo> roomJoinInfos = roomJoinInfoRepository.findByRoomIdAndUserId(roomId, userId);

        if (sprinklingMoney.size() == 0){
            throw new InValidInputDataException("뿌리기 정보가 없거나 받기 가능한 기간이 지났습니다.");
        }

        if (roomJoinInfos.size() == 0){
            throw new InValidInputDataException("참여하지 않은 채팅방입니다.");
        }

        if (sprinklingMoney.get(0).getCreator().getId() == userId) {
            throw new InValidInputDataException("뿌리기를 한 사람은 받을 수 없습니다.");
        }

        if (sprinklingMoney.get(0).getAmountStatus() == AmountStatus.COMPLETED) {
            throw new InValidInputDataException("남은 뿌리기 금액이 없습니다");
        }

        for (SprinklingMoneyDistributionInfo distributionInfo : sprinklingMoney.get(0).getSprinkingMoneyDistributionInfos()){
            if (distributionInfo.getUser() != null && distributionInfo.getUser().getId() == userId){
                throw  new InValidInputDataException("받기는 한번만 가능합니다.");
            }
        }

    }

    private Long pickUp(List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos, Long userId){
        SprinklingMoneyDistributionInfo sprinklingMoneyDistributionInfo = sprinklingMoneyDistributionInfos.get(0);
        User user = userRepository.findOne(userId);
        sprinklingMoneyDistributionInfo.pickUp(user);
        if (sprinklingMoneyDistributionInfos.size() == 1){
            sprinklingMoneyDistributionInfo.getSprinklingMoney().completePikingUp();
        }
        return sprinklingMoneyDistributionInfo.getId();
    }
}
