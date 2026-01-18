package com.example.fjnuserviceapp.ui.study;

import android.content.Context;
import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.fjnuserviceapp.model.Course;

public class CourseEditViewModel extends ViewModel {
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> teacher = new MutableLiveData<>();
    private final MutableLiveData<String> time = new MutableLiveData<>();
    private final MutableLiveData<String> location = new MutableLiveData<>();

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getTeacher() {
        return teacher;
    }

    public LiveData<String> getTime() {
        return time;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public void parseFromUri(Context context, Uri uri) {
        // 暂不支持 PDF 解析，功能开发中
        // String text = PdfParserUtil.readTextOrExtractFromPdf(context, uri);
        // Course c = PdfParserUtil.parseCourseFromText(text);
        // if (c != null) {
        // name.setValue(c.getName());
        // teacher.setValue(c.getTeacher());
        // time.setValue(c.getTime());
        // location.setValue(c.getLocation());
        // }
    }

    public Course buildCourse() {
        String n = valueOrEmpty(name.getValue());
        String t = valueOrEmpty(teacher.getValue());
        String ti = valueOrEmpty(time.getValue());
        String l = valueOrEmpty(location.getValue());
        // 默认 day=1, section=1, startWeek=1, endWeek=16, color=默认蓝
        return new Course(n, t, l, 1, 1, 2, 1, 16, "#FF4A90E2");
    }

    public void setName(String v) {
        name.setValue(v);
    }

    public void setTeacher(String v) {
        teacher.setValue(v);
    }

    public void setTime(String v) {
        time.setValue(v);
    }

    public void setLocation(String v) {
        location.setValue(v);
    }

    private String valueOrEmpty(String v) {
        return v == null ? "" : v;
    }
}
