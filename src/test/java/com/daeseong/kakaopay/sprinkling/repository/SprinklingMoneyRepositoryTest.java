package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.*;
import com.daeseong.kakaopay.sprinkling.util.KakaoPayRandomGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SprinklingMoneyRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired RoomRepository roomRepository;
    @Autowired RoomJoinInfoRepository roomJoinInfoRepository;
    @Autowired SprinklingMoneyDistributionInfoRepository sprinklingMoneyDistributionInfoRepository;

    @Autowired
    SprinklingMoneyRepository sprinklingMoneyRepository;

    @Test
    public void shouldCreateSprinklingMoney(){
        List<User> users = createUsers(5);
        Room room = createRoom();
        joinRoomForUsers(users, room);
        String token = createToken();
        int amount = 100;
        List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos = createSprinklingMoneyDistributionInfos(users.size(),amount);


        SprinklingMoney sprinklingMoney = SprinklingMoney.createSprinklingMoney(users.get(0),room,token,amount,sprinklingMoneyDistributionInfos);
        sprinklingMoneyRepository.save(sprinklingMoney);

        assertEquals(sprinklingMoney.getCreator().getId(), users.get(0).getId());
        assertEquals(sprinklingMoney.getAmountStatus(), AmountStatus.AVAILABLE);
        assertEquals(sprinklingMoney.getRoom().getId(), room.getId());
        for (SprinklingMoneyDistributionInfo sprinklingMoneyDistributionInfo : sprinklingMoney.getSprinkingMoneyDistributionInfos()){
            assertEquals(sprinklingMoneyDistributionInfo.getAmountStatus(), AmountStatus.AVAILABLE);
        }
        assertEquals(sprinklingMoney.getToken(),token);

    }

    @Test
    public void shouldFindValidSprinklingMoneyForGettingMoney() {
        List<User> users = createUsers(5);
        Room room = createRoom();
        joinRoomForUsers(users, room);
        String token = createToken();
        int amount = 100;
        List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos = createSprinklingMoneyDistributionInfos(users.size(),amount);
        SprinklingMoney sprinklingMoney = SprinklingMoney.createSprinklingMoney(users.get(0),room,token,amount,sprinklingMoneyDistributionInfos);
        sprinklingMoneyRepository.save(sprinklingMoney);

        assertEquals(sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, room.getId()).size(),1);

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

    private String createToken(){
        return "ABC";
    }
}