package com.example.fjnuserviceapp.auth.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "verification_codes", indices = {
        @Index("phone"),
        @Index("code")
})
public class VerificationCode {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String phone;
    private String email;
    private String code;
    private int type; // 1:注册, 2:登录, 3:找回密码
    private int status; // 0:未使用, 1:已使用, 2:已过期
    
    @ColumnInfo(name = "expires_at")
    private long expiresAt;
    
    @ColumnInfo(name = "created_at")
    private long createdAt;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
