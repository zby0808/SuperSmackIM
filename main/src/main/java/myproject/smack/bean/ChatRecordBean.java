package myproject.smack.bean;

import android.os.Parcel;

import myproject.smack.manager.SmackManager;
import myproject.utils.DateUtil;

/**
 * 聊天记录实体对象
 *
 *
 * Created by zby on 2018/12/3.
 */
public class ChatRecordBean extends ChatUserBean {
    /**
     * 最后聊天时间
     */
    private String mChatTime;
    /**
     * 朋友头像地址
     */
    private String mFriendAvatar;
    /**
     * 最后一条聊天记录
     */
    private String mLastMessage;
    /**
     * 未读消息数
     */
    private int mUnReadMessageCount;

    public ChatRecordBean(ChatUserBean chatUser) {

        //减少服务请求
        setFriendUsername(chatUser.getFriendUsername());
        setFriendNickname(chatUser.getFriendNickname());
        setMeNickname(chatUser.getMeNickname());
        setMeUsername(chatUser.getMeUsername());
        setChatJid(chatUser.getChatJid());
        setFileJid(chatUser.getFileJid());
        setUuid(chatUser.getUuid());
        setChatTime(DateUtil.currentDatetime());
        setMulti(chatUser.isMulti());
    }

    public ChatRecordBean(ChatMessageBean chatMessage) {

        setFriendUsername(chatMessage.getFriendUsername());
        setMeUsername(chatMessage.getMeUsername());
        setMeNickname(chatMessage.getMeNickname());

        if(chatMessage.isMulti()) {//群发
            int idx = chatMessage.getFriendUsername().indexOf("@conference.");
            String friendNickName = chatMessage.getFriendUsername().substring(0, idx);
            setFriendNickname(friendNickName);//群聊记录显示群聊名称
            setChatJid(chatMessage.getFriendUsername());
            setMulti(chatMessage.isMulti());
        } else {
            setFriendNickname(chatMessage.getFriendNickname());
            String chatJid = SmackManager.getInstance().getChatJid(chatMessage.getFriendUsername());
            String fileJid = SmackManager.getInstance().getFileTransferJid(chatJid);
            setChatJid(chatJid);
            setFileJid(fileJid);
        }

        setChatTime(chatMessage.getDatetime());
        setLastMessage(chatMessage.getContent());
        setUuid(chatMessage.getUuid());
        updateUnReadMessageCount();
    }

    public String getFriendAvatar() {

        return mFriendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {

        mFriendAvatar = friendAvatar;
    }

    public String getChatTime() {

        return mChatTime == null ? DateUtil.currentDatetime() : mChatTime;
    }

    public void setChatTime(String chatTime) {

        mChatTime = chatTime;
    }

    public String getLastMessage() {

        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {

        mLastMessage = lastMessage;
    }

    public int getUnReadMessageCount() {

        return mUnReadMessageCount;
    }

    public void updateUnReadMessageCount() {

        mUnReadMessageCount += 1;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null) {
            return false;
        }
        if(obj instanceof ChatRecordBean) {
            return this.getUuid().equals(((ChatRecordBean) obj).getUuid());
        }
        return false;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);
        dest.writeString(this.mChatTime);
        dest.writeString(this.mFriendAvatar);
        dest.writeString(this.mLastMessage);
        dest.writeInt(this.mUnReadMessageCount);
    }

    public ChatRecordBean() {

    }

    protected ChatRecordBean(Parcel in) {

        super(in);
        this.mChatTime = in.readString();
        this.mFriendAvatar = in.readString();
        this.mLastMessage = in.readString();
        this.mUnReadMessageCount = in.readInt();
    }

    public static final Creator<ChatRecordBean> CREATOR = new Creator<ChatRecordBean>() {
        @Override
        public ChatRecordBean createFromParcel(Parcel source) {

            return new ChatRecordBean(source);
        }

        @Override
        public ChatRecordBean[] newArray(int size) {

            return new ChatRecordBean[size];
        }
    };
}
