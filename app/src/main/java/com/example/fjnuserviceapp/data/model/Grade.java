package com.example.fjnuserviceapp.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grades")
public class Grade {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String semester; // e.g., "2025-2026-1"
    public String courseName;
    public float score; // 0-100
    public float credit; // e.g., 2.0, 3.0
    public float gpa; // e.g., 4.0 scale
    public boolean isPass;

    public Grade(String semester, String courseName, float score, float credit, float gpa, boolean isPass) {
        this.semester = semester;
        this.courseName = courseName;
        this.score = score;
        this.credit = credit;
        this.gpa = gpa;
        this.isPass = isPass;
    }
}
