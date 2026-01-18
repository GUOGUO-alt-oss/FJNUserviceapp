package com.example.fjnuserviceapp.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String courseName;
    public String classroom;
    public String teacher;
    public int dayOfWeek; // 1-7 (Mon-Sun)
    public int startPeriod; // 1-12
    public int endPeriod; // 1-12
    public String weeks; // e.g., "1-16"
    public String colorTag; // Hex color code
    public String remark; // Exam, Homework
    public int source; // 0: Manual, 1: Imported

    public Course(String courseName, String classroom, String teacher, int dayOfWeek, int startPeriod, int endPeriod, String weeks, String colorTag) {
        this.courseName = courseName;
        this.classroom = classroom;
        this.teacher = teacher;
        this.dayOfWeek = dayOfWeek;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.weeks = weeks;
        this.colorTag = colorTag;
        this.source = 0;
    }
}
