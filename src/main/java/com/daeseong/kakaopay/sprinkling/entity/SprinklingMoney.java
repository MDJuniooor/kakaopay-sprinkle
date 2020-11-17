package com.daeseong.kakaopay.sprinkling.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class SprinklingMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private int amount;
    private boolean amountStatus;
    private Date createdAt;
    private int validMinutesForGettingMoney;
    private int validDaysForRead;


    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "sprinklingMoney", cascade = CascadeType.ALL)
    private List<SprinklingMoneyDistributionInfo> sprinkingMoneyDistributionInfos = new ArrayList<>();

}
