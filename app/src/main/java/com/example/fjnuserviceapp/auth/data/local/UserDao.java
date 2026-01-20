package com.example.fjnuserviceapp.auth.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fjnuserviceapp.auth.data.model.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    User getUserByPhone(String phone);

    @Query("SELECT * FROM users WHERE student_id = :studentId LIMIT 1")
    User getUserByStudentId(String studentId);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(long userId);

    @Query("SELECT * FROM users WHERE phone = :account OR student_id = :account OR email = :account LIMIT 1")
    User getUserByAccount(String account);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Update
    void update(User user);

    @Query("UPDATE users SET last_login_at = :loginAt, login_count = login_count + 1 WHERE id = :userId")
    void updateLoginInfo(long userId, long loginAt);

    @Query("UPDATE users SET status = :status WHERE id = :userId")
    void updateStatus(long userId, int status);

    @Query("DELETE FROM users WHERE id = :userId")
    void deleteById(long userId);
}
