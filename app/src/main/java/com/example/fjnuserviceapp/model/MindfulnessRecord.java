package com.example.fjnuserviceapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mindfulness_records")
public class MindfulnessRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String type; // "呼吸练习", "身体扫描", "正念冥想"
    private int durationMinutes; // 练习时长
    private long timestamp; // 完成时间

    public MindfulnessRecord(String type, int durationMinutes, long timestamp) {
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
