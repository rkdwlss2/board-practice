# board-practice

Spring Boot 기반 게시판 API 프로젝트입니다. 회원 가입/로그인, 게시글 CRUD, 댓글, 좋아요, 파일 업로드, Elasticsearch 게시글 검색 기능을 포함합니다.

## 기술 스택

- Java 17
- Spring Boot 4.0.6
- Spring MVC
- Spring Data JPA
- Spring Security
- MySQL
- Elasticsearch Java Client 7.17.24
- Gradle
- Lombok

## 주요 기능

- 회원
  - 회원가입
  - JSON 기반 로그인
  - 내 정보 조회
  - 닉네임 수정
  - 비밀번호 수정
  - 회원 탈퇴
- 게시글
  - 게시글 목록 조회
  - 게시글 상세 조회
  - 게시글 생성
  - 게시글 수정
  - 게시글 삭제
  - 게시글 이미지 업로드
- 댓글
  - 댓글 목록 조회
  - 댓글 작성
  - 댓글 수정
  - 댓글 삭제
- 좋아요
  - 게시글 좋아요
  - 게시글 좋아요 취소
  - 사용자/게시글 기준 중복 좋아요 방지
- 검색
  - Elasticsearch 기반 제목/본문 검색
  - 제목 검색 가중치 적용
  - 생성일 기준 정렬

## 프로젝트 구조

```text
src/main/java/com/example/boardpractice
├── common          # 공통 유틸, AOP, 커스텀 애노테이션
├── config          # Security, CORS, Elasticsearch 설정
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
- Elasticsearch: `http://localhost:9200`

기본 설정은 [application.yaml](src/main/resources/application.yaml)에 있고, 환경별 DB/Elasticsearch 주소는 profile 설정 파일에서 관리합니다.

```yaml
# application-local.yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_ADDRESS}/boardpractice?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  elasticsearch:
    uris: ${ELASTICSEARCH_URIS:http://localhost:9200}
```

## Elasticsearch 인덱스 생성

게시글 생성 시 `boards` 인덱스에 문서를 색인하고, 검색 API도 `boards` 인덱스를 조회합니다.

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

위 설정은 Elasticsearch에 Nori analysis plugin이 설치되어 있어야 사용할 수 있습니다.

## 실행 방법

```bash
./gradlew bootRun
```

테스트 실행:

```bash
./gradlew test
```

애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

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
| GET | `/boards/posts` | 게시글 목록 조회 | No |
| GET | `/boards/posts/{boardId}` | 게시글 상세 조회 | No |
| POST | `/boards/posts` | 게시글 생성 | Yes |
| PUT | `/boards/posts/{boardId}` | 게시글 수정 | Yes |
| DELETE | `/boards/posts/{boardId}` | 게시글 삭제 | Yes |
| POST | `/boards/posts/{boardId}/image` | 게시글 이미지 업로드 | Yes |
| GET | `/boards/posts/search` | 게시글 검색 | No |

게시글 생성 요청 예시:

```http
POST /boards/posts
Content-Type: application/json

{
  "title": "게시글 제목",
  "content": "게시글 내용"
}
```

검색 요청 예시:

```http
GET /boards/posts/search?keyword=검색어&page=0&size=10
```

### Comment

| Method | Path | Description | Auth |
| --- | --- | --- | --- |
| GET | `/boards/posts/{boardId}/comment` | 댓글 목록 조회 | No |
| POST | `/boards/posts/{boardId}/comment` | 댓글 작성 | Yes |
| PUT | `/boards/posts/comments/{commentId}` | 댓글 수정 | Yes |
| DELETE | `/boards/posts/comment/{commentId}` | 댓글 삭제 | Yes |

댓글 작성 요청 예시:

```http
POST /boards/posts/{boardId}/comment
Content-Type: application/json

{
  "content": "댓글 내용"
}
```

### Like

| Method | Path | Description | Auth |
| --- | --- | --- | --- |
| POST | `/boards/likes/{boardId}` | 좋아요 | Yes |
| DELETE | `/boards/likes/{boardId}` | 좋아요 취소 | Yes |

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
- DB 계정, 비밀번호, Elasticsearch 주소는 환경에 맞게 수정해야 합니다.
- Elasticsearch 검색 필드명은 색인 문서(`BoardDocument`)와 인덱스 mapping의 필드명이 일치해야 합니다.
- 파일 업로드 경로는 기본적으로 `./uploads`입니다.
