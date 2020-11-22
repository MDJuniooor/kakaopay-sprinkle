package com.daeseong.kakaopay.sprinkling.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TokenCacheRepositoryTest {

    @Autowired TokenCacheRepository tokenCacheRepository;
    @Autowired StringRedisTemplate stringRedisTemplate;

    @Before
    public void setting(){
        stringRedisTemplate.delete("token-roomId");
    }

    @Test
    public void shouldCreateToken(){
        String token = "token";
        String roomId = "roomId";
        boolean existsToken = tokenCacheRepository.existsToken(token, roomId);
        assertFalse(existsToken);
        tokenCacheRepository.setToken(token, roomId);

        existsToken = tokenCacheRepository.existsToken(token, roomId);
        assertTrue(existsToken);
    }
}