# Arc'teryx Fit Board

아크테릭스 제품을 구매하기 전 필요한 착샷, 착용감, 사이즈 정보를 사용자들이 직접 공유하고 찾아볼 수 있는 커뮤니티 API 프로젝트입니다. 사용자 후기에 더해 Groq 멀티모달 모델을 활용한 AI 핏 확인 기능으로 개인의 체형과 원하는 착용 방식에 맞는 사이즈 선택을 돕습니다.

단순 게시판이 아니라, 같은 제품을 입어 본 사람들의 실제 후기를 기반으로 구매 판단을 돕는 것을 목표로 합니다. 사용자는 게시글과 이미지를 통해 자신의 착용 사진을 올리고, 본문과 댓글로 체형별 핏, 사이즈 추천, 레이어링 조합, 실사용감을 나눌 수 있습니다.

현재 백엔드는 회원 가입/로그인, 게시글 CRUD, 댓글, 좋아요, S3 이미지 업로드, OpenSearch 기반 검색, Groq 기반 AI 핏 확인 기능을 제공합니다.

## 관련 링크

- [프로젝트 소개 동영상](https://www.youtube.com/watch?v=b8WDZwWoiVk)
- [Figma draft](https://www.figma.com/design/qWsJLHs9mkiL9VKjHrdaYU/board-practice-Figma-draft?node-id=2-2&p=f&t=G6aC35WbpeIvmaDh-0)
- [API 설계서](https://docs.google.com/spreadsheets/d/19TSP1DUNjSzVFpn31pWY3r7ZlEfz5WZxMNU5zR8tiTg/edit?usp=sharing)
- [ERD](https://www.erdcloud.com/d/g5TxBGgkHb2NkNmHo)

## 기술 스택

- Java 17
- Spring Boot 4.0.6
- Spring MVC
- Spring Data JPA
- Spring Security
- MySQL
- OpenSearch Java Client
- AWS SDK for Java (S3)
- Groq API
- Gradle
- Lombok

## 서비스 목적

아크테릭스는 제품별로 `Trim Fit`, `Regular Fit` 등 핏 구분이 다양하고, 같은 표기 사이즈라도 제품군과 출시 시기에 따라 실제 착용감이 달라질 수 있습니다. 기존에 L을 입던 사용자가 어떤 제품에서는 M부터 비교해야 하는 것처럼 일반적인 국내 브랜드의 사이즈 감각만으로 선택하기 어려운 경우도 있습니다.

하지만 이런 차이를 한곳에서 비교할 수 있는 아크테릭스 중심의 국내 착샷·사이즈 정보 게시판은 충분하지 않습니다. 이 프로젝트는 공식 치수표만으로 알기 어려운 실제 착용 정보를 사용자들의 사진과 후기로 축적하고, 개인별 사이즈 고민을 줄이기 위해 만들었습니다.

커뮤니티 정보만으로 판단하기 어려운 경우에는 AI 핏 확인 기능을 사용할 수 있습니다. 사용자가 참고하려는 게시글의 착샷과 제품·후기 정보에 자신의 키, 몸무게, 평소 상·하의 및 신발 사이즈, 원하는 핏을 함께 입력하면 Groq 모델이 정보를 비교합니다. 예를 들어 오버핏이나 레이어링을 원할 때 현재 사이즈가 적합한지, 사이즈 업 또는 다운을 고려해야 하는지와 그 이유를 안내합니다. AI 결과는 구매를 돕는 참고 정보이며 실제 신체 치수나 착용 결과를 보장하지 않습니다.

주요 사용 흐름은 다음과 같습니다.

- 사용자가 자신의 아크테릭스 착샷과 착용 후기를 게시글로 등록
- 제품명, 사이즈, 체형, 착용감, 레이어링 정보를 본문에 기록
- 다른 사용자가 댓글로 추가 질문이나 사이즈 조언을 남김
- 좋아요와 조회수를 통해 참고 가치가 높은 게시글을 확인
- OpenSearch 검색으로 원하는 제품명, 사이즈, 핏 키워드의 후기를 빠르게 탐색
- 참고할 게시글을 선택하고 자신의 체형, 평소 사이즈, 원하는 핏을 입력
- Groq AI가 게시글의 착샷과 후기 정보를 함께 분석해 개인화된 사이즈 조언 제공

## 주요 기능

- 회원
  - 회원가입
  - JSON 기반 로그인
  - 내 정보 조회
  - 닉네임 수정
  - 비밀번호 수정
  - 회원 탈퇴
  - 프로필 이미지 업로드
- 게시글
  - 아크테릭스 착샷/후기 게시글 목록 조회
  - 착용감, 사이즈 정보, 구매 참고 내용을 포함한 게시글 상세 조회
  - 게시글 생성, 수정, 삭제
  - 착샷 이미지 업로드
  - 조회수 집계
- 댓글
  - 사이즈 문의, 착용감 질문, 구매 조언 댓글 작성
  - 댓글 목록 조회, 수정, 삭제
- 좋아요
  - 참고가 된 착샷/후기 게시글 좋아요
  - 게시글 좋아요 취소
  - 사용자/게시글 기준 중복 좋아요 방지
- 검색
  - OpenSearch 기반 제목/본문 검색으로 제품명, 사이즈, 핏 키워드 탐색
  - 제목 검색 가중치 적용
  - 전체 텍스트 검색
  - 좋아요 기준 검색 결과 정렬
  - 생성일 기준 정렬
- AI 핏 확인
  - 게시글의 착샷, 제목, 본문에 담긴 제품 및 착용 정보 분석
  - 사용자의 키, 몸무게, 평소 상·하의 및 신발 사이즈 반영
  - 오버핏, 정핏 등 사용자가 원하는 핏 반영
  - 현재 사이즈 적합 여부와 사이즈 업·다운 조언 및 판단 이유 제공
  - 정보가 부족한 경우 불확실한 결과임을 명시

## 게시글 예시

현재 게시글 모델은 `title`, `content`, `boardImageUrl`을 중심으로 구성되어 있으며, 사이즈와 착용감 정보는 본문에 함께 작성하는 방식입니다.

```text
제목: Beta AR M 사이즈 착용감 후기
본문:
- 키/몸무게: 178cm / 72kg
- 평소 상의: 100-105
- 착용 사이즈: M
- 착용감: 얇은 플리스까지 레이어링 가능, 기장은 골반을 살짝 덮음
- 구매 참고: 여유로운 핏을 원하면 정사이즈, 도심 착용 위주면 한 사이즈 다운도 고려
```

## 프로젝트 구조

```text
src/main/java/com/example/boardpractice
├── common          # 공통 유틸, AOP, 커스텀 애노테이션
├── config          # Security, CORS, OpenSearch 설정
├── entity          # JPA Entity
├── exception       # 커스텀 예외
├── handler         # 전역 예외 핸들러
├── repository      # Spring Data JPA Repository
├── security        # JSON 로그인 필터, UserDetails
├── service         # 비즈니스 로직
└── web
    ├── api         # REST API Controller
    └── dto         # Request/Response DTO
```

## 실행 환경

로컬 실행 기준으로 아래 서비스가 필요합니다.

- MySQL: `localhost:3306`
- OpenSearch: `http://localhost:9200`

기본 설정은 [application.yaml](src/main/resources/application.yaml)에 있고, 환경별 DB/OpenSearch 주소는 profile 설정 파일에서 관리합니다.

```yaml
# application-local.yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_ADDRESS}/boardpractice?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  opensearch:
    uris: ${OPENSEARCH_URIS:http://localhost:9200}
```

## OpenSearch 인덱스 생성

애플리케이션 기동 시 `boards` 인덱스가 없으면 아래 설정으로 자동 생성합니다. 게시글 생성 시 `boards` 인덱스에 문서를 색인하고, 검색 API도 `boards` 인덱스를 조회합니다.

```http
PUT /boards
{
  "settings": {
    "analysis": {
      "analyzer": {
        "boards_content_analyzer": {
          "char_filter": [],
          "tokenizer": "nori_tokenizer",
          "filter": [
            "nori_part_of_speech",
            "nori_readingform",
            "lowercase"
          ]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "boardId": {
        "type": "long"
      },
      "title": {
        "type": "text",
        "analyzer": "boards_content_analyzer"
      },
      "content": {
        "type": "text",
        "analyzer": "boards_content_analyzer"
      },
      "writer": {
        "type": "keyword",
        "fields": {
          "text": {
            "type": "text",
            "analyzer": "boards_content_analyzer"
          }
        }
      },
      "createdAt": {
        "type": "date"
      }
    }
  }
}
```

위 설정은 OpenSearch에 Nori analysis plugin이 설치되어 있어야 사용할 수 있습니다.

## 실행 방법

```bash
./gradlew bootRun
```

테스트 실행:

```bash
./gradlew test
```

애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

로컬 OpenSearch만 Docker로 실행:

```bash
docker compose --profile local up -d opensearch
```

## Docker 실행

EC2에서는 환경변수를 이미지에 포함하지 말고 서버의 env 파일로 관리합니다.

```bash
sudo nano /etc/board-practice.env
```

```properties
DB_ADDRESS=database-1.c36gqoimycz2.ap-northeast-2.rds.amazonaws.com:3306
DB_USER=admin
DB_PASSWORD=your-db-password
PUBLIC_IP=13.125.36.40
SPRING_PROFILES_ACTIVE=dev
OPENSEARCH_URIS=https://your-opensearch-domain.ap-northeast-2.es.amazonaws.com
GROQ_API_KEY=your-groq-api-key
GROQ_MODEL=qwen/qwen3.6-27b
```

```bash
sudo chmod 600 /etc/board-practice.env
docker compose up -d --build
```

로그 확인:

```bash
docker logs -f board-practice
```

중지:

```bash
docker compose down
```

## 인증 방식

Spring Security 기반 세션 인증을 사용합니다. 로그인은 `/users/login`에서 JSON 요청을 처리하는 커스텀 필터(`JsonLoginFilter`)로 동작합니다.

로그인 요청 예시:

```http
POST /users/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

로그인 성공 시 세션이 생성되고, 이후 인증이 필요한 API는 세션 쿠키(`JSESSIONID`)를 사용합니다.

## API 목록

### User

| Method | Path | Description | Auth |
| --- | --- | --- | --- |
| POST | `/users/signup` | 회원가입 | No |
| POST | `/users/login` | 로그인 | No |
| GET | `/users/me` | 내 정보 조회 | Yes |
| PUT | `/users/me` | 닉네임 수정 | Yes |
| PUT | `/users/me/password` | 비밀번호 수정 | Yes |
| DELETE | `/users/me` | 회원 탈퇴 | Yes |
| POST | `/users/me/image` | 프로필 이미지 업로드 | Yes |

### Board

| Method | Path | Description | Auth |
| --- | --- | --- | --- |
| GET | `/boards/posts` | 착샷/후기 게시글 목록 조회 | No |
| GET | `/boards/posts/{boardId}` | 착용감/사이즈 정보 상세 조회 | No |
| POST | `/boards/posts` | 착샷/후기 게시글 생성 | Yes |
| PUT | `/boards/posts/{boardId}` | 착샷/후기 게시글 수정 | Yes |
| DELETE | `/boards/posts/{boardId}` | 착샷/후기 게시글 삭제 | Yes |
| POST | `/boards/posts/{boardId}/image` | 착샷 이미지 업로드 | Yes |
| GET | `/boards/posts/search` | 제목/본문 기반 게시글 검색 | No |
| GET | `/boards/posts/search/fulltext` | 전체 텍스트 검색 | No |
| GET | `/boards/posts/search/like` | 좋아요 기준 검색 | No |

게시글 생성 요청 예시:

```http
POST /boards/posts
Content-Type: application/json

{
  "title": "Beta AR M 사이즈 착용감 후기",
  "content": "178cm/72kg, 평소 100-105 착용. M 사이즈는 얇은 플리스까지 레이어링 가능했고 기장은 골반을 살짝 덮습니다."
}
```

검색 요청 예시:

```http
GET /boards/posts/search?keyword=Beta%20AR%20M&page=0&size=10
```

### Comment

| Method | Path | Description | Auth |
| --- | --- | --- | --- |
| GET | `/boards/posts/{boardId}/comment` | 댓글 목록 조회 | No |
| POST | `/boards/posts/{boardId}/comment` | 사이즈 문의/구매 조언 댓글 작성 | Yes |
| PUT | `/boards/posts/comments/{commentId}` | 댓글 수정 | Yes |
| DELETE | `/boards/posts/comment/{commentId}` | 댓글 삭제 | Yes |

댓글 작성 요청 예시:

```http
POST /boards/posts/{boardId}/comment
Content-Type: application/json

{
  "content": "평소 105 입으면 L이 더 나을까요? 겨울에 아톰 LT랑 같이 입을 예정입니다."
}
```

### Like

| Method | Path | Description | Auth |
| --- | --- | --- | --- |
| POST | `/boards/likes/{boardId}` | 참고가 된 후기 좋아요 | Yes |
| DELETE | `/boards/likes/{boardId}` | 좋아요 취소 | Yes |

### AI Fit Check

| Method | Path | Description | Auth |
| --- | --- | --- | --- |
| POST | `/ai/fit-check/posts/{postId}` | 게시글 착샷과 사용자 정보를 이용한 AI 핏 확인 | Yes |

요청 예시:

```http
POST /ai/fit-check/posts/1
Content-Type: application/json

{
  "height": 178,
  "weight": 72,
  "usualTopSize": "L (100-105)",
  "usualBottomSize": "32",
  "usualShoeSize": "270",
  "preferredFit": "플리스 레이어링이 가능한 오버핏"
}
```

응답 예시:

```json
{
  "result": "LIKELY_FITS",
  "label": "원하는 오버핏에 가까울 가능성이 높습니다.",
  "sizeAdvice": "두꺼운 이너를 자주 입는다면 현재 사이즈를 우선 고려하세요.",
  "boardImageUrl": "https://example.com/images/fit.jpg",
  "reasons": [
    "게시글 작성자의 착용 사진에서 품과 소매에 여유가 있습니다.",
    "사용자의 평소 상의 사이즈와 게시글의 제품 정보가 유사합니다.",
    "레이어링이 가능한 여유로운 핏을 선호합니다."
  ]
}
```

`result`는 `LIKELY_FITS`, `SIZE_UP`, `SIZE_DOWN`, `UNCERTAIN` 중 하나입니다. 분석에 사용하는 게시글 이미지는 Groq에서 접근할 수 있는 공개 HTTPS URL이어야 하며, `GROQ_API_KEY`가 설정되지 않으면 기능을 사용할 수 없습니다.

## 데이터 삭제 정책

`Users`, `Boards`, `Comments`는 Hibernate `@SQLDelete`와 `@SQLRestriction`을 사용해 물리 삭제 대신 `delete_date`를 기록하는 소프트 삭제 방식으로 처리합니다.

`Likes`는 사용자와 게시글 조합에 unique constraint를 적용해 중복 좋아요를 방지합니다.

## CORS

허용 origin은 [application.yaml](src/main/resources/application.yaml)에 설정되어 있습니다.

```yaml
cors:
  allowed-origins:
    - http://localhost:5500
    - http://127.0.0.1:5500
    - http://localhost:5173
```

## 참고 사항

- 현재 설정은 로컬 개발 환경을 기준으로 합니다.
- DB 계정, 비밀번호, OpenSearch 주소는 환경에 맞게 수정해야 합니다.
- OpenSearch 검색 필드명은 색인 문서(`BoardDocument`)와 인덱스 mapping의 필드명이 일치해야 합니다.
- 파일 업로드 경로는 기본적으로 `./uploads`입니다.
