# D-velkit
![Github_리드미_백엔드](https://user-images.githubusercontent.com/70882917/193630557-54c72f8c-c22c-4518-878a-aa29a25949a0.png)


## 소개프로젝트 소개
파일공유, 일정관리, 채팅 등 다양한 기능을 한번에 사용할 수 있는 협업툴
프로젝트 생성 후 초대코드를 팀원들에게 전달하면 팀공간 WorkSpace에 참여가능

[체험해보기!!](https://d-velkit.com/)

## 📅 프로젝트 기간
2022/08/26 ~ 2022/10/07

배포일자 2022/09/27

## 🚀👩‍🚀👨‍🚀 팀원
|이름|깃헙주소|담당|
|:---:|:---:|:---:|
|FE|[https://github.com/develkitProject/frontend](https://github.com/develkitProject/frontend)|FE/React|
|한호성L|[https://github.com/hosunghan-0821](https://github.com/hosunghan-0821)|BE/Spring|
|황인권|[https://github.com/ingwon97](https://github.com/ingwon97)|BE/Spring|
|이재헌|[https://github.com/romeo92s](https://github.com/romeo92s)|BE/Spring|
|임준철|[https://github.com/cheoljun0422](https://github.com/cheoljun0422)|BE/Spring|
|류현VL|[https://github.com/LuisKlopp](https://github.com/LuisKlopp)|FE/React|
|이지혜|[https://github.com/G-Hae](https://github.com/G-Hae)|FE/React|
|김소정|[https://github.com/kimsojeong01](https://github.com/kimsojeong01)|Designer|


## 🎬 시연 및 소개영상
[시연 및 소개영상](https://www.youtube.com/watch?v=KBnkppEY78I)


## 📚 기술스택
Language&build tool
<br>
<img src="https://img.shields.io/badge/Java-536DFE?style=flat-square&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white"/>
<br>
Framework
<br>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/>
<br>
Library
<br>
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white"/>
<img src="https://img.shields.io/badge/Sock.js-000000?style=flat-square&logo=Sock.js&logoColor=white"/>
<img src="https://img.shields.io/badge/Stomp-000000?style=flat-square&logo=Stomp&logoColor=white"/>
<br>
Database
<br>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/>
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/>
<br>
Deploy
<br>
<img src="https://img.shields.io/badge/NGINX-009639?style=flat-square&logo=NGINX&logoColor=white"/> <img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=flat-square&logo=AmazonEC2&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=flat-square&logo=GitHub Actions&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=Amazon S3&logoColor=white"/>



## 🔧 사용 툴
<img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/> <img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white"/>
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat-square&logo=IntelliJ IDEA&logoColor=white"/>
<img src="https://img.shields.io/badge/Sourcetree-0052CC?style=flat-square&logo=Sourcetree&logoColor=white"/>
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/>
<img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/>
<img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white"/>
<img src="https://img.shields.io/badge/Figma-F24E1E?style=flat-square&logo=Figma&logoColor=white"/>


## 🧱 ERD
<img width="960" alt="erd 최종" src="https://user-images.githubusercontent.com/70882917/193749206-4beff896-43a8-40ff-b38a-b7bea6b0713d.png">


## ⇆ 서비스 아키텍쳐
<img width="895" alt="디벨킷아키텍처" src="https://user-images.githubusercontent.com/70882917/193648270-6f9f85d4-dc2c-41be-ad19-386deafd5012.png">


## 📜 API
[🗒 명세서 ](https://www.notion.so/379722be5a5f4ff4a4a61a0b5e72d244?v=7ebf5e46885447629e6cd31fab990a9d)


## ⚙ 주요기능
<details>
<summary>게스트 로그인</summary>
<div markdown="1">

**(1) 목적:** 회원가입/로그인 절차를 부담스럽게 생각하는 사용자를 고려하여, 우선적으로 사용자가 서비스를 체험해보도록 게스트로그인 기능을 추가하였습니다.

**(2) 특징:** 메인화면에서 “임시계정으로 체험하기” 버튼을 클릭하면 자동으로 아이디가 생성되어 회원가입 및 로그인이 완료됩니다.

</div>
</details>

<details>
<summary>문서</summary>
<div markdown="1">

**(1) 목적:** 협업툴 사이트라는 목적에 맞게 문서, 이미지, 파일을 쉽게 공유하고 수정하도록 하였습니다.

**(2) 특징**

1. 자유로운 파일공유: 전체 30MB내에서 파일 공유를 가능하도록 하였습니다.
2. 자유로운 수정기능: 다른 팀원이 올린 글에 내용만 살짝 수정하거나 혹은 파일을 새로 추가하는 경우를 고려하여, 삭제는 작성자만 가능하지만 수정은 프로젝트 내 모든 회원이 가능합니다. 단 최정 수정자가 게시글 하단에 기재됩니다.
3. 문서 읽은 사람 명단을 표시할 수 있도록 구현 하였습니다.

</div>
</details>

<details>
<summary>실시간 채팅</summary>
<div markdown="1">

**(1) 목적:** 프로젝트 협업을 위해서 각 프로젝트마다 채팅을할 수 있도록 하였습니다.

**(2) 특징:** 추후에 프로젝트에 합류하는 사람들도 전체적인 프로젝트 진행상황을 알 수 있도록 기존 채팅 내역 불러오기 기능을 무한스크롤로 구현하였습니다.

</div>
</details>

<details>
<summary>일정관리 캘린더</summary>
<div markdown="1">

**(1) 목적:** 프로젝트내 모든 사람들이 일정을 확인하고 공유 하도록 하였습니다.

**(2) 특징:** 캘린더를 사용하여 일정추가/일정확인/삭제 기능 구현하였습니다.

</div>
</details>



## 🧑‍💻 기술적 의사 결정
|기술명|이유|
|:---|:---|
|Spring Boot|자바의 웹 프레임워크로, 특정 Library의 버전 자동 설정 기능을 이용하여 쉽고 빠른 웹 개발을 위하여 선택|
|MYSQL|현재 사용되는 서비스의 데이터간의 관계 및 확장성을 고려했을 때, RDBMS 인 MYSQL을 선택|
|Stomp,Sock.Js|기존의 Websocket위에서 작동하는 프로토콜로, 클라이언트와 서버의 통신에 사용할 메시징 형식이 정의되어 있고, 그에 맞춰서 데이터 송수신에 편리함이 있어 선택 <br> Spring STOMP의 내장된 Message Broker와 Publish/Subscribe 기능을 통해, 데이터를 원하는 클라이언트로 전달하는데 편리함이 있어 선택 <br> 유저의 브라우저가 Websocket 지원하지 않을 경우를 대비하여 Sock.js 선택|
|Redis|채팅 데이터를 빠르게 읽고 쓰기 위한 caching용 DB로 선택|


## ⚡ 트러블슈팅

<details>
<summary>Redis 를 활용한 채팅데이터 쓰기 전략 수립 및 속도 개선</summary>
<div markdown="1">

(1) 문제상황

Websocket으로 들어오는 채팅내역들을 들어올 때 마다, MYSQL에 Insert 를 해야함. 계속해서 MYSQL Connection을 만들어서 쓰기 작업을 하는것은 비효율적이라고 판단. Redis에 caching 시킨 후 MYSQL에 일괄 저장하는 방법을 고민하기로함

(2) 해결방안 검토

1. Spring Data JDBC 를 이용한 batchUpdate()  / Entity Generation Type: Identity  

    → Batch insert 가능

2. Spring Data JPA를 이용한 SaveAll()  / Entity Generation Type: Identity 

    →  Batch insert 불가능 (Hibernate Flush 방식 Transactional Write Behind에 위배됨으로 사용 못함)

3. Spring Data JPA를 이용한 SaveAll() /Entity Generation Type: Table

    → Batch insert 가능


(3) 의견결정 (Test 통해 결정)

**Test 환경**

- **Java Version : 11**
- **Spring Boot : 2.7.3**
- **Test Libaray : Junit5**
- **RDBS : mysql 8.0**
- 데이터 읽기
- 채팅 데이터 읽기 쓰기 전략 간단 모식도

  **Test 결과**

![쓰기테스트결과](https://user-images.githubusercontent.com/70882917/193656447-637825ce-7f50-4d64-bc59-f9cecd9435a2.png)


**의견결정**

Spring Data JDBC 를 이용한 batchUpdate() 로 redis에 caching 되어 있는 데이터들을 writeback 방식으로 mysql 저장하는 방식 선택

</div>
</details>

<details>
<summary>Redis 를 활용한 채팅데이터 읽기 전략 수립 및 속도 개선(Caching)</summary>
<div markdown="1">
(1) 문제상황

MYSQL 에 저장되어 있는 기존의 채팅 데이터를 Redis에 어느 기준으로 caching 시켜놓아야 효율적인 관리가 될 수 있을지 고민하기로 함

(2) 해결방안 검토

1. redis에 채팅 데이터를 caching 시켜놓지 않고, 사용자가 채팅 데이터를 요청할 경우, 해당 데이터를 mysql로 부터 읽어들이고 해당 내용을 caching 하는 방법
2. redis에 매일 새벽 채팅 데이터 7일치를 최신 caching 해놓는 방법 &
최근 7일 이전 데이터를 요청할 경우, Mysql로부터 데이터를 끌어와 redis에 caching 해놓는 방법 

(3) 의견결정

1. 서비스 특성상, 채팅 데이터를 최신순으로 읽어오고, 프로젝트 페이지로 넘어올 때, 채팅이 자동 렌더링 되면서 데이터를 조회하기 때문에 , 최신 7일치 데이터를 caching 해놓는 방법 선택
2. Redis에 데이터 저장할 수 있는 용량이 제한이 있어서, User Test(100~150명)를 통해 최신 7일치 데이터를 caching 해도 무리없겠다고 판단.

</div>
</details>

<details>
<summary>채팅 데이터 읽기 쓰기 전략 간단 모식도</summary>
<div markdown="1">

![레디스 모식도](https://user-images.githubusercontent.com/70882917/193656866-fd14d8ad-7e51-40e9-a4f0-8fe1cd087f69.png)

</div>
</details>
  

<details>
<summary>Cursor Pagination 을 통해 채팅 데이터 읽기</summary>
<div markdown="1">

(1) 문제상황 ****

DB에 저장되어 있는 채팅 데이터를 Pagination을 통해 데이터를 제공해줘야 하는 상황에서, 데이터 누락,중복 없으면서, 효율적인 방법을 찾아야 했음.

(2) 해결방안 검토

1. Off-Set Pagination
    
    페이지 번호 (offset) 페이지 사이즈 (limit)을 기반으로 데이터 조회 페이지 번호까지 데이터를 전부 읽고 그 다음 필요한 데이터를 가져와서 사용하는 방식
    
2. Cursor Pagination
    
    Cursor를 기반으로 필요한 데이터를 가져오는 방식, 불필요한  데이터 조회가 없다.
    Off-set paging 의 존재하는 데이터 누락 혹은 중복 현상이 없다
    

(3) 의견 결정 (Test 통해 결정)

Mysql 로 부터 데이터 조회오는 방식에 따른 시간 분석  

**Test 환경**

- **Java Version : 11**
- **Spring Boot : 2.7.3**
- **Test Libaray : Junit5**
- **RDBS : mysql 8.0**

**Test 결과**

![페이징 테스트](https://user-images.githubusercontent.com/70882917/193657547-fcce6cba-03d9-43a6-85b8-8b3c60522f31.png)


**의견결정**

Off-Set paging 의 경우, 새로운 데이터가 삽입될 시, 데이터 중복 현상이 존재할 수 있기 때문에, 성능 측면 기능 측면 모두 Cursor Paging이 적합하다고 판단하여서,  Cursor Paging 방식 선택

</div>
</details>

