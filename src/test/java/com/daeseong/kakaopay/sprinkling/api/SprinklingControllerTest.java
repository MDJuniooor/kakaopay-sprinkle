package com.daeseong.kakaopay.sprinkling.api;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;
import com.daeseong.kakaopay.sprinkling.contants.HttpStatusCode;
import com.daeseong.kakaopay.sprinkling.dto.SingleResponse;
import com.daeseong.kakaopay.sprinkling.dto.SprinkleMoneyRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.daeseong.kakaopay.sprinkling.contants.BusinessStatusCode.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SprinklingControllerTest {

    @Autowired
    SprinklingController sprinklingController;

    @Test
    public void shouldSprinkleMoney(){
        String roomId = "3-room";
        Long userId = 1L;
        int numberForPickingUp = 2;
        int amount = 1000;

        SingleResponse singleResponse = sprinklingController.sprinkleMoney(roomId, userId
                , new SprinkleMoneyRequest(numberForPickingUp, amount));

        assertEquals(singleResponse.getCode(), HttpStatusCode.HTTP_200_OK.getCode());
        assertEquals(singleResponse.getMsg(), HttpStatusCode.HTTP_200_OK.getMsg());
    }

    @Test
    public void shouldSprinkleMoneyFailForBE1001(){
        String roomId = "3-room";
        Long strangeUserId = 5L;
        int numberForPickingUp = 2;
        int amount = 100;

        try {
            sprinklingController.sprinkleMoney(roomId, strangeUserId, new SprinkleMoneyRequest(numberForPickingUp, amount));
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE1001.getCode());
            assertEquals(e.getMsg(), BE1001.getMsg());
        }

    }

    @Test
    public void shouldSprinkleMoneyFailForBE1005(){
        String roomId = "3-room";
        Long userId = 1L;
        int noOneForPickingUp = 0;
        int amount = 100;

        try {
            sprinklingController.sprinkleMoney(roomId, userId, new SprinkleMoneyRequest(noOneForPickingUp, amount));
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE1005.getCode());
            assertEquals(e.getMsg(), BE1005.getMsg());
        }
    }

    @Test
    public void shouldSprinkleMoneyFailForBE2001(){
        String strangeRoomId = "cannot-find-room";
        Long userId = 1L;
        int numberForPickingUp = 2;
        int amount = 100;

        try {
            sprinklingController.sprinkleMoney(strangeRoomId, userId, new SprinkleMoneyRequest(numberForPickingUp, amount));
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE2001.getCode());
            assertEquals(e.getMsg(), BE2001.getMsg());
        }
    }

    @Test
    public void shouldSprinkleMoneyFailForBE2002(){
        String lonelyLoomId = "1-room";
        Long userId = 1L;
        int numberForPickingUp = 1;
        int amount = 100;

        try {
            sprinklingController.sprinkleMoney(lonelyLoomId, userId, new SprinkleMoneyRequest(numberForPickingUp, amount));
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE2002.getCode());
            assertEquals(e.getMsg(), BE2002.getMsg());
        }

    }

    @Test
    public void shouldSprinkleMoneyFailForBE3001(){
        String roomId = "3-room";
        Long userId = 1L;
        int tooManyNumberForPickingUp = 5;
        int amount = 100;

        try {
            sprinklingController.sprinkleMoney(roomId, userId, new SprinkleMoneyRequest(tooManyNumberForPickingUp, amount));
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE3001.getCode());
            assertEquals(e.getMsg(), BE3001.getMsg());
        }

    }

    @Test
    public void shouldSprinkleMoneyFailForBE3002(){
        String roomId = "3-room";
        Long userId = 1L;
        int numberForPickingUp = 2;
        int poorAmount = 1;

        try {
            sprinklingController.sprinkleMoney(roomId, userId, new SprinkleMoneyRequest(numberForPickingUp, poorAmount));
        } catch (BusinessException e) {
            assertEquals(e.getCode(), BE3002.getCode());
            assertEquals(e.getMsg(), BE3002.getMsg());
        }

    }

}