package com.daeseong.kakaopay.sprinkling.repository;

import com.daeseong.kakaopay.sprinkling.entity.AmountStatus;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoneyDistributionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SprinklingMoneyDistributionInfoRepository {

    private final EntityManager em;

    public void save(SprinklingMoneyDistributionInfo sprinklingMoneyDistributionInfo){
        if (sprinklingMoneyDistributionInfo.getId() == null) {
            em.persist(sprinklingMoneyDistributionInfo); // 신규 등록
        } else {
            em.merge(sprinklingMoneyDistributionInfo); // update
        }
    }

    public SprinklingMoneyDistributionInfo findOne(Long id){
        return em.find(SprinklingMoneyDistributionInfo.class, id);
    }

    public List<SprinklingMoneyDistributionInfo> findBySprinklingMoneyDistributionIdAndValidPickingUp(Long sprinklingMoneyId){
        return em.createQuery(
                "select smdi from SprinklingMoneyDistributionInfo smdi " +
                        "where smdi.sprinklingMoney.id = :sprinklingMoneyId " +
                        "and smdi.amountStatus = :available", SprinklingMoneyDistributionInfo.class)
                .setParameter("sprinklingMoneyId", sprinklingMoneyId)
                .setParameter("available", AmountStatus.AVAILABLE)
                .getResultList();
    }


}
