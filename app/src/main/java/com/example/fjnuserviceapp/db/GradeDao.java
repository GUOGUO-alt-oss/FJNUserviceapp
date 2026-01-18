package com.example.fjnuserviceapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.fjnuserviceapp.model.Grade;
import java.util.List;

@Dao
public interface GradeDao {
    @Query("SELECT * FROM grades ORDER BY term DESC")
    LiveData<List<Grade>> getAllGrades();

    @Query("SELECT * FROM grades WHERE term = :term")
    LiveData<List<Grade>> getGradesByTerm(String term);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Grade grade);

    @Delete
    void delete(Grade grade);
}
