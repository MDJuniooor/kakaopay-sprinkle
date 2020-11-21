package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.InValidInputDataException;
import com.daeseong.kakaopay.sprinkling.entity.*;
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
public class SprinklingMoneyCheckingServiceTest {

    @Autowired
    RoomJoinInfoRepository roomJoinInfoRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SprinklingMoneyRepository sprinklingMoneyRepository;
    @Autowired
    SprinklingMoneyCheckingService sprinklingMoneyCheckingService;

    @Before
    public void setting(){
        List<User> users = createUsers(5);
        Room room = createRoom();
        joinRoomForUsers(users, room);
        String token = "ABC";
        int amount = 100;
        List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos = createSprinklingMoneyDistributionInfos(users.size(),amount);
        SprinklingMoney sprinklingMoney = SprinklingMoney.createSprinklingMoney(users.get(0),room,token,amount,sprinklingMoneyDistributionInfos);
        sprinklingMoneyRepository.save(sprinklingMoney);
    }

    @Test
    public void getSprinklingMoneyInfo() {
        SprinklingMoney sprinklingMoney = sprinklingMoneyRepository.findOne(1L);
        SprinklingMoney findSprinklingMoney;
        String roomId = sprinklingMoney.getRoom().getId();
        Long userId = sprinklingMoney.getCreator().getId();
        String token = sprinklingMoney.getToken();

        findSprinklingMoney = sprinklingMoneyCheckingService.excute(roomId, userId, token);

        assertEquals(sprinklingMoney, findSprinklingMoney);

    }

    @Test(expected = InValidInputDataException.class)
    public void failGetSprinklingMoneyInfoUsingWrongRoomId(){
        SprinklingMoney sprinklingMoney = sprinklingMoneyRepository.findOne(1L);
        String roomId = "wrong-room-id";
        Long userId = sprinklingMoney.getCreator().getId();
        String token = sprinklingMoney.getToken();

        sprinklingMoneyCheckingService.excute(roomId, userId, token);

        fail("예외 발생");
    }

    @Test(expected = InValidInputDataException.class)
    public void failGetSprinklingMoneyInfoUsingWrongCreatorId(){
        SprinklingMoney sprinklingMoney = sprinklingMoneyRepository.findOne(1L);
        String roomId = sprinklingMoney.getRoom().getId();
        Long userId = sprinklingMoney.getCreator().getId() + (long) 100000;
        String token = sprinklingMoney.getToken();

        sprinklingMoneyCheckingService.excute(roomId, userId, token);

        fail("예외 발생");
    }

    @Test(expected = InValidInputDataException.class)
    public void failGetSprinklingMoneyInfoUsingWrongToken(){
        SprinklingMoney sprinklingMoney = sprinklingMoneyRepository.findOne(1L);
        String roomId = sprinklingMoney.getRoom().getId();
        Long userId = sprinklingMoney.getCreator().getId();
        String token = "wrongtoken";

        sprinklingMoneyCheckingService.excute(roomId, userId, token);

        fail("예외 발생");

    }

    @Test(expected = InValidInputDataException.class)
    public void failGetSprinklingMoneyInfoUsingInValidTimeForRead(){
        SprinklingMoney sprinklingMoney = sprinklingMoneyRepository.findOne(1L);
        sprinklingMoney.setValidTimeForRead(LocalDateTime.now().minusMinutes(10));
        sprinklingMoneyRepository.save(sprinklingMoney);

        String roomId = sprinklingMoney.getRoom().getId();
        Long userId = sprinklingMoney.getCreator().getId();
        String token = sprinklingMoney.getToken();

        sprinklingMoneyCheckingService.excute(roomId, userId, token);

        fail("예외 발생");

    }

    private Room createRoom(){
        Room room = new Room();
        roomRepository.save(room);
        return room;
    }

    private List<User> createUsers(int number){
        List<User> users = new ArrayList<>();
        User user = null;
        for (int i=0; i < number; i++){
            user = new User();
            userRepository.save(user);
            users.add(user);
        }
        return users;
    }

    private void joinRoomForUsers(List<User> users, Room room){
        RoomJoinInfo roomJoinInfo = null;
        for (User user: users){
            roomJoinInfo = RoomJoinInfo.joinRoom(user, room);
            roomJoinInfoRepository.save(roomJoinInfo);
        }
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