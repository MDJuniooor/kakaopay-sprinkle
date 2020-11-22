package com.daeseong.kakaopay.sprinkling.dto;

import lombok.Data;

@Data
public class SprinkleMoneyRequest {
    private int numberForPickingUp;
    private int amount;

    public SprinkleMoneyRequest(int numberForPickingUp, int amount){
        this.numberForPickingUp = numberForPickingUp;
        this.amount = amount;
    }
}