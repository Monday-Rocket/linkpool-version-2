## 링크 관리 서비스

링크풀은 링크를 쉽게 저장하고 카테고리별로 폴더링하여 언제든 쉽게 찾아볼 수 있도록 하는 `링크 관리 앱`입니다. 

### 🔗  Link

- IOS(아이폰) : [https://apps.apple.com/us/app/링크풀-체계적인-링크-관리의-시작/id1644108674](https://apps.apple.com/us/app/%EB%A7%81%ED%81%AC%ED%92%80-%EC%B2%B4%EA%B3%84%EC%A0%81%EC%9D%B8-%EB%A7%81%ED%81%AC-%EA%B4%80%EB%A6%AC%EC%9D%98-%EC%8B%9C%EC%9E%91/id1644108674)
- 안드로이드 : https://play.google.com/store/apps/details?id=com.mr.ac_project_app

### **[서비스 소개 페이지 바로가기](https://www.notion.so/c6ea6729dfac4dbc89c95d294bca43f8?pvs=21)**

## 프로젝트 구조

현재 Layered Architecture로 구성되어있는 프로젝트를 `Hexagonal Architecture`로 전환하기 위해 Domain과 Infra 영역을 분리하고 **Domain → Infra** 방향으로 흐르는 의존성을 제거했습니다.

### Hexagonal Architecture 전환기

- 통속적으로 사용되는 `계층형 구조의 문제점`을 인식
    - 서비스 코드 안에서 **비즈니스 로직과 기술적 요소가 혼합**되어, 서비스가 제공하는 핵심 가치가 잘 보이지 않음
    - **한 패키지 안에** 너무 많은 것들이 들어가면서 구조화가 안됨
    - 자연스럽게 `데이터베이스 주도 설계`를 하게 됨 (DB 스키마부터 정의하면서 시작하게 되는 프로젝트)
- `클린 아키텍처`가 이 문제점을 해결할 수 있을 것이라 기대
    - `의존성 역전`을 통해 영속성이 아니라 **비즈니스 도메인이 서비스의 주인공이 될 수 있도록** ⇒ `DDD` 적용에 용이
    - 도메인이 기술적인 세부사항에 의존하지 않기 때문에 **기술을 도중에 변경하더라도 도메인영역을 유지할 수 있음
      <br> ⇒ ex) JPA ⇒ R2DBC, Servlet Stack ⇒ Reactive Stack**
- `Hexagonal Architecture` ( = **Port and Adapters Architecture)** 채택
    - 클린 아키텍처 중 가장 레퍼런스가 많은 것으로 판단되어 채택
    - **Port & adapters**
        - 도메인을 향하고 있는 의존성 흐름을 표현

### Domain

현재 운영 중인 LinkPool Service를 `DDD(Domain Driven Design)`에 입각하여 전환하였습니다.
- `Event Storming` 방법론 채택
   - [LinkPool Event Storming 바로가기](https://www.figma.com/community/file/1267121665616660420/LinkPool-Event-storming)
- `Context Map` 도출
  - 각각의 서브도메인의 비즈니스가 물리적으로 완전히 격리되어 관리되어야할 만큼 규모가 크지 않다고 판단하여 모놀리틱으로 구현
    ![Untitled](https://complex-aster-37e.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fe46ad8ab-cdcf-442d-9356-1ce44922565d%2FUntitled.png?table=block&id=0514de60-dde4-412b-b94a-e3d2632553ac&spaceId=0257a9fe-c3da-4351-92b7-a807c92c74a1&width=1340&userId=&cache=v2)

- `애그리거트` 별로 패키지 구성
    - `Domain Driven` 전략을 채택한 Bounded Context에 대해서만 애그리거트 구조를 정의
        - 도메인 영역을 거치지 않고 조회 전용 쿼리로만 구현된 `Home&Search`와 서드파티인 `Auth`는 제외
    ![Untitled](https://complex-aster-37e.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F877ba80c-d284-410d-8c60-944fb4e0fae2%2FUntitled.png?table=block&id=453a39d3-87bf-4991-899f-440f565b1c05&spaceId=0257a9fe-c3da-4351-92b7-a807c92c74a1&width=2000&userId=&cache=v2)

### Infra

기존에 채택한 Spring Framework Servlet Stack에서 `Reactive Stack`으로 전환하여 Reactive Web Application을 구현하였습니다.

- Spring Webflux
- Spring Data R2DBC
- Kotlin Coroutine

## Domain Event
`Domain`간의 **의존성을 제거**하고, **트랜잭션을 분리**하여 **비동기**적으로 처리해야할 기능을 `Domain Event`로 구현하였습니다.

- **User** : `회원 탈퇴` 이벤트 **발행**
    - **Folder**: `회원 탈퇴` 이벤트를 **구독**하여 해당 회원의 Folder Batch 삭제
    - **Link** : `회원 탈퇴` 이벤트를 **구독**하여 해당 회원의 Link Batch 삭제

### 💡 Hexagonal Architecture에서 Domain Event의 개념을 `Domain`과 `Infra` 영역으로 분리
- **Domain**
    - 이벤트를 정의하고 이에 대한 `publisher`와 `listener`를 등록
    - `listener` 측에서 이벤트가 발생하면 해야할 행위를 정의
- **Infra**
    - 이벤트를 실제로 발행하여 각각의 listener들에게 aysnc non-blocking하게 전달
 
## Trouble Shooting
> Trouble Shooting

## Front
https://github.com/Monday-Rocket/ac_project_app
