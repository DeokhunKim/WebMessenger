# WebMessenger

## 개발 키워드
Language: `Java` `JavaScript` `MySql` `MongoDB`

Framework / Library: `Spring Boot` `Spring Data` `WebSocket` `JMeter`

<br/>

## 개요
이름: Web Messenger | 분류: 토이 프로젝트(개인) | 제작기간: 2022.03 약 2주 | 주요기능: 웹사이트 회원들간 채팅, 봇(bot)과의 채팅 

<br/>

## 진행 배경과 목적
웹 개발을 공부하며 앞으로 개발하게 될 토이 프로젝트를 모아두는 나만의 플랫폼을 만들고 싶었습니다. 그리고 그 플랫폼의 최초 기능으로 서버와 클라이언트간 실시간 통신이 어떤 식으로 이루어지는지 배워보고자 채팅 기능이 있는 메신저를 제작하게 되었습니다.

첫번째 목적, 공부했던 내용을 기반으로 직접 설계와 구현을 해봄으로 코딩 역량 향상. (배운 것을 써먹어 보자!)

두번째 목적, 실제 서비스되는 DB는 어떻게 사용 되어지고 특징이 있는지. (실무에서는 어떤 식으로 사용될까?)

이렇게 2가지 목적을 가지고 시작하게 되었습니다.

<br/>

## 주요 기능
*	회원 관리
*	회원가입 / 로그인
*	채팅 기능
*	유저들 간 실시간 채팅 기능 (유저-유저)
*	자동 응답 봇과의 채팅 기능 (유저-봇)
*	성능 테스트
*	초당 채팅 처리 속도 측정


<br/>


## 미리보기

 
 <img width="329" alt="image" src="https://user-images.githubusercontent.com/86358502/186424257-234f86c2-455c-45c1-b20d-19278b8a454c.png">

 <img width="452" alt="image" src="https://user-images.githubusercontent.com/86358502/186424223-b19713c6-c51a-4840-8227-8722ba93aaab.png">




 



<br/>

## 연구 과제 - DB 종류에 따른 Performance 차이

### 출발
*	메신저의 채팅 로그는 어떻게 저장하고 관리해야 효율적일까? 실제 서비스 되고 있는 다양한 메신저들은 어떤 방식을 채택했을까? 라는 의문을 가지고 찾아 보았습니다.
*	많은 메신저들이 NoSQL을 사용하고 있었고, 실제 RDB에서 NoSQL로 변경 후 많은 성능 향상이 있었다는 인터뷰도 확인이 되었습니다.
*	내가 만든 메신저에서도 DB 변화에 따른 성능 변화가 확인 될 수 있을지, 어떠한 특징과 이유 때문에 많은 기업들이 NoSQL을 채택 하였는지에 대해 직접 확인하고 싶었습니다.

### 계획
*	테스트 방법
    * Stress Test Tool을 이용하여 메세지 송신/수신을 반복적으로 발생 시켰을 때, 저장 방법에 따라 수치상 어떤 차이가 있는지 확인
*	저장 방법 비교군
    * 메모리
    * H2 (RDB)
    * MongoDB (NoSQL)
*	Stress Test Tool
    * JMeter



### 결과
*	‘메신저 이용 패턴’을 1000번 반복 했을 때의 평균 처리 속도 측정 (여러 번 수행 후 평균)
*	메신저 이용 패턴: 유저가 세션에 접속하여 채팅봇과 60개의 채팅을 주고 받은 뒤 세션을 종료함
*	단위: 초당 request 처리 개수


|    | 메모리	 |   H2	          |PostgreSQL	    | MongoDB |
|----|-------|-------------|---------|---------|
| 초당 처리량 |	11,000 /sec	| 860 /sec	| 1,080 /sec	| 1,500 /sec |




### 결론

우선은
*	직접 구현한 메신저에서 RDB보다 NoSQL을 사용했을 때 의미 있는 수치의 빠른 성능을 보임
*	데이터의 수가 많을 때 NoSQL이 높은 성능을 보이는 특징 때문에 이런 결과가 나온 것으로 판단됨
*	따라서 수 많은 채팅 로그가 쌓일 메신저에서는 NoSQL을 선택하는 것이 성능 면에서 유리함

하지만
*	DB설계를 통한 최적화에 대한 고민을 깊게 하지 않았으므로 각 DB별 성능 개선 여지가 있음
*	NoSQL이 가진 확장성(scale-out)이나 스키마에 대한 유연성 등의 장점을 살리지는 못하였음


<br/>


## 개발 스터디
*	MongoDB의 Collection 내부의 List 삽입 속도
*	https://deokhunkim.github.io/mongodb/2022/03/17/MongoDB의-Collection-내부의-List-삽입-속도.html
*	JPA에서 pageable과 limit의 쿼리 차이
*	https://deokhunkim.github.io/spring/2022/03/19/JPA에서-pageable과-limit의-쿼리-차이.html
*	JMeter를 이용한 WebSocket 테스트 방법
*	https://deokhunkim.github.io/jmeter/2022/03/20/JMeter를-이용한-WebSocket-테스트.html
*	메신저 웹소켓 처리
*	https://deokhunkim.github.io/spring/2022/03/20/메신저-웹소켓-처리.html

