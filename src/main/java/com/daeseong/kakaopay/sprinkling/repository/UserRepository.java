package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.Room;
import com.daeseong.kakaopay.sprinkling.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public User findOne(Long id){
        return em.find(User.class, id);
    }

    public void save(User user){
        em.persist(user);
    }

}
