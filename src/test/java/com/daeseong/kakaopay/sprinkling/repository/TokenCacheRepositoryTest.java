package com.daeseong.kakaopay.sprinkling.repository;

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
public class TokenCacheRepositoryTest {

    @Autowired TokenCacheRepository tokenCacheRepository;

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