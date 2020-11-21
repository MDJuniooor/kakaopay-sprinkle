package com.daeseong.kakaopay.sprinkling;

import com.daeseong.kakaopay.sprinkling.entity.Room;
import com.daeseong.kakaopay.sprinkling.entity.RoomJoinInfo;
import com.daeseong.kakaopay.sprinkling.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDatabase {
    private final InitService initService;

    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit() {
            List<User> users = createUsers();
            List<Room> rooms = createRooms();
            createRoomJoinInfos(users, rooms);
        }

        private List<User> createUsers(){
            List<User> users = new ArrayList<>();
            User user;
            for(int i=1; i < 11; i++){
                user = new User();
                user.setId((long) i);
                em.persist(user);
                users.add(user);
            }
            return users;
        }

        private List<Room> createRooms(){
            List<Room> rooms = new ArrayList<>();
            Room room;

            for(int i=1; i<4; i++){
                room = new Room();
                room.setId(String.format("%d-room", i));
                em.persist(room);
                rooms.add(room);
            }
            return rooms;
        }

        private void createRoomJoinInfos(List<User> users, List<Room> rooms){
            int roomQuantity;
            RoomJoinInfo roomJoinInfo;
            for(Room room: rooms){
                roomQuantity = Integer.parseInt(room.getId().substring(0,1));
                for(int i = 0;  i < roomQuantity ; i++){
                    roomJoinInfo = new RoomJoinInfo();
                    roomJoinInfo.setUser(users.get(i));
                    roomJoinInfo.setRoom(room);
                    roomJoinInfo.setJoinedDate(LocalDateTime.now());
                    em.persist(roomJoinInfo);
                }

            }
        }
    }
}
