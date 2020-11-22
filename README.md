# 카카오페이 사전과제 [뿌리기 기능 구현하기]

## 개발환경
- Java 1.8
- Spring Boot 2.1.16.RELEASE
- Jpa
- H2
- Gradle

## 빌드 및 실행환경
0. JAR 실행 파일 만들고 실행하기
```
> gradlew bootjar

// build/libs 로 이동한다.
> java -jar sprinkling-0.0.1-SNAPSHOT.jar
```
1. localhost:8080 으로 접속한다.
2. localhost:8080/swagger-ui.html 에서 API 문서 확인 가능하다.
3. redis 사용을 위해 redis docker를 사용하였다. 사용 포트: 6378
```
> docker run -p 6379:6379 --name redis_boot -d redis


// redis 정상 작동 확인을 위한 명령어
> docker exec -i -t redis_boot redis-cli

127.0.0.1:6379 >
```

## 문제 해결 전략  

### 1. 고유 토큰 생성 관련  
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

### 2. 머니 줍기 관련
1. 아직 상태가 완료되지 않은 돈들을 읽어서, 맨 처음 데이터를 갖게 하는 전략으로 구성하였다.
2. 동시에 주울 경우, 업데이트 시 같은 돈을 가져 가서 의도하지 않게 로직이 흘러갈 수 있다.
3. 해당 쿼리에 Transaction Lock type을 PESSIMISTIC_WRITE으로 적용하여 read lock을 걸어 동시성을 제어하였다.  

## API 명세
request header에 X-ROOM-ID와 X-USER-ID는 필수값이다.  
공통 Error 리턴
```
{
    "code" : 1001
    "msg" : "대화방에 참여하지 않은 사용자입니다."
}
```
* 하단에 비즈니스 상태 코드 참조  
[상태 코드 정의](#상태-코드-정의)

### 1. 머니 뿌리기  

<img width="982" alt="스크린샷 2020-11-22 오전 1 58 26" src="https://user-images.githubusercontent.com/33591838/99882758-37d62700-2c66-11eb-8baa-1687cfd4318a.png">

POST /v1/sprinkle  
Request
```
{
    "amount" : 10000,
    "numberForPickingUp : 3
}
```

Response
```
{
  "code": 200,
  "msg": "성공",
  "data": {
    "token": "5wn"
  }
}
```

### 2. 머니 뿌리기 상태 조회

<img width="982" alt="스크린샷 2020-11-22 오전 2 01 30" src="https://user-images.githubusercontent.com/33591838/99882810-808de000-2c66-11eb-9313-6b380b0630ff.png">


GET /v1/sprinkle/{token}  

Response
```
{
  "code": 200,
  "msg": "성공",
  "data": {
    "createdAt": "2020-11-22T20:34:55.875",
    "amount": 10000,
    "completedAmount": 7965,
    "distributionInfo": [
      {
        "userId": 2,
        "amount": 3600
      },
      {
        "userId": 3,
        "amount": 4365
      }
    ]
  }
}
```

### 3.머니 줍기

<img width="982" alt="스크린샷 2020-11-22 오전 2 01 30" src="https://user-images.githubusercontent.com/33591838/99882840-a61ae980-2c66-11eb-9000-d170536e1f70.png">
  
PUT /v1/pickup/{token}  

Response
```
{
  "code": 200,
  "msg": "성공",
  "data": {
    "amount": 400
  }
}
```

## 상태 코드 정의
- 비즈니스 상태에 따라 코드를 정의하였다.
- BE1XXX : 사용자 관련 코드
- BE2XXX : 대화방 관련 코드
- BE3XXX : 뿌리기 관련 코드
### 상태 코드 상세

#### 1. 사용자 관련 코드
1.1 BE1001 : 코드 - 1001, 상세 - 대화방에 참여하지 않은 사용자입니다.  
1.2 BE1002 : 코드 - 1002, 상세 - 뿌리기를 한 사람만 조회 가능합니다.  
1.3 BE1003 : 코드 - 1003, 상세 - 뿌리기를 한 사람은 받을 수 없습니다.  
1.4 BE1004 : 코드 - 1004, 상세 - 받기는 한번만 가능합니다.  
1.5 BE1005 : 코드 - 1005, 상세 - 줍기 가능한 인원은 1명 이상입니다.  

#### 2. 대화방 관련 코드 
2.1 BE2001 : 코드 - 2001, 상세 - 대화방이 존재하지 않습니다.  
2.2 BE2002 : 코드 - 2002, 상세 - 채팅방의 멤버는 적어도 2명 이상 이어야 합니다.  


#### 3. 뿌리기 관련 코드
3.1 BE3001 : 코드 - 3001, 상세 - 뿌리기를 받을 멤버의 수는 채팅방 참여 멤버의 수보다 더 적야 합니다.  
3.2 BE3002 : 코드 - 3002, 상세 - 뿌릴 돈은 멤버 수보다 커야 합니다.  
3.3 BE3003 : 코드 - 3003, 상세 - 뿌리기 정보가 없거나 조회 가능한 기간이 지났습니다.  
3.4 BE3004 : 코드 - 3004, 상세 - 뿌리기 정보가 없거나 받기 가능한 기간이 지났습니다.  
3.5 BE3005 : 코드 - 3005, 상세 - 남은 뿌리기 금액이 없습니다.  
3.6 BE3006 : 코드 - 3006, 상세 - 뿌릴 돈은 0원 보다 커야 합니다.  

## 데이터베이스 스키마

![image](https://user-images.githubusercontent.com/33591838/99895933-178e8280-2ccf-11eb-8099-8b06c6c53190.png)


## 테스트를 위한 초기 데이터 셋팅
### 초기 데이터 SQL 스크립트는 src/main/resources/data-h2.sql 에 있다.
* user는 id가 1 ~ 10 인 유저가 존재한다.
* room은 id가 1-room, 2-room, 3-room, 4-room 인 4개의 대화방이 존재한다.
* id가 1인 유저는 1-room, 2-room, 3-room, 4-room 4개의 대화방에 참여하고 있다.
* id가 2인 유저는 2-room, 3-room, 4-room 3개의 대화방에 참여하고 있다.
* id가 3인 유저는 3-room, 4-room 2개의 대화방에 참여하고 있다.
* id가 4인 유저는 4-room 1개의 대화방에 참여하고 있다.
