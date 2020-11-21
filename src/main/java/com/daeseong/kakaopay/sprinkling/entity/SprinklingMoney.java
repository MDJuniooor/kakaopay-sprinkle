package com.daeseong.kakaopay.sprinkling.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.daeseong.kakaopay.sprinkling.entity.AmountStatus.AVAILABLE;
import static com.daeseong.kakaopay.sprinkling.contants.SprinklingMoneyConstant.VALID_DAYS_FOR_READ;
import static com.daeseong.kakaopay.sprinkling.contants.SprinklingMoneyConstant.VALID_MINUTES_FOR_PICKING_UP_MONEY;
import static com.daeseong.kakaopay.sprinkling.entity.AmountStatus.COMPLETED;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SprinklingMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private int amount;

    private LocalDateTime createdAt;
    private LocalDateTime validTimeForPickingUpMoney;
    private LocalDateTime validTimeForRead;

    @Enumerated(EnumType.STRING)
    private AmountStatus amountStatus;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "sprinklingMoney", cascade = CascadeType.ALL)
    private List<SprinklingMoneyDistributionInfo> sprinkingMoneyDistributionInfos = new ArrayList<>();

    /**
     * 연관관계 메서드
     */
    public void addSprinkingMoneyDistributionInfos(SprinklingMoneyDistributionInfo sprinklingMoneyDistributionInfo){
        sprinkingMoneyDistributionInfos.add(sprinklingMoneyDistributionInfo);
        sprinklingMoneyDistributionInfo.setSprinklingMoney(this);
    }


    public static SprinklingMoney createSprinklingMoney(User creator, Room room, String token, int amount
            , List<SprinklingMoneyDistributionInfo> sprinklingMoneyDistributionInfos) {
        SprinklingMoney sprinklingMoney = new SprinklingMoney();
        LocalDateTime createdAt = LocalDateTime.now();
        sprinklingMoney.setCreator(creator);
        sprinklingMoney.setRoom(room);
        sprinklingMoney.setToken(token);
        sprinklingMoney.setAmount(amount);
        for (SprinklingMoneyDistributionInfo sprinklingMoneyDistributionInfo: sprinklingMoneyDistributionInfos){
            sprinklingMoney.addSprinkingMoneyDistributionInfos(sprinklingMoneyDistributionInfo);
        }
        sprinklingMoney.setCreatedAt(createdAt);
        sprinklingMoney.setAmountStatus(AVAILABLE);
        sprinklingMoney.setValidTimeForPickingUpMoney(createdAt.plusMinutes(VALID_MINUTES_FOR_PICKING_UP_MONEY));
        sprinklingMoney.setValidTimeForRead(createdAt.plusDays(VALID_DAYS_FOR_READ));

        return sprinklingMoney;
    }

    public void completePikingUp(){
        this.amountStatus = COMPLETED;
    }

}
