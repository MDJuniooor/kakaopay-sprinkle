package com.daeseong.kakaopay.sprinkling.entity.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("token")
@Getter @Setter
public class TokenCache implements Serializable {
    private String token;
    private String roomId;
}
