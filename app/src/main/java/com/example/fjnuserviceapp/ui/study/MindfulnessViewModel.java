package com.example.fjnuserviceapp.ui.study;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.fjnuserviceapp.db.AppDatabase;
import com.example.fjnuserviceapp.db.MindfulnessDao;
import com.example.fjnuserviceapp.model.MindfulnessRecord;
import java.util.List;

public class MindfulnessViewModel extends AndroidViewModel {
    private final MindfulnessDao dao;
    private final LiveData<List<MindfulnessRecord>> records;
    private final LiveData<Integer> totalDuration;
    private final LiveData<Integer> totalCount;

    public MindfulnessViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.mindfulnessDao();
        records = dao.getAllRecords();
        totalDuration = dao.getTotalDuration();
        totalCount = dao.getTotalCount();
    }

    public LiveData<List<MindfulnessRecord>> getRecords() {
        return records;
    }

    public LiveData<Integer> getTotalDuration() {
        return totalDuration;
    }

    public LiveData<Integer> getTotalCount() {
        return totalCount;
    }

    public void addRecord(String type, int duration) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(new MindfulnessRecord(type, duration, System.currentTimeMillis()));
        });
    }
}
