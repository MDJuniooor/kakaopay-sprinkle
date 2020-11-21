# 카카오페이 사전과제 [뿌리기 기능 구현하기]

## 개발환경
- Java 1.8
- Spring Boot 2.1.16.RELEASE
- Jpa
- H2
- Gradle

## 실행환경
1.  localhost:8080 으로 접속한다.
2. localhost:8080/swagger-ui.html 에서 API 문서 확인 가능하다.
3. redis 사용을 위해 redis docker를 사용하였다.
```
docker run -p 6379:6379 -d redis


// redis 정상 작동 확인을 위한 명령어
docker exec -i -t redis_boot redis-cli
127.0.0.1:6379 >
```

## 문제 해결 전략
1. token과 roomId를 조합하여 고유 token 키로 정의하였다.
2. 다수의 서버에 다수의 인스턴스로 동작 시, 토큰 고유값 유지를 위해 Global Cache Server로 
Redis를 이용하였다.
```
public String createToken(String roomId){
    KakaoPayRandomGenerator kakaoPayRandomGenerator = new KakaoPayRandomGenerator();
    String token = kakaoPayRandomGenerator.createRandomString(TOKEN_LENGTH);
    
    // cache hit 하지 않을 때까지 루프
    while(tokenCacheRepository.existsToken(token, roomId)){
        token = kakaoPayRandomGenerator.createRandomString(TOKEN_LENGTH);
    };
    // cache에 등록
    tokenCacheRepository.setToken(token, roomId);
    
    return token;
}
```

* cache의 유효 시간을 뿌리기가 유효한 시간과 동일하게 두어, 유효한 식별값만 유지시켰다.
```
@Repository
public class TokenCacheRepository{

    // 중략...

    public void setToken(String token, String roomId) {
        String key = TOKEN_KEY.replace("token",token).replace("roomId",roomId);
        // Cache Expire Time과 유효기간과 일치시킴
        stringRedisTemplate.opsForValue().set(key, "", VALID_MINUTES_FOR_PICKING_UP_MONEY, TimeUnit.MINUTES);
    }

}
```
## API 명세

<img width="982" alt="스크린샷 2020-11-22 오전 1 58 26" src="https://user-images.githubusercontent.com/33591838/99882758-37d62700-2c66-11eb-8baa-1687cfd4318a.png">

![image](https://user-images.githubusercontent.com/33591838/99882810-808de000-2c66-11eb-9313-6b380b0630ff.png)

<img width="982" alt="스크린샷 2020-11-22 오전 2 01 30" src="https://user-images.githubusercontent.com/33591838/99882840-a61ae980-2c66-11eb-9000-d170536e1f70.png">
