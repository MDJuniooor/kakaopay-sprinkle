package com.daeseong.kakaopay.sprinkling.api;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;
import com.daeseong.kakaopay.sprinkling.contants.BusinessStatusCode;
import com.daeseong.kakaopay.sprinkling.dto.ResponseDataForPickingUp;
import com.daeseong.kakaopay.sprinkling.dto.ResponseDataForSprinkling;
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
public class PickingUpControllerTest {

    @Autowired
    PickingUpController pickingUpController;
    @Autowired
    SprinklingController sprinklingController;
    @Autowired
    SprinklingMoneyRepository sprinklingMoneyRepository;

    private final Long USER_ID = 1L;
    private final String ROOM_ID = "4-room";
    private String TOKEN;

    @Before
    public void setting(){
        int numberForPickingUp = 2;
        int amount = 1000;

        ResponseDataForSprinkling response = (ResponseDataForSprinkling) sprinklingController
                .sprinkleMoney(ROOM_ID, USER_ID, new SprinkleMoneyRequest(numberForPickingUp, amount))
                .getData();
        TOKEN = response.getToken();
    }

    @Test
    public void shouldPickUpMoneySuccess() {
        Long userId = 2L;
        int pickedUpAmount = 0;
        ResponseDataForPickingUp response;

        response = (ResponseDataForPickingUp) pickingUpController.pickUpMoney(ROOM_ID, userId, TOKEN).getData();
        pickedUpAmount = response.getAmount();

        assertNotEquals(pickedUpAmount, 0);

    }

    @Test
    public void shouldPickUpMoneyFailForBE1001(){
        Long strangeUserId = 4L;

        try {
            pickingUpController.pickUpMoney(ROOM_ID, strangeUserId, TOKEN);
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE1001.getCode());
            assertEquals(e.getMsg(), BE1001.getMsg());
        }

    }

    @Test
    public void shouldPickUpMoneyFailForBE1003(){

        try {
            pickingUpController.pickUpMoney(ROOM_ID, USER_ID, TOKEN);
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE1003.getCode());
            assertEquals(e.getMsg(), BE1003.getMsg());
        }

    }

    @Test
    public void shouldPickUpMoneyFailForBE1004(){
        Long userId = 2L;

        try {
            pickingUpController.pickUpMoney(ROOM_ID, userId, TOKEN);
            pickingUpController.pickUpMoney(ROOM_ID, userId, TOKEN);
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE1004.getCode());
            assertEquals(e.getMsg(), BE1004.getMsg());
        }
    }

    @Test
    public void shouldPickUpMoneyFailForBE3004(){
        Long userId = 2L;

        SprinklingMoney sprinklingMoney = sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(TOKEN, ROOM_ID).get(0);
        sprinklingMoney.setValidTimeForPickingUpMoney(LocalDateTime.now().minusMinutes(10));
        sprinklingMoneyRepository.save(sprinklingMoney);

        try {
            pickingUpController.pickUpMoney(ROOM_ID, userId, TOKEN);
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE3004.getCode());
            assertEquals(e.getMsg(), BE3004.getMsg());
        }

    }

    @Test
    public void shouldPickUpMoneyFailForBE3005(){
        Long firstUserId = 2L;
        Long secondUserId = 3L;
        Long thirdUserId = 4L;

        try {
            pickingUpController.pickUpMoney(ROOM_ID, firstUserId, TOKEN);
            pickingUpController.pickUpMoney(ROOM_ID, secondUserId, TOKEN);
            pickingUpController.pickUpMoney(ROOM_ID, thirdUserId, TOKEN);
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE3005.getCode());
            assertEquals(e.getMsg(), BE3005.getMsg());
        }

    }
}