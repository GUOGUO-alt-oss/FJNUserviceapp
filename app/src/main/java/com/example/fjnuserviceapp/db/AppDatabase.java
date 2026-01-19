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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = { Course.class, Grade.class, UserProfile.class, TodoItem.class, CountdownEvent.class,
        MindfulnessRecord.class }, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();

    public abstract GradeDao gradeDao();

    public abstract UserDao userDao();

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
