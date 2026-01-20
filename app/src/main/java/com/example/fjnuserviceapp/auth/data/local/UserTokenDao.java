package com.example.fjnuserviceapp.auth.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fjnuserviceapp.auth.data.model.UserToken;

import java.util.List;

@Dao
public interface UserTokenDao {
    @Query("SELECT * FROM user_tokens WHERE token = :token LIMIT 1")
    UserToken getTokenByToken(String token);

    @Query("SELECT * FROM user_tokens WHERE user_id = :userId")
    List<UserToken> getTokensByUserId(long userId);

    @Query("SELECT * FROM user_tokens WHERE user_id = :userId AND device_type = :deviceType LIMIT 1")
    UserToken getTokenByUserAndDevice(long userId, String deviceType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserToken token);

    @Update
    void update(UserToken token);

    @Query("DELETE FROM user_tokens WHERE token = :token")
    void deleteByToken(String token);

    @Query("DELETE FROM user_tokens WHERE user_id = :userId")
    void deleteByUserId(long userId);

    @Query("DELETE FROM user_tokens WHERE expires_at < :currentTime")
    void deleteExpiredTokens(long currentTime);
}
