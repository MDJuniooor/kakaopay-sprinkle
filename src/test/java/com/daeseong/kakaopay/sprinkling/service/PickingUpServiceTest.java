package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;
import com.daeseong.kakaopay.sprinkling.entity.Room;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoneyDistributionInfo;
import com.daeseong.kakaopay.sprinkling.entity.User;
import com.daeseong.kakaopay.sprinkling.repository.RoomJoinInfoRepository;
import com.daeseong.kakaopay.sprinkling.repository.RoomRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import com.daeseong.kakaopay.sprinkling.repository.UserRepository;
import com.daeseong.kakaopay.sprinkling.util.KakaoPayRandomGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PickingUpServiceTest {

    @Autowired
    RoomJoinInfoRepository roomJoinInfoRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SprinklingMoneyRepository sprinklingMoneyRepository;
    @Autowired
    PickingUpService pickingUpService;

    private final Long USER_ID = 1L;
    private final String ROOM_ID = "3-room";
    private final String TOKEN = "ABC";

    @Before
    public void setting(){
        User user = userRepository.findOne(USER_ID);
        Room room = roomRepository.findOne(ROOM_ID);
        int numberForPickingUp = 1;
        int amount = 100;

        List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos = createSprinklingMoneyDistributionInfos(numberForPickingUp, amount);
        SprinklingMoney sprinklingMoney = SprinklingMoney.createSprinklingMoney(user, room, TOKEN, amount, sprinklingMoneyDistributionInfos);
        sprinklingMoneyRepository.save(sprinklingMoney);
    }

    @Test
    public void shouldPickUpMoneySuccess() {
        Long userId = 2L;
        int pickedUpAmount = 0;

        pickedUpAmount = pickingUpService.execute(ROOM_ID, userId, TOKEN);

        assertNotEquals(pickedUpAmount, 0);

    }

    @Test(expected = BusinessException.class)
    public void shouldPickUpMoneyFailForBE1001(){
        Long strangeUserId = 4L;

        pickingUpService.execute(ROOM_ID, strangeUserId, TOKEN);

        fail("예외 발생");

    }

    @Test(expected = BusinessException.class)
    public void shouldPickUpMoneyFailForBE1003(){

        pickingUpService.execute(ROOM_ID, USER_ID, TOKEN);

        fail("예외 발생");

    }

    @Test(expected = BusinessException.class)
    public void shouldPickUpMoneyFailForBE1004(){
        Long userId = 2L;

        pickingUpService.execute(ROOM_ID, userId, TOKEN);
        pickingUpService.execute(ROOM_ID, userId, TOKEN);

        fail("예외 발생");
    }

    @Test(expected = BusinessException.class)
    public void shouldPickUpMoneyFailForBE3004(){
        Long userId = 2L;

        SprinklingMoney sprinklingMoney = sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(TOKEN, ROOM_ID).get(0);
        sprinklingMoney.setValidTimeForPickingUpMoney(LocalDateTime.now().minusMinutes(10));
        sprinklingMoneyRepository.save(sprinklingMoney);

        pickingUpService.execute(ROOM_ID, userId, TOKEN);

        fail("예외 발생");

    }

    @Test(expected = BusinessException.class)
    public void shouldPickUpMoneyFailForBE3005(){
        Long firstUserId = 2L;
        Long secondUserId = 3L;

        pickingUpService.execute(ROOM_ID, firstUserId, TOKEN);
        pickingUpService.execute(ROOM_ID, secondUserId, TOKEN);

        fail("예외 발생");

    }

    private List<SprinklingMoneyDistributionInfo> createSprinklingMoneyDistributionInfos(int number, int amount) {
        KakaoPayRandomGenerator kakaoPayRandomGenerator = new KakaoPayRandomGenerator();
        List<Integer> dividedMoneyList = kakaoPayRandomGenerator.createMoneyForDistribution(number, amount);
        List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos = new ArrayList<>();
        SprinklingMoneyDistributionInfo sprinklingMoneyDistributionInfo;
        for(Integer dividedMoney: dividedMoneyList){
            sprinklingMoneyDistributionInfo = SprinklingMoneyDistributionInfo.createSprinklingMoneyDistributionInfo(dividedMoney);
            sprinklingMoneyDistributionInfos.add(sprinklingMoneyDistributionInfo);
        }
        return sprinklingMoneyDistributionInfos;
    }
}