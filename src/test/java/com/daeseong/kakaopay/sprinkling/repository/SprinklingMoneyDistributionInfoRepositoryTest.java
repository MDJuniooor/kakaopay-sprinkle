package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.Room;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoneyDistributionInfo;
import com.daeseong.kakaopay.sprinkling.entity.User;
import org.junit.Before;
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
public class SprinklingMoneyDistributionInfoRepositoryTest {

    @Autowired SprinklingMoneyDistributionInfoRepository sprinklingMoneyDistributionInfoRepository;
    @Autowired SprinklingMoneyRepository sprinklingMoneyRepository;
    @Autowired UserRepository userRepository;
    @Autowired RoomRepository roomRepository;

    private Long sprinklingMoneyId;

    @Before
    public void setting(){
        User user = userRepository.findOne(1L);
        Room room = roomRepository.findOne("4-room");
        String token = "token";
        int amount = 100;
        List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos = new ArrayList<>();
        sprinklingMoneyDistributionInfos.add(SprinklingMoneyDistributionInfo.createSprinklingMoneyDistributionInfo(10));
        sprinklingMoneyDistributionInfos.add(SprinklingMoneyDistributionInfo.createSprinklingMoneyDistributionInfo(90));

        SprinklingMoney sprinklingMoney = SprinklingMoney.createSprinklingMoney(user, room, token, amount, sprinklingMoneyDistributionInfos);
        sprinklingMoneyRepository.save(sprinklingMoney);

        sprinklingMoneyId = sprinklingMoney.getId();
    }

    @Test
    public void shouldFindBySprinklingMoneyDistributionIdAndValidPickingUp(){
        List<SprinklingMoneyDistributionInfo> distributionInfos = sprinklingMoneyDistributionInfoRepository
                .findBySprinklingMoneyDistributionIdAndValidPickingUp(sprinklingMoneyId);

        assertEquals(distributionInfos.size(), 2);
    }

    @Test
    public void shouldFindById(){
        SprinklingMoneyDistributionInfo distributionInfo = sprinklingMoneyDistributionInfoRepository
                .findOne(1L);

        assertEquals((long) distributionInfo.getId(), 1L);
    }
}