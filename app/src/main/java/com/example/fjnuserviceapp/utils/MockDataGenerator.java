package com.example.fjnuserviceapp.utils;

import com.example.fjnuserviceapp.model.Course;
import com.example.fjnuserviceapp.model.Grade;

import java.util.ArrayList;
import java.util.List;

public class MockDataGenerator {

    // Morandi Colors
    private static final String[] COLORS = {
            "#5B7C99", // Blue
            "#D8B4B8", // Pink
            "#8FA395", // Green
            "#9E9E9E", // Grey
            "#B0A4E3", // Purple
            "#E6A0C4", // Rose
            "#C6E2E9", // Light Blue
            "#F1D4AF"  // Beige
    };

    public static List<Course> getMockCourses() {
        List<Course> courses = new ArrayList<>();
        
        // Monday
        courses.add(new Course("高等数学", "张老师", "理工楼302", 1, 1, 2, 1, 16, COLORS[0]));
        courses.add(new Course("大学英语", "李老师", "文科楼105", 1, 3, 4, 1, 16, COLORS[1]));
        
        // Tuesday
        courses.add(new Course("Java程序设计", "王老师", "计算机楼404", 2, 3, 4, 1, 16, COLORS[2]));
        courses.add(new Course("体育", "赵老师", "体育馆", 2, 5, 6, 1, 16, COLORS[3]));
        
        // Wednesday
        courses.add(new Course("数据结构", "陈老师", "计算机楼202", 3, 1, 2, 1, 16, COLORS[4]));
        courses.add(new Course("马克思主义原理", "刘老师", "公共楼101", 3, 7, 8, 1, 12, COLORS[5]));
        
        // Thursday
        courses.add(new Course("操作系统", "林老师", "计算机楼305", 4, 3, 4, 1, 16, COLORS[6]));
        
        // Friday
        courses.add(new Course("计算机网络", "吴老师", "计算机楼501", 5, 1, 2, 1, 16, COLORS[7]));
        courses.add(new Course("软件工程", "郑老师", "计算机楼402", 5, 5, 6, 1, 16, COLORS[0]));

        return courses;
    }

    public static List<Grade> getMockGrades() {
        List<Grade> grades = new ArrayList<>();
        
        // 2024-2025(1)
        String term1 = "2024-2025(1)";
        grades.add(new Grade("1", "高等数学", term1, 88, 4.0f, 3.7f));
        grades.add(new Grade("2", "大学英语", term1, 92, 3.0f, 4.0f));
        grades.add(new Grade("3", "C语言程序设计", term1, 85, 3.0f, 3.5f));
        
        // 2024-2025(2)
        String term2 = "2024-2025(2)";
        grades.add(new Grade("4", "数据结构", term2, 90, 4.0f, 4.0f));
        grades.add(new Grade("5", "Java程序设计", term2, 95, 3.0f, 4.0f));
        grades.add(new Grade("6", "离散数学", term2, 82, 3.0f, 3.0f));

        return grades;
    }
}