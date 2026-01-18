package com.example.fjnuserviceapp.ui.study;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.fjnuserviceapp.db.AppDatabase;
import com.example.fjnuserviceapp.db.CourseDao;
import com.example.fjnuserviceapp.model.Course;
import java.util.List;

public class StudyViewModel extends AndroidViewModel {
    private final CourseDao courseDao;
    private final MutableLiveData<Integer> currentWeek = new MutableLiveData<>(1);
    private final LiveData<List<Course>> courses;

    public StudyViewModel(@NonNull Application application) {
        super(application);
        courseDao = AppDatabase.getDatabase(application).courseDao();

        // 当 currentWeek 变化时，自动重新从数据库查询
        // courses = Transformations.switchMap(currentWeek, week -> courseDao.getCoursesByWeek(week));
        courses = courseDao.getAllCourses();

        // 初始化一些测试数据 (如果为空)
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // 这里可以添加逻辑：如果数据库为空，插入默认数据
        });
    }

    public LiveData<List<Course>> getCourses() {
        return courses;
    }

    public LiveData<Integer> getCurrentWeek() {
        return currentWeek;
    }

    public void setWeek(int week) {
        if (week < 1)
            week = 1;
        if (week > 20)
            week = 20;
        currentWeek.setValue(week);
    }

    public void addCourse(Course c) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.insert(c);
        });
    }

    public void deleteCourse(Course c) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.delete(c);
        });
    }
}
