package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final EntityManager em;

    public void save(Room room){
        em.persist(room);
    }

    public Room findOne(String id){
        return em.find(Room.class, id);
    }
}
