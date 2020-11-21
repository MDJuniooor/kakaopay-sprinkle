package com.daeseong.kakaopay.sprinkling.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseDataForSprinkling {
    private String token;

    public ResponseDataForSprinkling() {
        this.token = "TOKEN_CREATE_FAILURE";
    }

    public ResponseDataForSprinkling(String token){
        this.token = token;
    }
}
