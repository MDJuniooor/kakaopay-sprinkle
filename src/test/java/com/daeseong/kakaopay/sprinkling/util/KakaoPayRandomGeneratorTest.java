package com.daeseong.kakaopay.sprinkling.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class KakaoPayRandomGeneratorTest {

    @Test
    public void testCreateMoneyForDistribution(){
        KakaoPayRandomGenerator kakaoPayRandomGenerator = new KakaoPayRandomGenerator();
        int number = 2; int amount = 10;
        List<Integer> result = null;

        result = kakaoPayRandomGenerator.createMoneyForDistribution(number, amount);
        System.out.println(result.toString());
        assertTrue(isValid(result, number, amount));

        number = 3; amount = 4;
        result = kakaoPayRandomGenerator.createMoneyForDistribution(number, amount);
        System.out.println(result.toString());
        assertTrue(isValid(result, number, amount));

        number = 7; amount = 10;
        result = kakaoPayRandomGenerator.createMoneyForDistribution(number, amount);
        System.out.println(result.toString());
        assertTrue(isValid(result, number, amount));

        number = 3; amount = 3;
        result = kakaoPayRandomGenerator.createMoneyForDistribution(number, amount);
        System.out.println(result.toString());
        assertTrue(isValid(result, number, amount));

        number = 3; amount = 100;
        result = kakaoPayRandomGenerator.createMoneyForDistribution(number, amount);
        System.out.println(result.toString());
        assertTrue(isValid(result, number, amount));

        number = 4; amount = 7;
        result = kakaoPayRandomGenerator.createMoneyForDistribution(number, amount);
        System.out.println(result.toString());
        assertTrue(isValid(result, number, amount));
    }

    private boolean isValid(List<Integer> list, int number, int amount){
        if (list.size() != number){
            return false;
        }
        if (!list.stream().allMatch(e -> e > 0 && e <= amount)){
            return false;
        }
        if (list.stream().mapToInt(Integer::intValue).sum() != amount){
            return false;
        }
        return true;
    }

    @Test
    public void testCreateRandomString(){
        KakaoPayRandomGenerator kakaoPayRandomGenerator = new KakaoPayRandomGenerator();

        assertEquals(kakaoPayRandomGenerator.createRandomString(1).length(),1);
        assertEquals(kakaoPayRandomGenerator.createRandomString(2).length(),2);
        assertEquals(kakaoPayRandomGenerator.createRandomString(3).length(),3);
        assertEquals(kakaoPayRandomGenerator.createRandomString(4).length(),4);
        assertEquals(kakaoPayRandomGenerator.createRandomString(5).length(),5);
        assertEquals(kakaoPayRandomGenerator.createRandomString(6).length(),6);
        assertEquals(kakaoPayRandomGenerator.createRandomString(7).length(),7);
        assertEquals(kakaoPayRandomGenerator.createRandomString(8).length(),8);
        assertEquals(kakaoPayRandomGenerator.createRandomString(9).length(),9);
        assertEquals(kakaoPayRandomGenerator.createRandomString(10).length(),10);
        assertEquals(kakaoPayRandomGenerator.createRandomString(1000).length(),1000);

    }

    @Test
    public void testCreateRandomStringFailure(){
        KakaoPayRandomGenerator kakaoPayRandomGenerator = new KakaoPayRandomGenerator();

        assertEquals(kakaoPayRandomGenerator.createRandomString(-1),kakaoPayRandomGenerator.CREATE_FAILURE);
        assertEquals(kakaoPayRandomGenerator.createRandomString(0),kakaoPayRandomGenerator.CREATE_FAILURE);
        assertEquals(kakaoPayRandomGenerator.createRandomString(-123),kakaoPayRandomGenerator.CREATE_FAILURE);

    }
}