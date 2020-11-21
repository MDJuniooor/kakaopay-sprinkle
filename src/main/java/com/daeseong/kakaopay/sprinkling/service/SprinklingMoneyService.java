package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;
import com.daeseong.kakaopay.sprinkling.contants.BusinessStatusCode;
import com.daeseong.kakaopay.sprinkling.entity.*;
import com.daeseong.kakaopay.sprinkling.repository.*;
import com.daeseong.kakaopay.sprinkling.util.KakaoPayRandomGenerator;
import com.daeseong.kakaopay.sprinkling.contants.SprinklingMoneyConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.daeseong.kakaopay.sprinkling.contants.BusinessStatusCode.*;
import static com.daeseong.kakaopay.sprinkling.contants.SprinklingMoneyConstant.TOKEN_LENGTH;

@Service
@RequiredArgsConstructor
@Transactional
public class SprinklingMoneyService {

    private final RoomRepository roomRepository;
    private final RoomJoinInfoRepository roomJoinInfoRepository;
    private final UserRepository userRepository;
    private final SprinklingMoneyRepository sprinklingMoneyRepository;
    private final TokenCacheRepository tokenCacheRepository;

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
        while(tokenCacheRepository.existsToken(token, roomId)){
            token = kakaoPayRandomGenerator.createRandomString(TOKEN_LENGTH);
        };
        tokenCacheRepository.setToken(token, roomId);
//        while(sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, roomId).size() > 0){
//            token = kakaoPayRandomGenerator.createRandomString(TOKEN_LENGTH);
//        }
        return token;
    }


    public void validate(String roomId, Long userId, int numberForSprinkling, int amount){
        Room room = roomRepository.findOne(roomId);
        List<RoomJoinInfo> roomMembers = roomJoinInfoRepository.findByRoomId(roomId);
        BusinessException e = new BusinessException(BE2001.getCode(), BE2001.getMsg());

        if (room == null) {
            throw new BusinessException(BE2001.getCode(), BE2001.getMsg());
        }

        if (roomMembers.size() < 2) {
            throw new BusinessException(BE2002.getCode(), BE2002.getMsg());
        }

        if (roomMembers.stream()
                .filter(m -> m.getUser().getId() == userId)
                .count() == 0){
            throw new BusinessException(BE1001.getCode(), BE1001.getMsg());
        }

        if (roomMembers.size() <= numberForSprinkling){
            throw new BusinessException(BE3001.getCode(), BE3001.getMsg());
        }

        if (amount < numberForSprinkling) {
            throw new BusinessException(BE3002.getCode(), BE3002.getMsg());
        }

        if (amount <= 0) {
            throw new BusinessException(BE3006.getCode(), BE3006.getMsg());
        }

        if (numberForSprinkling <= 0) {
            throw new BusinessException(BE1005.getCode(), BE1005.getMsg());
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
