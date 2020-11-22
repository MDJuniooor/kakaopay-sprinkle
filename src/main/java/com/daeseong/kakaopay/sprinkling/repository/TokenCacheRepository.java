package com.daeseong.kakaopay.sprinkling.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.concurrent.TimeUnit;

import static com.daeseong.kakaopay.sprinkling.contants.SprinklingMoneyConstant.VALID_MINUTES_FOR_PICKING_UP_MONEY;

@Repository
public class TokenCacheRepository{

    private static final String TOKEN_KEY = "token-roomId";
    private static final String TOKEN_PART = "token";
    private static final String ROOM_ID_PART = "roomId";

    private final StringRedisTemplate stringRedisTemplate;

    public TokenCacheRepository(
            @Qualifier("stringRedisTemplate") StringRedisTemplate stringRedisTemplate
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean existsToken(String token, String roomId) {
        String key = TOKEN_KEY.replace(TOKEN_PART, token).replace(ROOM_ID_PART, roomId);

        Boolean hasKey = stringRedisTemplate.hasKey(key);

        return hasKey;
    }

    public void setToken(String token, String roomId) {
        String key = TOKEN_KEY.replace(TOKEN_PART, token).replace(ROOM_ID_PART, roomId);
        stringRedisTemplate.opsForValue().set(key, "", VALID_MINUTES_FOR_PICKING_UP_MONEY, TimeUnit.MINUTES);
    }



}