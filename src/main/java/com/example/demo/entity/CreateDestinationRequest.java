package com.example.demo.entity;

import java.util.List;

public class CreateDestinationRequest {
    private String name;
    private String authorName;
    private String password;
    private String region;
    private String contents;
    private Double userRating;
    private String recommendedSeason;

    private String imageUrl;

    private String featuresString;

    private List<String> features;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public String getRecommendedSeason() {
        return recommendedSeason;
    }

    public void setRecommendedSeason(String recommendedSeason) {
        this.recommendedSeason = recommendedSeason;
    }

    public List<String> getFeatures() {return features;
    }
    public void setFeatures(List<String> features) {this.features=features;}

    public String getFeaturesString() {return featuresString;
    }
    public void setFeaturesString(String featuresString) {this.featuresString=featuresString;}

    public String getAuthor() {
        return this.authorName;
    }

    public Object getPassword() {
        return this.password;
    }


}
