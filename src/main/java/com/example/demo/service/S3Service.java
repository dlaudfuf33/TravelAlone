package com.example.demo.service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.amazonaws.services.s3.model.CryptoStorageMode.ObjectMetadata;

@Service  // 이 클래스를 스프링 서비스로 등록합니다.
public class S3Service {

    @Autowired  // AmazonS3 객체를 스프링에서 자동으로 주입합니다.
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")  // application.yml에서 버킷 이름을 가져옵니다.
    private String bucket;

    // 파일을 S3에 업로드하고, 해당 파일의 S3 URL을 반환하는 메서드입니다.
    public String uploadFile(MultipartFile file) {
        try {
            // MultipartFile을 File로 변환합니다.
            File convertedFile = convertMultiPartFileToFile(file);
            // 파일 이름을 현재 시간(밀리초) + 원본 파일 이름으로 설정합니다.
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // S3에 파일을 업로드합니다.
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, convertedFile));
            // 로컬에 저장된 변환 파일을 삭제합니다.
            convertedFile.delete();

            // 업로드된 파일의 S3 URL을 반환합니다.
            return fileName;
        } catch (Exception e) {
            // 파일 업로드 중 예외가 발생하면, 런타임 예외를 발생시킵니다.
            throw new RuntimeException("S3 파일 업로드 중 에러가 발생하였습니다: " + e.getMessage());
        }
    }

    // MultipartFile을 File로 변환하는 메서드입니다.
    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        // "uploaded" 디렉토리가 없다면 생성합니다.
        File directory = new File("uploaded");
        if (!directory.exists()) {
            directory.mkdir();
        }

        // 변환된 파일 객체를 생성합니다. 파일은 "uploaded" 디렉토리에 저장됩니다.
        File convertedFile = new File(directory, file.getOriginalFilename());

        // FileOutputStream을 사용하여 변환된 파일에 MultipartFile의 데이터를 씁니다.
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }

        // 변환된 파일을 반환합니다.
        return convertedFile;
    }

    public String uploadMedia(byte[] mediaData, String fileName) {
        try {
            // S3 버킷 이름과 미디어 파일의 파일 이름을 지정합니다.
            String s3Key = fileName;

            // 미디어 데이터를 업로드하기 위한 ObjectMetadata 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(mediaData.length);

            // S3에 미디어 데이터를 업로드하기 위한 PutObjectRequest를 생성합니다.
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, s3Key, new ByteArrayInputStream(mediaData), metadata);

            // 미디어를 S3에 업로드합니다.
            amazonS3.putObject(putObjectRequest);

            // 업로드된 미디어의 S3 URL을 반환합니다.
            System.out.println(s3Key);
            return generateMediaUrl(s3Key);
        } catch (Exception e) {
            // 업로드 중 발생할 수 있는 예외를 처리합니다.
            e.printStackTrace();
            return null;
        }
    }

    private String generateMediaUrl(String s3Key) {
        return "https://" + bucket + ".s3.amazonaws.com/" + s3Key;
    }

    public String generateImageUrl(String fileUrl) {
        return "https://" + bucket + ".s3.amazonaws.com/" + fileUrl;
    }


    public String generateVideoUrl(String fileUrl) {
        return "https://" + bucket + ".s3.amazonaws.com/" + fileUrl;
    }


    public String generateOtherFileUrl(String fileUrl) {
        return "https://" + bucket + ".s3.amazonaws.com/" + fileUrl;
    }

}
