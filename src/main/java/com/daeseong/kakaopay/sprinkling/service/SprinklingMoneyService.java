package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.InValidInputDataException;
import com.daeseong.kakaopay.sprinkling.entity.*;
import com.daeseong.kakaopay.sprinkling.repository.RoomJoinInfoRepository;
import com.daeseong.kakaopay.sprinkling.repository.RoomRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import com.daeseong.kakaopay.sprinkling.repository.UserRepository;
import com.daeseong.kakaopay.sprinkling.util.KakaoPayRandomGenerator;
import com.daeseong.kakaopay.sprinkling.contants.SprinklingMoneyConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.daeseong.kakaopay.sprinkling.contants.SprinklingMoneyConstant.TOKEN_LENGTH;

@Service
@RequiredArgsConstructor
@Transactional
public class SprinklingMoneyService {

    private final RoomRepository roomRepository;
    private final RoomJoinInfoRepository roomJoinInfoRepository;
    private final UserRepository userRepository;
    private final SprinklingMoneyRepository sprinklingMoneyRepository;

    public String execute(String roomId, Long userId, int numberForSprinkling, int amount){
        /**
         * case 1. number >= Room member return fail
         * case 2. number <  Room member && can't create token
         * case 3. number <  Room member && can create token (global caching)
         *        amount - Random(amount) n-1번 돌림 저장
         */
        validate(roomId, userId, numberForSprinkling, amount);

        String token = createToken(roomId);

        if (token == SprinklingMoneyConstant.TOKEN_CREATE_FAILURE){
            return "";
        }

        KakaoPayRandomGenerator kakaoPayRandomGenerator = new KakaoPayRandomGenerator();
        List<Integer> MoneyForDistribution = kakaoPayRandomGenerator.createMoneyForDistribution(numberForSprinkling, amount);

        SprinklingMoney sprinklingMoney =  sprinklingMoneyRepository
                .findOne(createSprinklingMoney(roomId, userId, numberForSprinkling, token,
                amount, MoneyForDistribution));

        return sprinklingMoney.getToken();


    }

    public String createToken(String roomId){
        KakaoPayRandomGenerator kakaoPayRandomGenerator = new KakaoPayRandomGenerator();
        String token = kakaoPayRandomGenerator.createRandomString(TOKEN_LENGTH);
        while(sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, roomId).size() > 0){
            token = kakaoPayRandomGenerator.createRandomString(TOKEN_LENGTH);
        }
        return token;
    }


    public void validate(String roomId, Long userId, int numberForSprinkling, int amount){
        Room room = roomRepository.findOne(roomId);
        List<RoomJoinInfo> roomMembers = roomJoinInfoRepository.findByRoomId(roomId);

        if (room == null) {
            throw new InValidInputDataException("채팅방이 존재하지 않습니다.");
        }

        if (roomMembers.size() < 2) {
            throw new InValidInputDataException("채팅방의 멤버는 적어도 2명 이상 이어야 합니다.");
        }

        if (roomMembers.stream()
                .filter(m -> m.getUser().getId() == userId)
                .count() == 0){
            throw new InValidInputDataException("참여하지 않은 채팅방입니다.");
        }

        if (roomMembers.size() <= numberForSprinkling){
            throw new InValidInputDataException("뿌리기를 받을 멤버의 수는 채팅방 참여 멤버의 수보다 더 적야 합니다");
        }

        if (amount < numberForSprinkling) {
            throw new InValidInputDataException("뿌릴 돈은 멤버 수보다 커야 합니다.");
        }
    }

    public Long createSprinklingMoney(String roomId, Long userId, int numberForSprinkling, String token,
            int amount, List<Integer> moneyForDistribution){
        Room room = roomRepository.findOne(roomId);
        User user = userRepository.findOne(userId);
        List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos = new ArrayList<>();

        for(Integer dividedAmount : moneyForDistribution){
            SprinklingMoneyDistributionInfo sprinklingMoneyDistributionInfo = SprinklingMoneyDistributionInfo
                    .createSprinklingMoneyDistributionInfo(dividedAmount);
            sprinklingMoneyDistributionInfos.add(sprinklingMoneyDistributionInfo);
        }

        SprinklingMoney sprinklingMoney = SprinklingMoney.createSprinklingMoney(user, room, token, amount, sprinklingMoneyDistributionInfos);
        sprinklingMoneyRepository.save(sprinklingMoney);
        return sprinklingMoney.getId();
    }
}
