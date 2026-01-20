package com.example.fjnuserviceapp.utils;

public class EmotionDataMapper {

    public enum DataType {
        GPA,
        COURSE_COUNT,
        TODAY_COURSE,
        TERM
    }

    public static String getLabelFor(DataType type, float value, String stringValue) {
        switch (type) {
            case GPA:
                if (value == 0)
                    return "一切从零开始，也挺酷";
                if (value < 2.0)
                    return "加油，逆风翻盘更帅";
                if (value < 3.0)
                    return "稳步前行，不必焦虑";
                if (value < 4.0)
                    return "保持优秀，从容不迫";
                return "巅峰寂寞，但风景独好";

            case COURSE_COUNT:
                int count = (int) value;
                if (count == 0)
                    return "空白，是最自由的阶段";
                if (count < 5)
                    return "轻装上阵，专注自我";
                if (count < 10)
                    return "充实，是成长的养料";
                return "博学多才，你是最棒的";

            case TODAY_COURSE:
                int today = (int) value;
                if (today == 0)
                    return "今天，留给自己";
                if (today <= 2)
                    return "轻松的一天，去晒晒太阳";
                if (today <= 4)
                    return "忙碌充实，记得喝水";
                return "满课的一天，你是战士";

            case TERM:
                return "新一轮人生副本";

            default:
                return "保持热爱";
        }
    }
}
