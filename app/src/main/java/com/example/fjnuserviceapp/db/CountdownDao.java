package com.example.fjnuserviceapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fjnuserviceapp.model.CountdownEvent;

import java.util.List;

@Dao
public interface CountdownDao {
    @Query("SELECT * FROM countdowns ORDER BY targetDate ASC")
    LiveData<List<CountdownEvent>> getAllCountdowns();

    @Query("SELECT COUNT(*) FROM countdowns")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CountdownEvent event);

    @Update
    void update(CountdownEvent event);

    @Delete
    void delete(CountdownEvent event);
}
