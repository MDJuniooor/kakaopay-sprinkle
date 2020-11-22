package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.advice.exception.BusinessException;
import com.daeseong.kakaopay.sprinkling.entity.AmountStatus;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SprinklingMoneyServiceTest {

    @Autowired
    SprinklingMoneyService sprinklingMoneyService;
    @Autowired SprinklingMoneyRepository sprinklingMoneyRepository;

    @Test
    public void shouldSprinkleMoneySuccess(){
        String roomId = "3-room";
        Long userId = 1L;
        int numberForPickingUp = 2;
        int amount = 100;
        String token;
        SprinklingMoney sprinklingMoney;

        token = sprinklingMoneyService.execute(roomId, userId, numberForPickingUp, amount);

        sprinklingMoney = sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, roomId).get(0);

        assertEquals(sprinklingMoney.getAmount(), amount);
        assertEquals(sprinklingMoney.getCreator().getId(), userId);
        assertEquals(sprinklingMoney.getAmountStatus(), AmountStatus.AVAILABLE);
        assertEquals(sprinklingMoney.getRoom().getId(), roomId);
        assertEquals(sprinklingMoney.getToken(), token);
    }

    @Test(expected = BusinessException.class)
    public void shouldSprinkleMoneyFailForBE1001(){
        String roomId = "3-room";
        Long strangeUserId = 5L;
        int numberForPickingUp = 2;
        int amount = 100;

        sprinklingMoneyService.execute(roomId, strangeUserId, numberForPickingUp, amount);

        fail("예외 발생");
    }

    @Test(expected = BusinessException.class)
    public void shouldSprinkleMoneyFailForBE1005(){
        String roomId = "3-room";
        Long userId = 1L;
        int noOneForPickingUp = 0;
        int amount = 100;

        sprinklingMoneyService.execute(roomId, userId, noOneForPickingUp, amount);

        fail("예외 발생");
    }

    @Test(expected = BusinessException.class)
    public void shouldSprinkleMoneyFailForBE2001(){
        String strangeRoomId = "cannot-find-room";
        Long userId = 1L;
        int numberForPickingUp = 2;
        int amount = 100;

        sprinklingMoneyService.execute(strangeRoomId, userId, numberForPickingUp, amount);

        fail("예외 발생");
    }

    @Test(expected = BusinessException.class)
    public void shouldSprinkleMoneyFailForBE2002(){
        String LonelyRoomId = "1-room";
        Long userId = 1L;
        int numberForPickingUp = 1;
        int amount = 100;
        String token;

        token = sprinklingMoneyService.execute(LonelyRoomId, userId, numberForPickingUp, amount);

        sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, LonelyRoomId).get(0);

        fail("예외 발생");
    }

    @Test(expected = BusinessException.class)
    public void shouldSprinkleMoneyFailForBE3001(){
        String roomId = "3-room";
        Long userId = 1L;
        int tooManyNumberForPickingUp = 5;
        int amount = 100;
        String token;

        token = sprinklingMoneyService.execute(roomId, userId, tooManyNumberForPickingUp, amount);

        sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, roomId).get(0);

        fail("예외 발생");
    }

    @Test(expected = BusinessException.class)
    public void shouldSprinkleMoneyFailForBE3002(){
        String roomId = "3-room";
        Long userId = 1L;
        int numberForPickingUp = 2;
        int poorAmount = 1;
        String token;

        token = sprinklingMoneyService.execute(roomId, userId, numberForPickingUp, poorAmount);

        sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, roomId).get(0);

        fail("예외 발생");
    }

    @Test(expected = BusinessException.class)
    public void shouldSprinkleMoneyFailForBE3006(){
        String roomId = "3-room";
        Long userId = 1L;
        int numberForPickingUp = 2;
        int zeroAmount = 0;
        String token;

        token = sprinklingMoneyService.execute(roomId, userId, numberForPickingUp, zeroAmount);

        sprinklingMoneyRepository.findByTokenAndRoomIdForPickingUpMoney(token, roomId).get(0);

        fail("예외 발생");
    }
}