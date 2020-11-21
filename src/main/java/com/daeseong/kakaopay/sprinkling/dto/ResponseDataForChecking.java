package com.daeseong.kakaopay.sprinkling.dto;

import com.daeseong.kakaopay.sprinkling.entity.AmountStatus;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoneyDistributionInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ResponseDataForChecking {
    private String createdAt;
    private int amount;
    private int completedAmount;
    private List<SprinklingMoneyDistributionInfoDto> sprinklingMoneyDistributionInfoDtos;

    public ResponseDataForChecking(SprinklingMoney sprinklingMoney){
        this.amount = sprinklingMoney.getAmount();
        this.createdAt = sprinklingMoney.getCreatedAt().toString();
        this.completedAmount = 0;
        this.sprinklingMoneyDistributionInfoDtos = new ArrayList<>();

        for( SprinklingMoneyDistributionInfo distributionInfo: sprinklingMoney.getSprinkingMoneyDistributionInfos()){
            if (distributionInfo.getAmountStatus() == AmountStatus.COMPLETED){
                this.completedAmount += distributionInfo.getAmount();
                this.sprinklingMoneyDistributionInfoDtos.add(
                        new SprinklingMoneyDistributionInfoDto(distributionInfo.getUser().getId(), distributionInfo.getAmount())
                );
            }
        }
    }
}
