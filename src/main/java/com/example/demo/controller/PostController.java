package com.example.demo.controller;

import com.example.demo.entity.FileEntity;
import com.example.demo.entity.Post;
import com.example.demo.repository.FileEntityRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import com.example.demo.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Base64;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "POSTS API", description = "게시판(Posts) 관련 API 엔드포인트")
@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired  // S3Service 객체를 스프링에서 자동으로 주입합니다.
    private S3Service s3Service;

    @Autowired
    private PostService postService;
    @Autowired
    private FileEntityRepository fileEntityRepository;

    @Autowired
    private PostRepository postRepository;


    // 모든 게시물 목록을 가져오는 엔드포인트
    @Operation(summary = "모든 게시물을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();
            System.out.println("모든 게시물 조회: " + posts.size() + "개의 게시물이 반환됨");
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            System.err.println("모든 게시물 조회 중 오류 발생: " + e.getMessage());
            // 예외 발생 시 500 Internal Server Error를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestPart("post") String postStr,
                                           @RequestPart(value = "files", required = false) MultipartFile[] files) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Post post = objectMapper.readValue(postStr, Post.class);

            // 게시물 생성
            Post createdPost = postService.createPost(post);

            if (post.getContents() != null) {
                // HTML 파싱
                Document document = Jsoup.parse(post.getContents());

                // 이미지 및 동영상 태그 처리
                Elements mediaElements = document.select("img, video");

                for (Element mediaElement : mediaElements) {
                    String src = mediaElement.attr("src");
                    if (src.startsWith("data:image") || src.startsWith("data:video")) {
                        // Base64 데이터를 추출하고 S3에 업로드
                        byte[] mediaData = parseBase64Data(src);
                        String fileExtension = src.startsWith("data:image") ? "jpg" : "mp4"; // 이미지와 동영상을 구분하여 확장자 설정
                        String mediaUrl = uploadMediaToS3(mediaData, createdPost.getId(), fileExtension);
                        System.out.println(mediaUrl +"인디 createPost");
                        // S3 URL로 src 속성 치환
                        mediaElement.attr("src", mediaUrl);
                    }
                }

                // 변경된 HTML을 다시 게시물에 설정
                String updatedContent = document.outerHtml();
                System.out.println(updatedContent +"ㅇㅇㅇ");
                createdPost.setContents(updatedContent);

                postService.updatePost(createdPost.getId(), createdPost);

            }

            // 파일 업로드 로직은 이미 구현되어 있으므로 스킵

            System.out.println("게시물 생성: ID " + createdPost.getId() + "의 게시물이 생성됨");
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            System.err.println("게시물 생성 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] parseBase64Data(String dataUri) {
        // data URI에서 Base64 데이터 추출 및 디코딩 로직
        // 예: data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...
        String base64Data = dataUri.split(",")[1];
        return Base64.getDecoder().decode(base64Data);
    }

    private String uploadMediaToS3(byte[] mediaData, Long postId, String fileExtension) {
        try {
            // S3Service를 사용하여 미디어 데이터를 S3에 업로드합니다.
            String fileName = "media_" + postId + "." + fileExtension;
            String s3Url = s3Service.uploadMedia(mediaData, fileName);
            System.out.println("uploadMediaToS3 : "+s3Url);
            // 실제로는 업로드 후에 얻은 S3 URL을 반환해야 합니다.
            return s3Url;
        } catch (Exception e) {
            // 업로드 중 오류가 발생한 경우 예외를 처리하십시오.
            e.printStackTrace();
            return null;
        }
    }


    // 특정 ID의 게시물을 가져오는 엔드포인트
    @GetMapping("/view/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        try {
            Post post = postService.getPostByIdWithFiles(id); // 게시물과 파일 정보 함께 조회
            if (post != null) {
                System.out.println("게시물 조회: ID " + id + "의 게시물이 반환됨");
                System.out.println("게시물 조회: ID " + id + "의" + post);
                return ResponseEntity.ok(post);
            } else {
                System.out.println("게시물 조회: ID " + id + "의 게시물이 없음");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("게시물 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "특정 ID의 게시물을 업데이트합니다.")
    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestPart("post") Post updatedPostData, // 업데이트된 게시물 정보를 요청 바디로 받음
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            // 해당 ID의 게시물을 데이터베이스에서 조회
            Post existingPost = postService.getPostById(id);

            if (existingPost != null) {
                // 업데이트된 게시물 정보를 기존 게시물에 복사 (일부 필드 제외)
                existingPost.setTitle(updatedPostData.getTitle());
                existingPost.setContents(updatedPostData.getContents());
                existingPost.setAuthor(updatedPostData.getAuthor());

                // 업로드된 파일이 있으면 파일 업로드 로직을 수행하고, 파일 경로를 업데이트된 게시물에 설정합니다.
                if (file != null && !file.isEmpty()) {
                    String fileUrl = handleFileUpload(file, id); // postId를 전달
                    if (fileUrl != null) {
                        // 새로운 FileEntity를 생성하거나 기존의 FileEntity를 업데이트합니다.
                        FileEntity newFileEntity = new FileEntity();
                        newFileEntity.setFileUrl(fileUrl);

                        // 기존의 FileEntity 목록을 가져와서 업데이트된 FileEntity를 추가합니다.
                        List<FileEntity> fileEntities = existingPost.getFileEntities();
                        fileEntities.add(newFileEntity);

                        // FileEntity를 저장
                        fileEntityRepository.save(newFileEntity);
                    }
                }

                // 게시물을 업데이트
                Post updatedPost = postService.updatePost(id, existingPost);

                return ResponseEntity.ok(updatedPost);
            } else {
                // 해당 ID의 게시물이 없을 경우 404 Not Found를 반환
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    // 특정 ID의 게시물을 삭제하는 엔드포인트
    @Operation(summary = "특정 ID의 게시물을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            // 성공적으로 삭제되면 204 No Content를 반환
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String handleFileUpload(MultipartFile file, Long postId) throws Exception {
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            String fileUrl = s3Service.uploadFile(file);

            if (contentType != null) {
                String generatedFileUrl = null;
                if (contentType.startsWith("image/")) {
                    generatedFileUrl = s3Service.generateImageUrl(fileUrl); // 이미지 파일 처리 및 URL 생성
                } else if (contentType.startsWith("video/")) {
                    generatedFileUrl = s3Service.generateVideoUrl(fileUrl); // 비디오 파일 처리 및 URL 생성
                } else {
                    generatedFileUrl = s3Service.generateOtherFileUrl(fileUrl); // 그 외의 파일 처리 및 URL 생성
                }

                // FileEntity 객체를 생성하고 파일 URL을 저장합니다.
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileUrl(generatedFileUrl);

                // postId에 해당하는 Post 엔터티 가져오기
                Post post = postRepository.findById(postId).orElse(null);
                if (post != null) {
                    fileEntity.setPost(post); // FileEntity에 연관된 Post 설정
                    fileEntityRepository.save(fileEntity); // FileEntity 저장

                    // Post 엔터티와 FileEntity를 연결하고 변경 사항을 데이터베이스에 저장
                    post.getFileEntities().add(fileEntity);
                    postRepository.save(post);
                }
            }
        }
        return null;
    }


}


