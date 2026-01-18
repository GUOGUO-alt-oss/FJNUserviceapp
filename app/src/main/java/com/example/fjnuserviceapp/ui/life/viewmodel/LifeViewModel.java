package com.example.fjnuserviceapp.ui.life.viewmodel;

import androidx.lifecycle.*;
import com.example.fjnuserviceapp.base.entity.LostAndFound;
import com.example.fjnuserviceapp.base.entity.IdleItem;

import java.util.*;

public class LifeViewModel extends ViewModel {

    /* 失物招领 */
    private final MutableLiveData<List<LostAndFound>> lostAndFound = new MutableLiveData<>();
    public  LiveData<List<LostAndFound>> getLostAndFound() { return lostAndFound; }

    /* 闲置交易 */
    private final MutableLiveData<List<IdleItem>> idleItems = new MutableLiveData<>();
    public  LiveData<List<IdleItem>> getIdleItems() { return idleItems; }

    public LifeViewModel() {
        loadLostAndFound();
        loadIdleItems();
    }

    /* 模拟失物招领 */
    public void loadLostAndFound() {
        List<LostAndFound> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            LostAndFound l = new LostAndFound();
            l.setId(UUID.randomUUID().toString());
            l.setTitle(i % 2 == 0 ? "校园卡" : "耳机");
            l.setDesc("学生活动中心 " + (i % 2 == 0 ? "捡到" : "丢失"));
            l.setCategory(i % 2);
            l.setTime(System.currentTimeMillis() - i * 86_400_000);
            list.add(l);
        }
        lostAndFound.setValue(list);
    }

    /* 模拟闲置商品 */
    public void loadIdleItems() {
        List<IdleItem> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            IdleItem item = new IdleItem();
            item.setId(UUID.randomUUID().toString());
            item.setTitle("二手教材 " + (i + 1));
            item.setDesc("九成新，无笔记");
            item.setPrice(10 + i * 2.5);
            item.setContact("微信：user" + i);
            item.setTime(System.currentTimeMillis() - i * 86_400_000);
            list.add(item);
        }
        idleItems.setValue(list);
    }
}