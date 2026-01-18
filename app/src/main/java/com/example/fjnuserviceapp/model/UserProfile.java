package com.example.fjnuserviceapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "user_profile")
public class UserProfile implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String studentId;
    private String department; // 学院
    private String major;      // 专业
    private String signature;  // 个人签名/状态
    private String avatarUrl;  // 头像路径
    private String phone;      // 联系电话

    public UserProfile() {
    }

    @Ignore
    public UserProfile(String name, String studentId, String department, String major) {
        this.name = name;
        this.studentId = studentId;
        this.department = department;
        this.major = major;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}