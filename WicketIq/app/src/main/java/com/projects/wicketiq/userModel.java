package com.projects.wicketiq;

public class userModel {

    private String email;
    private int totalScore;


    public userModel() { }

    public userModel(String email, int totalScore) {
        this.email = email;
        this.totalScore = totalScore;
    }

    public String getEmail() {
        return email;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
