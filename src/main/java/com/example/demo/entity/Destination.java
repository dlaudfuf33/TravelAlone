package com.example.demo.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.NoArgsConstructor;


@Schema(description = "여행지 정보")
@Entity
@Table(name = "destinations")
@NoArgsConstructor
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "여행지의 고유 ID")
    private Long id;
    @Column
    @Schema(description = "여행지의 이름")
    private String name;
    @Column(length = 500)
    @Schema(description = "여행지 설명 (긴 내용 가능)")
    private String description;
    @Column
    @Schema(description = "여행지 이미지 URL")
    private String imageUrl;
    @Column(columnDefinition = "json")
    @Schema(description = "여행지 특징을 나타내는 JSON 데이터")
    private String features;
    @Column
    @Schema(description = "여행지의 평균 평점")
    private Double averageRating = 0.0;


    // 파라미터 있는 생성자 추가
    public Destination(String name, String features, Double averageRating, String imageUrl, String description) {
        this.name = name;
        this.features = features;
        this.averageRating = averageRating;
        this.imageUrl = imageUrl;
        this.description = description;
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
    public String getFeatures() {
        return features;
    }

    // 여행지의 이미지 URL을 반환합니다.
    public String getImageUrl() {
        return imageUrl;
    }

    // 여행지에 대한 설명을 반환합니다.
    public String getDescription() {
        return description;
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
    public void setFeatures(String features) {
        this.features = features;
    }

    // 여행지의 이미지 URL을 설정합니다.
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 여행지에 대한 설명을 설정합니다.
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", name='" + name + '\'' +
                // 다른 속성들을 추가하여 원하는 형식으로 출력할 수 있습니다.
                '}';
    }
}
