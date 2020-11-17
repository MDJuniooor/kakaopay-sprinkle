package com.daeseong.kakaopay.sprinkling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class MultipleResponse<T> extends BaseResponse {
    private List<T> data;

    public MultipleResponse(List<T> data, int code, String msg) {
        super(code, msg);
        this.data = data;
    }
}
