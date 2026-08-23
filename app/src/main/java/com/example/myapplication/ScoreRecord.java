package com.example.myapplication;

import androidx.annotation.NonNull;

public class ScoreRecord implements Comparable<ScoreRecord> {
    private int id;
    private String playerId;
    private int score;
    private String date;

    // 构造函数
    public ScoreRecord() {}

    public ScoreRecord(String playerId, int score, String date) {
        this.playerId = playerId;
        this.score = score;
        this.date = date;
    }

    public ScoreRecord(int id, String playerId, int score, String date) {
        this.id = id;
        this.playerId = playerId;
        this.score = score;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // 按分数降序排序
    @Override
    public int compareTo(ScoreRecord other) {
        return Integer.compare(other.score, this.score);
    }

    @NonNull
    @Override
    public String toString() {
        return playerId + ": " + score + " 分 (" + date + ")";
    }
}