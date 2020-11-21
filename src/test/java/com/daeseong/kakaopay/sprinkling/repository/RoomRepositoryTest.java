package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.Room;
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
public class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @Test
    public void findRoom(){
        Room room = new Room();
        roomRepository.save(room);
        System.out.println(room.getId());
        assertEquals(room, roomRepository.findOne(room.getId()));
    }


}