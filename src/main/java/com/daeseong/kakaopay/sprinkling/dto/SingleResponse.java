package com.daeseong.kakaopay.sprinkling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponse<T> extends BaseResponse {
    private T data;

    public SingleResponse(T data, int code, String msg) {
        super(code, msg);
        this.data = data;
    }
}
