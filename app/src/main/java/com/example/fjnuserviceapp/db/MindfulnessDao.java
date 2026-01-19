package com.example.fjnuserviceapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.fjnuserviceapp.model.MindfulnessRecord;
import java.util.List;

@Dao
public interface MindfulnessDao {
    @Insert
    void insert(MindfulnessRecord record);

    @Query("SELECT * FROM mindfulness_records ORDER BY timestamp DESC")
    LiveData<List<MindfulnessRecord>> getAllRecords();

    @Query("SELECT SUM(durationMinutes) FROM mindfulness_records")
    LiveData<Integer> getTotalDuration();

    @Query("SELECT COUNT(*) FROM mindfulness_records")
    LiveData<Integer> getTotalCount();
}
