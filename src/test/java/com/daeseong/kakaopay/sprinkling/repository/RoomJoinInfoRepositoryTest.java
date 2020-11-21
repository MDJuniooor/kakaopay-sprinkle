package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.Room;
import com.daeseong.kakaopay.sprinkling.entity.RoomJoinInfo;
import com.daeseong.kakaopay.sprinkling.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoomJoinInfoRepositoryTest {

    @Autowired
    RoomJoinInfoRepository roomJoinInfoRepository;

    @Test
    public void shouldFindRoomJoinInfoByRoomId(){
        List<RoomJoinInfo> roomJoinInfo = roomJoinInfoRepository.findByRoomId("1-room");

        assertNotEquals(roomJoinInfo.size(), 0);
    }

    @Test
    public void shouldFindRoomJoinInfoByRoomIdAndUserId(){
        List<RoomJoinInfo> roomJoinInfo = roomJoinInfoRepository.findByRoomIdAndUserId("1-room",1L);

        assertEquals(roomJoinInfo.size(), 1);
    }

    @Test
    public void shouldCreateRoomJoinInfo(){

        User user = new User();
        Room room = new Room();

        RoomJoinInfo roomJoinInfo = RoomJoinInfo.joinRoom(user, room);
        roomJoinInfoRepository.save(roomJoinInfo);

        assertNotNull(roomJoinInfo.getId());
    }
}