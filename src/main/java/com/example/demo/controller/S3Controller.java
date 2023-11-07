package com.example.demo.controller;

import com.example.demo.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController  // 이 클래스를 REST 컨트롤러로 등록합니다.
@RequestMapping("/api/s3")  // 이 컨트롤러의 기본 URL 경로를 설정합니다.
public class S3Controller {
    @Autowired  // S3Service 객체를 스프링에서 자동으로 주입합니다.
    private S3Service s3Service;

    // 파일을 업로드하는 API 엔드포인트입니다.
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        // S3Service를 사용하여 파일을 S3에 업로드하고, 업로드된 파일의 S3 URL을 반환합니다.
        return s3Service.uploadFile(file);
    }


}
