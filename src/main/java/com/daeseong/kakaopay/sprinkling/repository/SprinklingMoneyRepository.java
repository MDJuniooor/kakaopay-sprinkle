package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoneyDistributionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SprinklingMoneyRepository {

    private final EntityManager em;

    public void save(SprinklingMoney sprinklingMoney){
        if (sprinklingMoney.getId() == null) {
            em.persist(sprinklingMoney); // 신규 등록
        } else {
            em.merge(sprinklingMoney); // update
        }
    }

    public SprinklingMoney findOne(Long id){
        return em.find(SprinklingMoney.class, id);
    }

    public List<SprinklingMoney> findByTokenAndRoomIdForPickingUpMoney(String token, String roomId) {
        return em.createQuery("select s " +
                "from SprinklingMoney s " +
                "where s.token = :token " +
                "and s.room.id = :roomId " +
                "and :now <= s.validTimeForPickingUpMoney", SprinklingMoney.class)
                .setParameter("token", token)
                .setParameter("roomId", roomId)
                .setParameter("now", LocalDateTime.now())
        .getResultList();
    }

    public List<SprinklingMoney> findByTokenAndRoomIdForRead(String token, String roomId) {
        return em.createQuery("select distinct s " +
                "from SprinklingMoney s " +
                "join fetch s.sprinkingMoneyDistributionInfos smdi " +
                "where s.token = :token " +
                "and s.room.id = :roomId " +
                "and :now <= s.validTimeForRead", SprinklingMoney.class)
                .setParameter("token", token)
                .setParameter("roomId", roomId)
                .setParameter("now", LocalDateTime.now())
                .getResultList();
    }

}
