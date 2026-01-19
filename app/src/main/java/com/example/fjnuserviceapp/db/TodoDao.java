package com.example.fjnuserviceapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fjnuserviceapp.model.TodoItem;

import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM todos WHERE state = 0 ORDER BY createdTime DESC") // PENDING
    LiveData<List<TodoItem>> getPendingTodos();

    @Query("SELECT * FROM todos WHERE state = 2 ORDER BY createdTime DESC") // DONE
    LiveData<List<TodoItem>> getDoneTodos();

    @Query("SELECT * FROM todos WHERE state = 1 LIMIT 1") // ACTIVE
    LiveData<TodoItem> getActiveTodo();

    @Query("SELECT * FROM todos WHERE state = 1 LIMIT 1") // ACTIVE (Synchronous for checking)
    TodoItem getActiveTodoSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TodoItem todo);

    @Update
    void update(TodoItem todo);

    @Delete
    void delete(TodoItem todo);
}
