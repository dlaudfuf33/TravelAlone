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

### 주요 기능
1. **사용자 관리**
   - 사용자 등록, 수정, 삭제, 조회 기능
   - 관련 파일: `UserController.java`, `User.java`, `UserRepository.java`
   - 예시 코드:
     ```java
     @PostMapping("/register")
     public ResponseEntity<User> registerUser(@RequestBody User user) {
         User createdUser = userService.createUser(user);
         return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
     }
     ```

2. **댓글 관리**
   - 댓글 작성, 수정, 삭제, 조회 기능
   - 관련 파일: `CommentController.java`, `Comment.java`, `CommentRepository.java`
   - 예시 코드:
     ```java
     @PostMapping
     public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
         Comment createdComment = commentService.createComment(comment);
         return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
     }
     ```

3. **여행지 관리**
   - 여행지 생성, 수정, 삭제, 조회 기능
   - 관련 파일: `DestinationController.java`, `Destination.java`, `DestinationRepository.java`
   - 예시 코드:
     ```java
     @PostMapping
     public ResponseEntity<Destination> createDestination(@RequestBody Destination destination) {
         Destination createdDestination = destinationService.createDestination(destination);
         return new ResponseEntity<>(createdDestination, HttpStatus.CREATED);
     }
     ```

4. **게시물 관리**
   - 게시물 작성, 수정, 삭제, 조회 기능
   - 관련 파일: `PostController.java`, `Post.java`, `PostRepository.java`
   - 예시 코드:
     ```java
     @PostMapping
     public ResponseEntity<Post> createPost(@RequestBody Post post) {
         Post createdPost = postService.createPost(post);
         return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
     }
     ```

5. **역할 관리**
   - 역할 생성, 수정, 삭제, 조회 기능
   - 관련 파일: `RoleController.java`, `Role.java`, `RoleRepository.java`
   - 예시 코드:
     ```java
     @PostMapping
     public ResponseEntity<Role> createRole(@RequestBody Role role) {
         Role createdRole = roleService.createRole(role);
         return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
     }
     ```

6. **여행지 활동 관리**
   - 사용자 여행지 활동 기록 및 조회 기능
   - 관련 파일: `UserDestinationActivityController.java`, `UserDestinationActivity.java`, `UserDestinationActivityRepository.java`
   - 예시 코드:
     ```java
     @PostMapping
     public ResponseEntity<UserDestinationActivity> logActivity(@RequestBody UserDestinationActivity activity) {
         UserDestinationActivity loggedActivity = activityService.logActivity(activity);
         return new ResponseEntity<>(loggedActivity, HttpStatus.CREATED);
     }
     ```

7. **사용자 여행지 관리**
   - 사용자가 관심 있는 여행지 추가, 수정, 삭제, 조회 기능
   - 관련 파일: `UserDestinationController.java`, `UserDestination.java`, `UserDestinationRepository.java`
   - 예시 코드:
     ```java
     @PostMapping
     public ResponseEntity<UserDestination> addUserDestination(@RequestBody UserDestination userDestination) {
         UserDestination addedDestination = userDestinationService.addUserDestination(userDestination);
         return new ResponseEntity<>(addedDestination, HttpStatus.CREATED);
     }
     ```

### 추가 기능
1. **Swagger 설정**
   - API 문서화를 위한 Swagger 설정
   - 관련 파일: `SwaggerConfig.java`
   - 예시 코드:
     ```java
     @Configuration
     @EnableSwagger2
     public class SwaggerConfig {
         @Bean
         public Docket api() {
             return new Docket(DocumentationType.SWAGGER_2)
                     .select()
                     .apis(RequestHandlerSelectors.any())
                     .paths(PathSelectors.any())
                     .build();
         }
     }
     ```
1. **이용자 활동 데이터 수집**
   - 사용자의 특정 여행지에서의 활동을 기록
   - 관련 파일 및 클래스: `UserDestinationActivity.java`, `UserDestinationActivityRepository.java`, `UserDestinationActivityController.java`

**UserDestinationActivity.java**
이 클래스는 사용자의 특정 여행지에서의 활동을 기록하는 엔티티입니다.
```java
@Entity
public class UserDestinationActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long destinationId;
    private String activityType;
    private LocalDate activityDate;
    
    // Getters and setters
}
id: 고유 식별자
userId: 사용자 ID
destinationId: 여행지 ID
activityType: 활동 유형 (예: 방문, 리뷰 작성 등)
activityDate: 활동 날짜
UserDestinationActivityRepository.java
이 인터페이스는 UserDestinationActivity 엔티티의 데이터베이스 상호작용을 담당합니다.

java
코드 복사
public interface UserDestinationActivityRepository extends JpaRepository<UserDestinationActivity, Long> {
    List<UserDestinationActivity> findByUserId(Long userId);
}
findByUserId(Long userId): 특정 사용자 ID에 해당하는 활동 기록을 조회
UserDestinationActivityController.java
이 클래스는 사용자 활동 데이터를 관리하는 REST 컨트롤러입니다.

java
코드 복사
@RestController
@RequestMapping("/activities")
public class UserDestinationActivityController {
    
    @Autowired
    private UserDestinationActivityService activityService;
    
    @PostMapping
    public ResponseEntity<UserDestinationActivity> logActivity(@RequestBody UserDestinationActivity activity) {
        UserDestinationActivity loggedActivity = activityService.logActivity(activity);
        return new ResponseEntity<>(loggedActivity, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDestinationActivity> getActivityById(@PathVariable Long id) {
        UserDestinationActivity activity = activityService.getActivityById(id);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }
}
logActivity: 사용자의 활동을 기록
getActivityById: 특정 활동 기록을 조회
     




## ERD :
![image](https://github.com/dlaudfuf33/livealone/assets/100833610/977990af-143e-4aec-bf0d-14e580d9eb95)
## 플로우차트 : 
![image](https://github.com/dlaudfuf33/livealone/assets/100833610/3d095940-d7db-4073-a715-d66b65dd2e96)
## 아키텍쳐 : 
![image](https://github.com/dlaudfuf33/livealone/assets/100833610/65460b82-2356-440f-8b55-dfb9688681db)


![스크린샷 2024-06-06 오후 7 58 11](https://github.com/dlaudfuf33/livealone/assets/100833610/adf17750-122e-4be6-8012-ad89c7e2b106)
![스크린샷 2024-06-06 오후 7 57 22](https://github.com/dlaudfuf33/livealone/assets/100833610/7e50210b-156d-43a5-b06f-0bb2d82caaa6)

![스크린샷 2024-06-06 오후 7 58 21](https://github.com/dlaudfuf33/livealone/assets/100833610/7bc40ecb-d1ec-425d-857e-7e4e402314a9)
