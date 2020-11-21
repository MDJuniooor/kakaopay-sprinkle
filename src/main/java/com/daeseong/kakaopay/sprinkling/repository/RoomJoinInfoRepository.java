package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.RoomJoinInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomJoinInfoRepository {

    private final EntityManager em;

    public void save(RoomJoinInfo roomJoinInfo){
        em.persist(roomJoinInfo);
    }

    public List<RoomJoinInfo> findByRoomId(String roomId){
        return em.createQuery(
                "select rj from RoomJoinInfo  rj where rj.room.id = :roomId", RoomJoinInfo.class)
                .setParameter("roomId",roomId)
                .getResultList();
    }

    public List<RoomJoinInfo> findByRoomIdAndUserId(String roomId, Long userId){
        return em.createQuery(
                "select rj from RoomJoinInfo  rj " +
                        "where rj.room.id = :roomId " +
                        "and rj.user.id = :userId", RoomJoinInfo.class)
                .setParameter("roomId", roomId)
                .setParameter("userId", userId)
                .getResultList();
    }
}
