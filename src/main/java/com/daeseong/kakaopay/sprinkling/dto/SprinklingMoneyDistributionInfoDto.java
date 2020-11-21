package com.daeseong.kakaopay.sprinkling.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SprinklingMoneyDistributionInfoDto {

    Long userId;
    int amount;

    public SprinklingMoneyDistributionInfoDto(Long userId, int amount){
        this.userId = userId;
        this.amount = amount;
    }
}
