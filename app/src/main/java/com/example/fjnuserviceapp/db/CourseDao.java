package com.example.fjnuserviceapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.fjnuserviceapp.model.Course;
import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getAllCourses();

    // 暂时移除 startWeek/endWeek 条件，因为 Course 实体中尚未添加这两个字段
    // 后续需要更新 Course 实体并迁移数据库
    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getCoursesByWeek(); // 移除未使用参数 week

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Course> courses);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);
    
    @Query("DELETE FROM courses")
    void deleteAll();
}
