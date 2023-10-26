package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.converter.FeaturesConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "destinations", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_region", columnList = "region")
})
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "여행지의 고유 ID")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "여행지의 이름")
    private String name;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Schema(description = "여행지의 상세 내용 (HTML 형식)")
    private String contents;

    @Column
    @Schema(description = "여행지의 대표 이미지 URL")
    private String imageUrl;

    @Convert(converter = FeaturesConverter.class)
    @Schema(description = "여행지 특징을 나타내는 해시태그 데이터")
    private List<String> features;

    @Column
    @Schema(description = "여행지의 지역")
    private String region;

    @Column
    @Schema(description = "여행지가 가장 좋은 계절")
    private String bestSeason;

    @Column
    @Schema(description = "평점의 총 합")
    private Double totalRating = 0.0;

    @Column
    @Schema(description = "여행지의 평균 평점")
    private Double averageRating = 0.0;

    @Column
    @Schema(description = "평점을 제공한 사용자 수")
    private Integer ratingCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "레코드의 생성일시")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "레코드의 수정일시")
    private Timestamp updatedAt;

    // 파라미터 있는 생성자 추가
    public Destination(String name, List<String> features, Double averageRating, String imageUrl, String contents) {
        this.name = name;
        this.features = features;
        this.averageRating = averageRating;
        this.imageUrl = imageUrl;
        this.contents = contents;
    }

    // 여행지의 고유 ID를 반환합니다.
    public Long getId() {
        return id;
    }

    // 여행지의 이름을 반환합니다.
    public String getName() {
        return name;
    }

    // 여행지의 특성을 반환합니다.
    public List<String> getFeatures() {
        return features;
    }

    // 여행지의 이미지 URL을 반환합니다.
    public String getImageUrl() {
        return imageUrl;
    }

    // 여행지에 대한 설명을 반환합니다.
    public String getContents() {
        return contents;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    // 여행지의 고유 ID를 설정합니다.
    public void setId(Long id) {
        this.id = id;
    }

    // 여행지의 이름을 설정합니다.
    public void setName(String name) {
        this.name = name;
    }

    // 여행지의 특성을 설정합니다.
    public void setFeatures(List<String> features) {
        this.features = features;
    }

    // 여행지의 이미지 URL을 설정합니다.
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 여행지에 대한 설명을 설정합니다.
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * 새로운 평점을 추가하고 평균 평점을 업데이트합니다.
     * @param newRating 새로운 평점
     */
    public void addRating(Double newRating) {
        this.totalRating += newRating;
        this.ratingCount++;
        this.averageRating = this.totalRating / this.ratingCount;
    }

    /**
     * 기존 평점을 수정하고 평균 평점을 업데이트합니다.
     * @param oldRating 기존 평점
     * @param newRating 새로운 평점
     */
    public void updateRating(Double oldRating, Double newRating) {
        this.totalRating = this.totalRating - oldRating + newRating;
        this.averageRating = this.totalRating / this.ratingCount;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contents='" + contents + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", features=" + features +
                ", region='" + region + '\'' +
                ", bestSeason='" + bestSeason + '\'' +
                ", averageRating=" + averageRating +
                ", ratingCount=" + ratingCount +
                '}';
    }
}
