package com.example.fjnuserviceapp.ui.study;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.fjnuserviceapp.db.AppDatabase;
import com.example.fjnuserviceapp.db.GradeDao;
import com.example.fjnuserviceapp.model.Grade;
import java.util.List;

public class GradeViewModel extends AndroidViewModel {
    private final GradeDao gradeDao;
    private final LiveData<List<Grade>> grades;
    private final MutableLiveData<String> currentTerm = new MutableLiveData<>(null);

    public GradeViewModel(@NonNull Application application) {
        super(application);
        gradeDao = AppDatabase.getDatabase(application).gradeDao();

        grades = Transformations.switchMap(currentTerm, term -> {
            if (term == null || "全部学期".equals(term)) {
                return gradeDao.getAllGrades();
            } else {
                return gradeDao.getGradesByTerm(term);
            }
        });
    }

    public LiveData<List<Grade>> getGrades() {
        return grades;
    }

    public void setTerm(String term) {
        currentTerm.setValue(term);
    }

    public void addGrade(Grade grade) {
        AppDatabase.databaseWriteExecutor.execute(() -> gradeDao.insert(grade));
    }
}
