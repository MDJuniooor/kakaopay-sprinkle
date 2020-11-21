package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;
import com.daeseong.kakaopay.sprinkling.entity.*;
import com.daeseong.kakaopay.sprinkling.repository.RoomJoinInfoRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyDistributionInfoRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import com.daeseong.kakaopay.sprinkling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.daeseong.kakaopay.sprinkling.contants.BusinessStatusCode.*;

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
            throw new BusinessException(BE3004.getCode(), BE3004.getMsg());
        }

        if (roomJoinInfos.size() == 0){
            throw new BusinessException(BE1001.getCode(), BE1001.getMsg());
        }

        if (sprinklingMoney.get(0).getCreator().getId() == userId) {
            throw new BusinessException(BE1003.getCode(), BE1003.getMsg());
        }

        if (sprinklingMoney.get(0).getAmountStatus() == AmountStatus.COMPLETED) {
            throw new BusinessException(BE3005.getCode(), BE3005.getMsg());
        }

        for (SprinklingMoneyDistributionInfo distributionInfo : sprinklingMoney.get(0).getSprinkingMoneyDistributionInfos()){
            if (distributionInfo.getUser() != null && distributionInfo.getUser().getId() == userId){
                throw new BusinessException(BE1004.getCode(), BE1004.getMsg());
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
