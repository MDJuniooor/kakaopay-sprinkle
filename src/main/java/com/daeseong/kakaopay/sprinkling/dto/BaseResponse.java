package com.daeseong.kakaopay.sprinkling.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    @ApiModelProperty(value = "응답 코드")
    private int code;

    @ApiModelProperty(value = "응답 메세지")
    private String msg;

}
