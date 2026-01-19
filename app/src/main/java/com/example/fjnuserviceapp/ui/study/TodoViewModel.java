package com.example.fjnuserviceapp.ui.study;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.fjnuserviceapp.db.AppDatabase;
import com.example.fjnuserviceapp.db.TodoDao;
import com.example.fjnuserviceapp.model.TodoItem;

import com.example.fjnuserviceapp.utils.MockDataGenerator;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private final TodoDao todoDao;
    private final LiveData<List<TodoItem>> pendingTodos;
    private final LiveData<List<TodoItem>> doneTodos;
    private final LiveData<TodoItem> activeTodo;

    // Status Card Data
    private final MutableLiveData<Integer> todayFocusMinutes = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> consecutiveDays = new MutableLiveData<>(0);

    public TodoViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        todoDao = db.todoDao();

        pendingTodos = todoDao.getPendingTodos();
        doneTodos = todoDao.getDoneTodos();
        activeTodo = todoDao.getActiveTodo();

        // Init Mock Data for Status
        todayFocusMinutes.setValue(38);
        consecutiveDays.setValue(3);

        // Init DB with Mock Data if empty
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (todoDao.getActiveTodoSync() == null &&
                    (todoDao.getPendingTodos().getValue() == null || todoDao.getPendingTodos().getValue().isEmpty())) {
                List<TodoItem> mocks = MockDataGenerator.getMockTodos();
                for (TodoItem item : mocks) {
                    todoDao.insert(item);
                }
            }
        });
    }

    public LiveData<List<TodoItem>> getPendingTodos() {
        return pendingTodos;
    }

    public LiveData<List<TodoItem>> getDoneTodos() {
        return doneTodos;
    }

    public LiveData<TodoItem> getActiveTodo() {
        return activeTodo;
    }

    public LiveData<Integer> getTodayFocusMinutes() {
        return todayFocusMinutes;
    }

    public LiveData<Integer> getConsecutiveDays() {
        return consecutiveDays;
    }

    public void addTodo(String title, String subtitle, int duration, boolean startImmediately) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Check if we need to pause current active one
            if (startImmediately) {
                TodoItem currentActive = todoDao.getActiveTodoSync();
                if (currentActive != null) {
                    currentActive.setState(TodoItem.STATE_PENDING);
                    todoDao.update(currentActive);
                }
            }

            TodoItem newItem = new TodoItem(title, subtitle, duration,
                    startImmediately ? TodoItem.STATE_ACTIVE : TodoItem.STATE_PENDING,
                    System.currentTimeMillis());
            todoDao.insert(newItem);
        });
    }

    public void startTodo(TodoItem todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Pause current active
            TodoItem currentActive = todoDao.getActiveTodoSync();
            if (currentActive != null) {
                currentActive.setState(TodoItem.STATE_PENDING);
                todoDao.update(currentActive);
            }

            // Start new one
            todo.setState(TodoItem.STATE_ACTIVE);
            todoDao.update(todo);
        });
    }

    public void pauseTodo(TodoItem todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todo.setState(TodoItem.STATE_PENDING);
            todoDao.update(todo);
        });
    }

    public void completeTodo(TodoItem todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todo.setState(TodoItem.STATE_DONE);
            todo.setCompletedDuration(todo.getTotalDuration()); // Assume full completion
            todoDao.update(todo);

            // Update stats (simple mock logic)
            // In real app, we should accumulate this in a separate Stats table
            // todayFocusMinutes.postValue(todayFocusMinutes.getValue() +
            // todo.getTotalDuration());
        });
    }

    // For updating progress (Mock: In real app, use a Timer/Service)
    public void updateProgress(TodoItem todo, int newCompletedDuration) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todo.setCompletedDuration(newCompletedDuration);
            todoDao.update(todo);
        });
    }
}
