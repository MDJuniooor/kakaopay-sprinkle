package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.User;
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
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldFindUser(){
        User user = userRepository.findOne(1L);

        assertEquals((long)user.getId(), 1L);
    }

    @Test
    public void shouldCreateUser(){
        User user = new User();
        userRepository.save(user);

        assertNotNull(user.getId());
    }
}