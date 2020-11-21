package com.daeseong.kakaopay.sprinkling.service;

import com.daeseong.kakaopay.sprinkling.repository.RoomRepository;
import com.daeseong.kakaopay.sprinkling.repository.SprinklingMoneyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SprinklingMoneyServiceTest {

    @Autowired
    SprinklingMoneyService sprinklingMoneyService;
    @Autowired RoomRepository roomRepository;
    @Autowired SprinklingMoneyRepository sprinklingMoneyRepository;


}