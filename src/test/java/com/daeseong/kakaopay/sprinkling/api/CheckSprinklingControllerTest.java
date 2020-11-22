package com.daeseong.kakaopay.sprinkling.api;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;

import com.daeseong.kakaopay.sprinkling.contants.HttpStatusCode;
import com.daeseong.kakaopay.sprinkling.dto.ResponseDataForSprinkling;
import com.daeseong.kakaopay.sprinkling.dto.SingleResponse;
import com.daeseong.kakaopay.sprinkling.dto.SprinkleMoneyRequest;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import static com.daeseong.kakaopay.sprinkling.contants.BusinessStatusCode.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CheckSprinklingControllerTest {

    @Autowired
    CheckSprinklingController checkSprinklingController;
    @Autowired
    SprinklingController sprinklingController;
    @Autowired
    SprinklingMoneyRepository sprinklingMoneyRepository;

    private final Long USER_ID = 1L;
    private final String ROOM_ID = "3-room";
    private String TOKEN;

    @Before
    public void setting(){
        int numberForPickingUp = 2;
        int amount = 1000;

        ResponseDataForSprinkling response= (ResponseDataForSprinkling) sprinklingController
                .sprinkleMoney(ROOM_ID, USER_ID, new SprinkleMoneyRequest(numberForPickingUp, amount))
                .getData();
        TOKEN = response.getToken();
    }

    @Test
    public void shouldGetSprinklingMoneyInfoSuccess() {
        SingleResponse response = checkSprinklingController.getSprinklingMoneyInfo(ROOM_ID, USER_ID, TOKEN);

        assertEquals(response.getCode(), HttpStatusCode.HTTP_200_OK.getCode());
        assertEquals(response.getMsg(), HttpStatusCode.HTTP_200_OK.getMsg());
    }

    @Test
    public void shouldGetSprinklingMoneyInfoFailForBE1001(){
        String strangeRoomId = "wrong-room-id";

        try {
            checkSprinklingController.getSprinklingMoneyInfo(strangeRoomId, USER_ID, TOKEN);
        } catch (BusinessException e){
            assertEquals(e.getCode(), BE1001.getCode());
            assertEquals(e.getMsg(), BE1001.getMsg());
        }
    }

    @Test
    public void shouldGetSprinklingMoneyInfoFailForBE1002(){
        Long strangeUserId = 2L;

        try {
            checkSprinklingController.getSprinklingMoneyInfo(ROOM_ID, strangeUserId, TOKEN);
        } catch (BusinessException e){
            assertEquals(e.getCode(), BE1002.getCode());
            assertEquals(e.getMsg(), BE1002.getMsg());
        }
    }

    @Test
    public void shouldGetSprinklingMoneyInfoFailForBE3003WithWrongToken(){
        String strangeToken = "wrong-token";

        try {
            checkSprinklingController.getSprinklingMoneyInfo(ROOM_ID, USER_ID, strangeToken);
        } catch (BusinessException e){
            assertEquals(e.getCode(), BE3003.getCode());
            assertEquals(e.getMsg(), BE3003.getMsg());
        }
    }


    @Test
    public void shouldGetSprinklingMoneyInfoFailForBE3003WithTokenExpired(){
        SprinklingMoney sprinklingMoney = sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(TOKEN, ROOM_ID).get(0);
        sprinklingMoney.setValidTimeForRead(LocalDateTime.now().minusMinutes(10));
        sprinklingMoneyRepository.save(sprinklingMoney);

        try {
            checkSprinklingController.getSprinklingMoneyInfo(ROOM_ID, USER_ID, TOKEN);
        } catch (BusinessException e){
            assertEquals(e.getCode(), BE3003.getCode());
            assertEquals(e.getMsg(), BE3003.getMsg());
        }

    }

}