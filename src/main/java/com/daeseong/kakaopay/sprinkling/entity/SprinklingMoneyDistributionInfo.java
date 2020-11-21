package com.daeseong.kakaopay.sprinkling.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.daeseong.kakaopay.sprinkling.entity.AmountStatus.AVAILABLE;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class SprinklingMoneyDistributionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    @Enumerated(EnumType.STRING)
    private AmountStatus amountStatus;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="sprinkling_money_id")
    private SprinklingMoney sprinklingMoney;

    public static SprinklingMoneyDistributionInfo createSprinklingMoneyDistributionInfo(int amount){
        SprinklingMoneyDistributionInfo sprinklingMoneyDistributionInfo = new SprinklingMoneyDistributionInfo();
        sprinklingMoneyDistributionInfo.setAmount(amount);
        sprinklingMoneyDistributionInfo.setAmountStatus(AVAILABLE);
        return sprinklingMoneyDistributionInfo;
    }

    public void pickUp(User user){
        this.user = user;
        this.amountStatus = AmountStatus.COMPLETED;
    }
}
