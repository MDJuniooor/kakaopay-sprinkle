package com.daeseong.kakaopay.sprinkling.entity;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class SprinklingMoneyDistributionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="sprinkling_money_id")
    private SprinklingMoney sprinklingMoney;
}
