package com.daeseong.kakaopay.sprinkling.contants;

import lombok.Getter;

@Getter
public enum BusinessStatusCode {
    /**
     * 1xxx : user exception
     * 2xxx : room exception
     * 3xxx : sprinklingMoney exception
     */
    BE1001(1001, "대화방에 참여하지 않은 사용자입니다."),
    BE1002(1002, "뿌리기를 한 사람만 조회 가능합니다."),
    BE1003(1003, "뿌리기를 한 사람은 받을 수 없습니다."),
    BE1004(1004, "받기는 한번만 가능합니다."),
    BE1005(1005, "줍기 가능한 인원은 1명 이상입니다."),

    BE2001(2001, "대화방이 존재하지 않습니다."),
    BE2002(2002, "채팅방의 멤버는 적어도 2명 이상 이어야 합니다."),

    BE3001(3001, "뿌리기를 받을 멤버의 수는 채팅방 참여 멤버의 수보다 더 적야 합니다"),
    BE3002(3002, "뿌릴 돈은 멤버 수보다 커야 합니다."),
    BE3003(3003, "뿌리기 정보가 없거나 조회 가능한 기간이 지났습니다."),
    BE3004(3004, "뿌리기 정보가 없거나 받기 가능한 기간이 지났습니다."),
    BE3005(3005, "남은 뿌리기 금액이 없습니다");

    int code;
    String msg;

    BusinessStatusCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
