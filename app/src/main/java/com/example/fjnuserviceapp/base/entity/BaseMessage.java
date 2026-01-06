package com.example.fjnuserviceapp.base.entity;

import android.os.Parcel;
import android.os.Parcelable;

// 核心修正1：声明实现Parcelable接口
public class BaseMessage implements Parcelable {
    private String id;             // 消息唯一ID
    private int type;              // 1-学院通知 2-私信 3-系统消息
    private String title;          // 标题
    private String content;        // 内容
    private String sender;         // 发送者
    private long time;             // 时间戳（毫秒）
    private boolean isRead;        // 是否已读

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }

    public boolean isRead() { return isRead; }
    // 优化：参数名改为isRead，和字段名一致（无运行时影响，仅规范）
    public void setRead(boolean isRead) { this.isRead = isRead; }

    // 空构造方法
    public BaseMessage() {}

    // Parcel构造方法（反序列化）
    protected BaseMessage(Parcel in) {
        id = in.readString();
        type = in.readInt();
        title = in.readString();
        content = in.readString();
        sender = in.readString();
        time = in.readLong();
        isRead = in.readByte() != 0;
    }

    // 静态CREATOR
    public static final Parcelable.Creator<BaseMessage> CREATOR = new Parcelable.Creator<BaseMessage>() {
        @Override
        public BaseMessage createFromParcel(Parcel in) {
            return new BaseMessage(in);
        }

        @Override
        public BaseMessage[] newArray(int size) {
            return new BaseMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(sender);
        dest.writeLong(time);
        dest.writeByte((byte) (isRead ? 1 : 0));
    }
}