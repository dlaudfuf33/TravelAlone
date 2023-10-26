package com.example.demo;

import com.example.demo.entity.Destination;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Post;
import com.example.demo.service.S3Service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Base64;

@Component
public class MediaProcessor {

    @Autowired
    private S3Service s3Service;

    public void processMediaPost(Document document, Post createdPost) {
        processImages(document, createdPost);
        processVideos(document, createdPost);
    }

    public void processMediaDestination(Document document, Destination createdDestination) {
        processImages(document, createdDestination);
        processVideos(document, createdDestination);
    }

    private void processImages(Document document, Object object) {
        Elements imgElements = document.select("img");

        for (int i = 0; i < imgElements.size(); i++) {
            Element imgElement = imgElements.get(i);
            String src = imgElement.attr("src");

            if (src.startsWith("data:image")) {
                byte[] imageData = parseBase64Data(src);
                String imageUrl = uploadMediaToS3(imageData, getId(object), "jpg");
                imgElement.attr("src", imageUrl);
            }
        }
    }

    private void processVideos(Document document, Object object) {
        Elements videoElements = document.select("video");

        for (int i = 0; i < videoElements.size(); i++) {
            Element videoElement = videoElements.get(i);
            String src = videoElement.attr("src");

            if (src.startsWith("data:video")) {
                byte[] videoData = parseBase64Data(src);
                String videoUrl = uploadMediaToS3(videoData, getId(object), "mp4");
                videoElement.attr("src", videoUrl);
            }
        }
    }

    private byte[] parseBase64Data(String dataUri) {
        String base64Data = dataUri.split(",")[1];
        return Base64.getDecoder().decode(base64Data);
    }

    private String uploadMediaToS3(byte[] mediaData, Long objectId, String fileExtension) {
        try {
            String fileName = "media_" + objectId + "_" + System.currentTimeMillis() + "." + fileExtension;
            return s3Service.uploadMedia(mediaData, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Long getId(Object object) {
        if (object instanceof Post) {
            return ((Post) object).getId();
        } else if (object instanceof Destination) {
            return ((Destination) object).getId();
        } else {
            throw new IllegalArgumentException("지원되지 않는 object type");
        }
    }


    // HTML 내용에서 이미지 URL을 추출하는 메서드
    public String extractImageUrl(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        Element firstImage = document.select("img").first();
        if (firstImage != null) {
            return firstImage.attr("src");
        }
        return null;
    }
}
