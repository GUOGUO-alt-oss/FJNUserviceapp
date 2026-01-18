package com.example.fjnuserviceapp.utils;

import com.example.fjnuserviceapp.model.Notification;
import java.util.ArrayList;
import java.util.List;

public class NotificationMockData {

    public static List<Notification> getCollegeNotifications() {
        List<Notification> list = new ArrayList<>();
        list.add(new Notification("1", "关于2025年寒假放假的通知", "全校师生：根据校历安排，现将2025年寒假放假有关事项通知如下...", "2025-01-10", "教务处", Notification.TYPE_COLLEGE));
        list.add(new Notification("2", "2024-2025学年第一学期期末考试安排", "请各位同学登录教务系统查看具体的考试时间和地点...", "2024-12-25", "计算机学院", Notification.TYPE_COLLEGE));
        list.add(new Notification("3", "关于开展2025年大学生创新创业训练计划项目申报的通知", "各学院：为深化高校创新创业教育改革...", "2024-12-20", "创新创业中心", Notification.TYPE_COLLEGE));
        return list;
    }

    public static List<Notification> getChatMessages() {
        List<Notification> list = new ArrayList<>();
        list.add(new Notification("4", "辅导员", "收到，记得按时提交综测材料。", "10:30", "辅导员", Notification.TYPE_CHAT));
        list.add(new Notification("5", "张三", "下午去图书馆吗？", "昨天", "张三", Notification.TYPE_CHAT));
        list.add(new Notification("6", "李四", "项目代码我已经推送到Git了。", "星期一", "李四", Notification.TYPE_CHAT));
        return list;
    }

    public static List<Notification> getSystemMessages() {
        List<Notification> list = new ArrayList<>();
        list.add(new Notification("7", "系统维护通知", "系统将于今晚23:00进行例行维护，预计耗时2小时。", "2025-01-15", "系统管理员", Notification.TYPE_SYSTEM));
        list.add(new Notification("8", "账户安全提醒", "您的账户于异地登录，请确认是否为本人操作。", "2025-01-12", "安全中心", Notification.TYPE_SYSTEM));
        return list;
    }
}