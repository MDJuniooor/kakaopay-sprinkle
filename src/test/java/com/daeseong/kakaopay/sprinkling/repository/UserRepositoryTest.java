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
    public void findUser(){
        User user = new User();
        userRepository.save(user);
        assertEquals(user, userRepository.findOne(user.getId()));
    }
}