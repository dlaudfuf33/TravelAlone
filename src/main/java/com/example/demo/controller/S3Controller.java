package com.example.demo.controller;

import com.example.demo.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController  // 이 클래스를 REST 컨트롤러로 등록합니다.
@RequestMapping("/api/s3")  // 이 컨트롤러의 기본 URL 경로를 설정합니다.
@RequiredArgsConstructor
public class S3Controller {

    private final  S3Service s3Service;

    // 파일을 업로드하는 API 엔드포인트입니다.
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        // S3Service를 사용하여 파일을 S3에 업로드하고, 업로드된 파일의 S3 URL을 반환합니다.
        return s3Service.uploadFile(file);
    }


}
