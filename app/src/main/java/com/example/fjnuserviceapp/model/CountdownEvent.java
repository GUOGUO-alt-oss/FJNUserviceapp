package com.example.fjnuserviceapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "countdowns")
public class CountdownEvent {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private long targetDate; // Timestamp
    private int type; // 0: Exam, 1: Homework, 2: Holiday, 3: Other
    private boolean isTop; // Pinned to header (optional logic)
    private String motivation; // 激励语

    public static final int TYPE_EXAM = 0;
    public static final int TYPE_HOMEWORK = 1;
    public static final int TYPE_HOLIDAY = 2;
    public static final int TYPE_OTHER = 3;

    public CountdownEvent(String title, long targetDate, int type, String motivation) {
        this.title = title;
        this.targetDate = targetDate;
        this.type = type;
        this.motivation = motivation;
        this.isTop = false;
    }

    // Constructor overload for backward compatibility or simpler creation
    @Ignore
    public CountdownEvent(String title, long targetDate, int type) {
        this(title, targetDate, type, "稳扎稳打，每天一点进步");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(long targetDate) {
        this.targetDate = targetDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public int getDaysRemaining() {
        long diff = targetDate - System.currentTimeMillis();
        return (int) (diff / (1000 * 60 * 60 * 24)) + 1; // +1 to include today roughly
    }
}
