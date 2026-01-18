package com.example.fjnuserviceapp.ui.nav.utils;

import android.content.Context;

import com.example.fjnuserviceapp.ui.nav.model.LatLngPoint;
import com.example.fjnuserviceapp.ui.nav.model.PoiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试数据工具类，用于加载校园POI数据
 */
public class TestDataUtil {

    /**
     * 加载校园POI数据
     * @param context 上下文
     * @return POI信息列表
     */
    public static List<PoiInfo> loadCampusPOIs(Context context) {
        List<PoiInfo> poiList = new ArrayList<>();

        // 福师大旗山校区主要建筑和地点
        poiList.add(new PoiInfo(
                "1",
                "图书馆",
                "旗山校区图书馆",
                new LatLngPoint(26.0245, 119.2108),
                "学习场所",
                "福建师范大学旗山校区图书馆，提供丰富的图书资源和学习空间"
        ));

        poiList.add(new PoiInfo(
                "2",
                "教学楼1号楼",
                "旗山校区教学楼1号楼",
                new LatLngPoint(26.0251, 119.2106),
                "教学场所",
                "主要教学楼之一，包含多个教室和实验室"
        ));

        poiList.add(new PoiInfo(
                "3",
                "学生食堂",
                "旗山校区第一学生食堂",
                new LatLngPoint(26.0238, 119.2095),
                "餐饮场所",
                "提供各种美食的学生食堂，价格实惠"
        ));

        poiList.add(new PoiInfo(
                "4",
                "田径场",
                "旗山校区田径场",
                new LatLngPoint(26.0225, 119.2115),
                "运动场所",
                "标准田径场，配备跑道和足球场"
        ));

        poiList.add(new PoiInfo(
                "5",
                "体育馆",
                "旗山校区体育馆",
                new LatLngPoint(26.0228, 119.2128),
                "运动场所",
                "大型体育馆，可举办各种体育赛事和活动"
        ));

        poiList.add(new PoiInfo(
                "6",
                "行政楼",
                "旗山校区行政楼",
                new LatLngPoint(26.0265, 119.2118),
                "办公场所",
                "学校行政办公大楼"
        ));

        poiList.add(new PoiInfo(
                "7",
                "学生公寓1区",
                "旗山校区学生公寓1区",
                new LatLngPoint(26.0218, 119.2085),
                "住宿场所",
                "学生宿舍区，提供舒适的住宿环境"
        ));

        poiList.add(new PoiInfo(
                "8",
                "校医院",
                "旗山校区校医院",
                new LatLngPoint(26.0275, 119.2102),
                "医疗场所",
                "为师生提供基本医疗服务"
        ));

        poiList.add(new PoiInfo(
                "9",
                "实验楼",
                "旗山校区实验楼",
                new LatLngPoint(26.0262, 119.2095),
                "教学场所",
                "各种专业实验室集中地"
        ));

        poiList.add(new PoiInfo(
                "10",
                "创业园",
                "旗山校区大学生创业园",
                new LatLngPoint(26.0242, 119.2125),
                "创业场所",
                "为大学生创业提供支持和服务的平台"
        ));

        return poiList;
    }
}