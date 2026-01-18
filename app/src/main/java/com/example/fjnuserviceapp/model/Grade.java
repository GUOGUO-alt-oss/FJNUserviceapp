package com.example.fjnuserviceapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "grades")
public class Grade implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String courseName;
    private String term; // e.g., "2025-2026(1)"
    private float score;
    private float credit;
    private float gradePoint; // e.g., 4.0

    public Grade(@NonNull String id, String courseName, String term, float score, float credit, float gradePoint) {
        this.id = id;
        this.courseName = courseName;
        this.term = term;
        this.score = score;
        this.credit = credit;
        this.gradePoint = gradePoint;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public float getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(float gradePoint) {
        this.gradePoint = gradePoint;
    }
}