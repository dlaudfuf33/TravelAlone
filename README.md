![스크린샷 2024-06-06 오후 7 56 54](https://github.com/dlaudfuf33/livealone/assets/100833610/65eb58cb-b521-40c5-b497-1839e1d812d6)

LiveAlone 프로젝트

여행에 대한 커뮤니티 웹 애플리케이션입니다.

## 개요

**LiveAlone** 프로젝트는 사용자는 지역 기반의 정보를 얻고,여행지에 대한 커뮤니티에서 서로 도움을 주고 받을 수 있습니다.

## 목적

1. **정보 제공**: 사용자에게 필요한 여행 정보를 지역 기반으로 제공.
2. **커뮤니티 형성**: 사용자 간 소통과 정보 공유를 통한 커뮤니티 형성.


## 기술스택
<div align=center> 
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white"/>
<img src="https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=TypeScript&logoColor=white"/>
<img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white"/>
<img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=Hibernate&logoColor=white"/>
<img src="https://img.shields.io/badge/SpringSecurity%20JWT-000000?style=for-the-badge&logo=JSON%20Web%20Tokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"/>
</div>



## 구현 기능 

- 회원가입, 로그인
- 댓글 관리
- 여행지 관리
- 게시물 관리
- 추천 관리
- 역할 관리
- 파일 업로드 관리
- 사용자 활동 관리
  

## 관련 파일 및 클래스

#### 회원가입, 로그인
- `UserController.java`: 사용자 회원가입 및 로그인 관리

### [댓글 관리]

- `CommentController.java`: 댓글 작성, 수정, 삭제, 조회 관리

### [여행지 관리]

- `DestinationController.java`: 여행지 생성, 수정, 삭제, 조회 관리

### [게시물 관리]

- `PostController.java`: 게시물 작성, 수정, 삭제, 조회 관리

### [추천 관리]

- `RecommendationController.java`: 추천 기능 관련 관리

### [역할 관리]

- `RoleController.java`: 역할 생성, 수정, 삭제, 조회 관리

### [파일 업로드 관리]

- `S3Controller.java`: 파일 업로드, 삭제, 조회 관리

### [사용자 활동 관리]

- `UserDestinationActivityController.java`: 사용자 활동 데이터 관리
- `UserDestinationController.java`: 사용자 여행지 관리

## 데이터 흐름 요약

### 회원가입, 로그인
1. 사용자는 이메일과 비밀번호를 이용해 회원가입 및 로그인을 할 수 있습니다.
2. 회원가입 시 입력된 비밀번호는 솔팅(salting)과 해싱(hassing)과정을 거쳐 안전하게 저장됩니다.


### 댓글 관리
1. 사용자는 댓글을 작성, 수정, 삭제할 수 있습니다.
2. 댓글의 상세 내용을 조회할 수 있습니다.

### 여행지 관리
1. 관리자는 여행지를 등록, 수정, 삭제할 수 있습니다.
2. 모든 여행지 목록을 조회할 수 있으며, 특정 여행지의 상세 정보도 조회할 수 있습니다.

### 게시물 관리
1. 사용자는 게시물을 작성, 수정, 삭제할 수 있습니다.
2. 게시물의 상세 내용을 조회할 수 있습니다.
3. 사용자는 댓글을 작성, 수정, 삭제할 수 있습니다.

### 추천 관리
1. 사용자는 추천 여행지 목록을 조회할 수 있습니다.
2. 추천 기능은 사용자의 활동 데이터를 기반으로 작동합니다.

### 역할 관리
1. 관리자는 새로운 역할을 생성, 수정, 삭제할 수 있습니다.
2. 역할 목록을 조회할 수 있으며, 특정 역할의 상세 정보도 조회할 수 있습니다.

### 파일 업로드 관리
1. 사용자는 파일을 업로드하고, 업로드된 파일을 조회하거나 삭제할 수 있습니다.
2. AWS S3를 사용하여 파일 관리를 수행합니다.

### 사용자 활동 관리
1. 사용자의 여행지페이지에서의 활동을 수집할 수 있습니다.
2. 수집된 활동 데이터를 기반으로 개인화된 추천이 제공됩니다.

## Swagger API 문서
![스크린샷 2024-06-06 오후 8 56 47](https://github.com/dlaudfuf33/livealone/assets/100833610/a19ccf44-f6a5-47d9-a8c3-a5dafa919993)

### Role API
역할(Role) 관련 API 엔드포인트

| Method | URL                            | Description               |
|--------|--------------------------------|---------------------------|
| GET    | /api/roles                     | 모든 역할을 조회          |
| GET    | /api/roles/read/{roleName}     | 특정 ID의 역할을 조회     |
| DELETE | /api/roles/delete/{roleId}     | 특정 ID의 역할을 삭제     |

### Destinations API
여행지(Destinations) 관련 API 엔드포인트

| Method | URL                                     | Description                      |
|--------|-----------------------------------------|----------------------------------|
| POST   | /api/destinations/update/{destinationId}| 여행지 정보 업데이트             |
| POST   | /api/destinations/create                | 새로운 여행지 생성               |
| GET    | /api/destinations                       | 모든 여행지 목록 조회            |
| GET    | /api/destinations/view/{id}             | 특정 ID의 여행지 조회            |
| GET    | /api/destinations/searchByAuthor/{authorName} | 특정 작성자명으로 여행지 조회 |
| GET    | /api/destinations/recommend/popular     | 인기 있는 (평점 높은 순) 여행지 추천 |
| DELETE | /api/destinations/{id}                  | 특정 ID의 여행지 삭제            |

### Recommendations API
추천(Recommendations) 관련 API 엔드포인트

| Method | URL                                     | Description                      |
|--------|-----------------------------------------|----------------------------------|
| GET    | /api/recommendations                    | 모든 추천 목록 조회              |
| GET    | /api/recommendations/{id}               | 특정 ID의 추천 조회              |
| DELETE | /api/recommendations/{id}               | 특정 ID의 추천 삭제              |
| GET    | /api/recommendations/foruser/{userId}/{count} | 사용자에게 여행지 개인추천  |

### User API
사용자(User) 관련 API 엔드포인트

| Method | URL                            | Description                      |
|--------|--------------------------------|----------------------------------|
| PUT    | /api/users/update/{userId}     | 특정 ID의 사용자 업데이트       |
| POST   | /api/users/signup              | 사용자 가입                      |
| POST   | /api/users/logout              | 사용자 로그아웃                  |
| POST   | /api/users/login               | 사용자 로그인                    |
| GET    | /api/users/userinfo            | 사용자 정보 조회                 |
| DELETE | /api/users/delete/{userId}     | 특정 ID의 사용자 삭제            |

### Comment API
댓글을 생성, 조회, 수정, 삭제하는 API

| Method | URL                            | Description                      |
|--------|--------------------------------|----------------------------------|
| PUT    | /api/comments/update/{id}      | 특정 ID의 댓글을 수정            |
| POST   | /api/comments/create           | 새로운 댓글 생성                 |
| GET    | /api/comments/read/{id}        | 특정 ID의 댓글 조회              |
| GET    | /api/comments/posts/{postId}   | 특정 게시물에 연결된 댓글 조회   |
| DELETE | /api/comments/delete/{id}      | 특정 ID의 댓글 삭제              |

### Posts API
게시물(Posts) 관련 API 엔드포인트

| Method | URL                            | Description                      |
|--------|--------------------------------|----------------------------------|
| PUT    | /api/posts/update/{id}         | 특정 ID의 게시물을 수정          |
| POST   | /api/posts/create              | 게시물 생성                      |
| GET    | /api/posts                     | 모든 게시물의 모든 데이터를 조회 |
| GET    | /api/posts/view/{id}           | 특정 ID의 게시물 조회            |
| DELETE | /api/posts/delete/{id}         | 특정 ID의 게시물을 삭제          |

### User Destination Activity Controller
사용자 활동 데이터 관리

| Method | URL                            | Description                      |
|--------|--------------------------------|----------------------------------|
| GET    | /api/activities/{id}           | 특정 ID의 활동을 조회            |
| PUT    | /api/activities/{id}           | 특정 ID의 활동을 수정            |
| DELETE | /api/activities/{id}           | 특정 ID의 활동을 삭제            |
| POST   | /api/activities/collect        | 활동 데이터 수집                 |
| GET    | /api/activities                | 활동 데이터 조회                 |

### User Destination Controller
사용자 여행지 관리

| Method | URL                            | Description                      |
|--------|--------------------------------|----------------------------------|
| POST   | /api/userDestinations/review   | 여행지 리뷰 생성                 |
| GET    | /api/userDestinations          | 모든 여행지 조회                 |
| GET    | /api/userDestinations/{id}     | 특정 ID의 여행지 조회            |
| DELETE | /api/userDestinations/{id}     | 특정 ID의 여행지 삭제            |

### S3 Controller
파일 업로드 관리

| Method | URL                            | Description                      |
|--------|--------------------------------|----------------------------------|
| POST   | /api/s3/upload                 | 파일 업로드                      |

### Test Controller

| Method | URL                            | Description                      |
|--------|--------------------------------|----------------------------------|
| GET    | /api/test                     | 테스트 엔드포인트                 |

## 이미지 예시

### 메인화면
![스크린샷 2024-06-06 오후 7 56 54](https://github.com/dlaudfuf33/livealone/assets/100833610/c2ac6623-e1f3-4468-81a8-d0ac3ffeb18f)


### 자유게시판 글 등록
![스크린샷 2024-06-06 오후 7 57 22](https://github.com/dlaudfuf33/livealone/assets/100833610/10a4b46d-9274-49a2-8e8c-5b76a4dcd487)

### 여행지 목록
![스크린샷 2024-06-06 오후 7 58 11](https://github.com/dlaudfuf33/livealone/assets/100833610/26276f8c-99f1-46ea-a795-2ff863e3639c)


### 여행지 상세조회
![스크린샷 2024-06-06 오후 7 58 21](https://github.com/dlaudfuf33/livealone/assets/100833610/c140ec49-bee0-44e6-a5cc-94aec3a048cc)


## ERD :
![image](https://github.com/dlaudfuf33/livealone/assets/100833610/977990af-143e-4aec-bf0d-14e580d9eb95)
## 플로우차트 : 
![image](https://github.com/dlaudfuf33/livealone/assets/100833610/3d095940-d7db-4073-a715-d66b65dd2e96)
## 아키텍쳐 : 
![image](https://github.com/dlaudfuf33/livealone/assets/100833610/65460b82-2356-440f-8b55-dfb9688681db)

