package com.example.jua_kaligo;

public class ModelReview {

    String uid,ratings,review,timestamp;

    public ModelReview() {
    }

    public ModelReview(String uid, String ratings, String review, String timestamp) {
        this.uid = uid;
        this.ratings = ratings;
        this.review = review;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
