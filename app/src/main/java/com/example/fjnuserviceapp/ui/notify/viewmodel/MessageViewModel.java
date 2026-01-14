package com.example.fjnuserviceapp.ui.notify.viewmodel;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.fjnuserviceapp.base.entity.BaseMessage;
import com.example.fjnuserviceapp.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MessageViewModel extends ViewModel {
    public MutableLiveData<List<BaseMessage>> collegeNotifyList = new MutableLiveData<>();
    public MutableLiveData<List<BaseMessage>> privateChatList = new MutableLiveData<>();
    public MutableLiveData<List<BaseMessage>> messageCenterList = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    // ========== 核心修改：Mock学院通知数据 ==========
    public void getCollegeNotify(String userId, Context context) {
        // 1. 模拟加载状态
        isLoading.setValue(true);

        // 2. 模拟网络请求延迟（1.5秒）
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // 3. 构造Mock数据（和真实接口返回格式完全一致）
                List<BaseMessage> mockData = new ArrayList<>();

                // 第一条通知
                BaseMessage msg1 = new BaseMessage();
                msg1.setId("1");
                msg1.setType(1); // 学院通知类型
                msg1.setTitle("关于2026年春季学期选课的通知");
                msg1.setContent("各位同学：2026年春季学期选课将于2月1日开始，截止时间为2月10日，请大家按时完成选课，逾期不予处理。");
                msg1.setSender("教务处");
                msg1.setTime(System.currentTimeMillis() - 86400000); // 昨天的时间
                msg1.setRead(false);

                // 第二条通知
                BaseMessage msg2 = new BaseMessage();
                msg2.setId("2");
                msg2.setType(1);
                msg2.setTitle("关于开展校园安全排查的通知");
                msg2.setContent("为保障校园安全，本周三（1月8日）将对宿舍进行安全排查，请各位同学配合整理宿舍，禁止使用违规电器。");
                msg2.setSender("学生处");
                msg2.setTime(System.currentTimeMillis() - 172800000); // 前天的时间
                msg2.setRead(true);

                // 第三条通知
                BaseMessage msg3 = new BaseMessage();
                msg3.setId("3");
                msg3.setType(1);
                msg3.setTitle("2026届毕业生论文答辩安排");
                msg3.setContent("2026届毕业生论文答辩将于3月15日-3月25日进行，请各位毕业生提前准备答辩PPT，联系指导老师确认答辩时间。");
                msg3.setSender("研究生院");
                msg3.setTime(System.currentTimeMillis() - 259200000); // 三天前的时间
                msg3.setRead(false);

                mockData.add(msg1);
                mockData.add(msg2);
                mockData.add(msg3);

                // 4. 通知UI更新
                collegeNotifyList.postValue(mockData);
                isLoading.postValue(false);
            }
        }, 1500); // 延迟1.5秒，模拟网络请求
    }

    // ========== Mock私信数据 ==========
    public void getPrivateChat(String userId, Context context) {
        isLoading.setValue(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                List<BaseMessage> mockData = new ArrayList<>();

                BaseMessage chat1 = new BaseMessage();
                chat1.setId("101");
                chat1.setType(2); // 私信类型
                chat1.setTitle(""); // 私信无标题
                chat1.setContent("小明，你的作业我已经批改好了，有几个地方需要修改，明天来我办公室一趟。");
                chat1.setSender("李老师");
                chat1.setTime(System.currentTimeMillis() - 3600000); // 1小时前
                chat1.setRead(false);

                BaseMessage chat2 = new BaseMessage();
                chat2.setId("102");
                chat2.setType(2);
                chat2.setTitle("");
                chat2.setContent("周末一起去图书馆复习吧？");
                chat2.setSender("小张");
                chat2.setTime(System.currentTimeMillis() - 7200000); // 2小时前
                chat2.setRead(true);

                mockData.add(chat1);
                mockData.add(chat2);

                privateChatList.postValue(mockData);
                isLoading.postValue(false);
            }
        }, 1500);
    }

    // ========== Mock消息中心数据 ==========
    public void getMessageCenter(String userId, Context context) {
        isLoading.setValue(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                List<BaseMessage> mockData = new ArrayList<>();

                BaseMessage sys1 = new BaseMessage();
                sys1.setId("201");
                sys1.setType(3); // 系统消息类型
                sys1.setTitle("系统通知");
                sys1.setContent("你的校园卡余额不足10元，请及时充值。");
                sys1.setSender("校园卡中心");
                sys1.setTime(System.currentTimeMillis() - 1800000); // 30分钟前
                sys1.setRead(false);

                BaseMessage sys2 = new BaseMessage();
                sys2.setId("202");
                sys2.setType(3);
                sys2.setTitle("系统通知");
                sys2.setContent("你已成功报名参加“校园招聘会”，时间为1月10日上午9点。");
                sys2.setSender("就业指导中心");
                sys2.setTime(System.currentTimeMillis() - 43200000); // 12小时前
                sys2.setRead(true);

                mockData.add(sys1);
                mockData.add(sys2);

                messageCenterList.postValue(mockData);
                isLoading.postValue(false);
            }
        }, 1500);
    }
}