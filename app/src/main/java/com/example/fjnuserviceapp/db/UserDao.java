package com.example.fjnuserviceapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.fjnuserviceapp.model.UserProfile;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_profile LIMIT 1")
    LiveData<UserProfile> getUserProfile();

    @Query("SELECT * FROM user_profile LIMIT 1")
    UserProfile getUserProfileSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserProfile profile);

    @Update
    void update(UserProfile profile);
    
    @Query("DELETE FROM user_profile")
    void deleteAll();
}
