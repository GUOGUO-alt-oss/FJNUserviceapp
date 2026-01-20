package com.example.fjnuserviceapp.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.fjnuserviceapp.model.Course;
import com.example.fjnuserviceapp.model.Grade;
import com.example.fjnuserviceapp.model.UserProfile;
import com.example.fjnuserviceapp.model.TodoItem;
import com.example.fjnuserviceapp.model.CountdownEvent;
import com.example.fjnuserviceapp.model.MindfulnessRecord;
import com.example.fjnuserviceapp.auth.data.model.User;
import com.example.fjnuserviceapp.auth.data.model.UserToken;
import com.example.fjnuserviceapp.auth.data.model.VerificationCode;
import com.example.fjnuserviceapp.auth.data.local.UserTokenDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = { Course.class, Grade.class, UserProfile.class, TodoItem.class, CountdownEvent.class,
        MindfulnessRecord.class, User.class, UserToken.class, VerificationCode.class }, version = 8, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();

    public abstract GradeDao gradeDao();

    public abstract UserDao userDao();
    
    public abstract com.example.fjnuserviceapp.auth.data.local.UserDao authUserDao();
    
    public abstract UserTokenDao userTokenDao();

    public abstract TodoDao todoDao();

    public abstract CountdownDao countdownDao();

    public abstract MindfulnessDao mindfulnessDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
