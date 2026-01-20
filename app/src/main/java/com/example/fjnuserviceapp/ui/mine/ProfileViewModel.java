package com.example.fjnuserviceapp.ui.mine;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.fjnuserviceapp.db.AppDatabase;
import com.example.fjnuserviceapp.db.CourseDao;
import com.example.fjnuserviceapp.db.GradeDao;
import com.example.fjnuserviceapp.db.UserDao;
import com.example.fjnuserviceapp.model.Course;
import com.example.fjnuserviceapp.model.Grade;
import com.example.fjnuserviceapp.model.UserProfile;
import java.util.Calendar;
import java.util.List;

import com.example.fjnuserviceapp.ui.mine.engine.IdentityEngine;

public class ProfileViewModel extends AndroidViewModel {
    private final UserDao userDao;
    private final CourseDao courseDao;
    private final GradeDao gradeDao;

    private final LiveData<UserProfile> userProfile;
    private final MediatorLiveData<ProfileStats> stats = new MediatorLiveData<>();
    private final IdentityEngine identityEngine; // Add Engine

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        courseDao = db.courseDao();
        gradeDao = db.gradeDao();

        identityEngine = IdentityEngine.getInstance(); // Get Instance

        userProfile = userDao.getUserProfile();

        // 聚合计算统计数据
        LiveData<List<Course>> courses = courseDao.getAllCourses();
        LiveData<List<Grade>> grades = gradeDao.getAllGrades();

        stats.addSource(courses, c -> calculateStats(c, grades.getValue()));
        stats.addSource(grades, g -> calculateStats(courses.getValue(), g));

        // 初始化默认用户 (如果数据库为空)
        initializeDefaultUser();
    }

    public LiveData<IdentityEngine.IdentityState> getIdentityState() {
        return identityEngine.getIdentityState();
    }

    public void switchIdentityMode(IdentityEngine.IdentityMode mode) {
        identityEngine.switchMode(mode);
    }

    private void initializeDefaultUser() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (userDao.getUserProfileSync() == null) {
                // 插入示例数据
                userDao.insert(new UserProfile("浦颖昊", "22010101", "计算机学院", "软件工程"));
            }
        });
    }

    public void updateUser(UserProfile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.update(profile);
        });
    }

    private void calculateStats(List<Course> courses, List<Grade> grades) {
        ProfileStats current = new ProfileStats();

        // 1. 课程数
        if (courses != null) {
            current.courseCount = courses.size();

            // 计算今日课程
            int todayWeekDay = getTodayWeekDay();
            long todayCount = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                todayCount = courses.stream().filter(c -> c.getDayOfWeek() == todayWeekDay).count();
            } else {
                for (Course c : courses) {
                    if (c.getDayOfWeek() == todayWeekDay)
                        todayCount++;
                }
            }
            current.todayCourseCount = (int) todayCount;
        }

        // 2. GPA & 学分
        if (grades != null) {
            float totalPoint = 0;
            float totalCredit = 0;
            for (Grade g : grades) {
                try {
                    float s = g.getScore();
                    if (s >= 60) {
                        totalPoint += g.getGradePoint() * g.getCredit();
                        totalCredit += g.getCredit();
                    }
                } catch (Exception e) {
                }
            }
            current.gpa = totalCredit > 0 ? totalPoint / totalCredit : 0;
            current.creditCount = totalCredit;
        }

        current.termName = "2025-2026(2)"; // 假定当前学期
        stats.setValue(current);
    }

    private int getTodayWeekDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // Calendar: Sun=1, Mon=2 ... Sat=7
        // App: Mon=1 ... Sun=7
        if (day == Calendar.SUNDAY)
            return 7;
        return day - 1;
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<ProfileStats> getStats() {
        return stats;
    }

    public static class ProfileStats {
        public int courseCount = 0;
        public int todayCourseCount = 0;
        public float gpa = 0.0f;
        public float creditCount = 0.0f;
        public String termName = "2025-2026(2)";
    }
}