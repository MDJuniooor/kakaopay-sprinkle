package com.daeseong.kakaopay.sprinkling.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseDataForPickingUp {
    int amount;

    public ResponseDataForPickingUp(int amount) {
        this.amount = amount;
    }
}
