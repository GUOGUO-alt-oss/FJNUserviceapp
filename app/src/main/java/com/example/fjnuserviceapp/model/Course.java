package com.example.fjnuserviceapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "courses")
public class Course implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String teacher;
    private String location;
    private int dayOfWeek; // 1-7
    private int startSection;
    private int endSection;
    private int startWeek;
    private int endWeek;
    private String color; // Hex color code

    public Course(String name, String teacher, String location, int dayOfWeek, int startSection, int endSection, int startWeek, int endWeek, String color) {
        this.name = name;
        this.teacher = teacher;
        this.location = location;
        this.dayOfWeek = dayOfWeek;
        this.startSection = startSection;
        this.endSection = endSection;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.color = color;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public int getStartSection() { return startSection; }
    public void setStartSection(int startSection) { this.startSection = startSection; }
    public int getEndSection() { return endSection; }
    public void setEndSection(int endSection) { this.endSection = endSection; }
    public int getStartWeek() { return startWeek; }
    public void setStartWeek(int startWeek) { this.startWeek = startWeek; }
    public int getEndWeek() { return endWeek; }
    public void setEndWeek(int endWeek) { this.endWeek = endWeek; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    // Compatibility method
    public String getColorTag() {
        return color != null ? color : "#FF4A90E2";
    }
}