package com.daeseong.kakaopay.sprinkling.contants;

import lombok.Getter;

@Getter
public enum HttpStatusCode {

    HTTP_200_OK(200, "성공"),
    HTTP_201_CREATED(201, "생성"),
    HTTP_401_UNAUTHORIZED(401, "권한없음"),
    HTTP_403_FORBIDDEN(403, ""),
    HTTP_404_NOT_FOUND(404, "없음"),
    HTTP_405_METHOD_NOT_ALLOWED(405, "사용하지 못함"),
    HTTP_500_INTERNAL_SERVER_ERROR(500, "내부 서버 에러");

    int code;
    String msg;

    HttpStatusCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
