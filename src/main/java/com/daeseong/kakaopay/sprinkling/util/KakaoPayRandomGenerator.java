package com.daeseong.kakaopay.sprinkling.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KakaoPayRandomGenerator {
    private final String STRING_DICTIONARY = "01234567890abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final String CREATE_FAILURE = "F";
    private final int MINIMUM_VALUE_FOR_DISTRIBUTION = 1;
    private final int MINIMUM_VALUE_FOR_RANDOM_STRING = 1;

    public List<Integer> createMoneyForDistribution(int number, int amount){
        List<Integer> moneyForDistribution = new ArrayList<>();
        Random generator = new java.util.Random(System.currentTimeMillis());
        generator.setSeed(System.currentTimeMillis());
        int remains = amount - number;
        int tempInt = 0;

        for(int i = 0; i < number - 1; i++){
            if (remains > 0) {
                tempInt = generator.nextInt(remains);
                moneyForDistribution.add(tempInt + MINIMUM_VALUE_FOR_DISTRIBUTION);
                remains -= tempInt;
            } else {
                moneyForDistribution.add(MINIMUM_VALUE_FOR_DISTRIBUTION);
            }
        }

        moneyForDistribution.add(remains + MINIMUM_VALUE_FOR_DISTRIBUTION);
        return moneyForDistribution;
    }

    public String createRandomString(int number){
        Random generator = new Random();
        StringBuilder randomString = new StringBuilder();
        int dictionarySize = STRING_DICTIONARY.length();
        int randomIndex;

        if (number < MINIMUM_VALUE_FOR_RANDOM_STRING){
            return CREATE_FAILURE;
        }

        for (int i=0; i < number; i++){
            randomIndex = generator.nextInt(dictionarySize);
            randomString.append(STRING_DICTIONARY.charAt(randomIndex)) ;
        }
        return randomString.toString();
    }
}
