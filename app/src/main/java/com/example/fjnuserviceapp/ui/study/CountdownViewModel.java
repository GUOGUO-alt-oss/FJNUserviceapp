package com.example.fjnuserviceapp.ui.study;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.fjnuserviceapp.db.AppDatabase;
import com.example.fjnuserviceapp.db.CountdownDao;
import com.example.fjnuserviceapp.model.CountdownEvent;
import com.example.fjnuserviceapp.utils.MockDataGenerator;

import java.util.List;

public class CountdownViewModel extends AndroidViewModel {
    private final CountdownDao countdownDao;
    private final LiveData<List<CountdownEvent>> allCountdowns;
    private final LiveData<CountdownEvent> topEvent;

    public CountdownViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        countdownDao = db.countdownDao();
        allCountdowns = countdownDao.getAllCountdowns();

        // Automatically pick the first one as top event (closest date)
        topEvent = Transformations.map(allCountdowns, list -> {
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
            return null;
        });

        // Init Mock Data Logic
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // We can't easily check count without a DAO method returning int,
            // but we can just let the user add data or use the "initData" method triggered
            // by UI once
        });
    }

    public void checkAndInitMocks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int count = countdownDao.getCount();
            if (count == 0) {
                List<CountdownEvent> mocks = MockDataGenerator.getMockCountdowns();
                for (CountdownEvent e : mocks) {
                    countdownDao.insert(e);
                }
            }
        });
    }

    public void injectMocks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<CountdownEvent> mocks = MockDataGenerator.getMockCountdowns();
            for (CountdownEvent e : mocks) {
                countdownDao.insert(e);
            }
        });
    }

    public LiveData<List<CountdownEvent>> getAllCountdowns() {
        return allCountdowns;
    }

    public LiveData<CountdownEvent> getTopEvent() {
        return topEvent;
    }

    public void addCountdown(String title, long targetDate, int type, String motivation) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            CountdownEvent event = new CountdownEvent(title, targetDate, type, motivation);
            countdownDao.insert(event);
        });
    }

    public void deleteCountdown(CountdownEvent event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            countdownDao.delete(event);
        });
    }
}
