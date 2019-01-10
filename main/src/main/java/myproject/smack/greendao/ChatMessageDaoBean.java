package myproject.smack.greendao;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import myproject.smack.enumclass.FileLoadState;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 聊天发送的消息
 *
 * Created by zby on 2018/12/3.
 */
@Entity
public class ChatMessageDaoBean implements Parcelable {
    public static final String KEY_FROM_NICKNAME = "fromNickName";
    public static final String KEY_MESSAGE_CONTENT = "messageContent";
    public static final String KEY_MULTI_CHAT_SEND_USER = "multiChatSendUser";

    @Id
    private String uuid;
    /**
     * 消息内容
     */

    private String mContent;
    /**
     * 消息类型
     */
    private int mMessageType;
    /**
     * 聊天好友的用户名,群聊时为群聊的jid,格式为：老张的群@conference.127.0.0.1
     */
    private String mFriendUsername;
    /**
     * 聊天好友的昵称
     */
    private String mFriendNickname;
    /**
     * 自己的用户名
     */
    private String mMeUsername;
    /**
     * 自己的昵称
     */
    private String mMeNickname;
    /**
     * 消息发送接收的时间
     */
    private String mDatetime;
    /**
     * 当前消息是否是自己发出的
     */
    private boolean mIsMeSend;
    /**
     * 接收的图片或语音路径
     */
    private String mFilePath;
    /**}
     * 文件加载状态
     */
    private int mFileLoadState = FileLoadState.STATE_LOAD_START.value();
    /**
     * 是否为群聊记录
     */
    private boolean mIsMulti = false;

    public ChatMessageDaoBean() {

    }

    public ChatMessageDaoBean(int messageType, boolean isMeSend) {

        mMessageType = messageType;
        mIsMeSend = isMeSend;

        this.uuid = UUID.randomUUID().toString();
        this.mDatetime = formatDatetime(new Date());
    }

    @Generated(hash = 608355322)
    public ChatMessageDaoBean(String uuid, String mContent, int mMessageType,
            String mFriendUsername, String mFriendNickname, String mMeUsername,
            String mMeNickname, String mDatetime, boolean mIsMeSend,
            String mFilePath, int mFileLoadState, boolean mIsMulti) {
        this.uuid = uuid;
        this.mContent = mContent;
        this.mMessageType = mMessageType;
        this.mFriendUsername = mFriendUsername;
        this.mFriendNickname = mFriendNickname;
        this.mMeUsername = mMeUsername;
        this.mMeNickname = mMeNickname;
        this.mDatetime = mDatetime;
        this.mIsMeSend = mIsMeSend;
        this.mFilePath = mFilePath;
        this.mFileLoadState = mFileLoadState;
        this.mIsMulti = mIsMulti;
    }


    protected ChatMessageDaoBean(Parcel in) {
        uuid = in.readString();
        mContent = in.readString();
        mMessageType = in.readInt();
        mFriendUsername = in.readString();
        mFriendNickname = in.readString();
        mMeUsername = in.readString();
        mMeNickname = in.readString();
        mDatetime = in.readString();
        mIsMeSend = in.readByte() != 0;
        mFilePath = in.readString();
        mFileLoadState = in.readInt();
        mIsMulti = in.readByte() != 0;
    }

    public static final Creator<ChatMessageDaoBean> CREATOR = new Creator<ChatMessageDaoBean>() {
        @Override
        public ChatMessageDaoBean createFromParcel(Parcel in) {
            return new ChatMessageDaoBean(in);
        }

        @Override
        public ChatMessageDaoBean[] newArray(int size) {
            return new ChatMessageDaoBean[size];
        }
    };

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMContent() {
        return this.mContent;
    }

    public void setMContent(String mContent) {
        this.mContent = mContent;
    }

    public int getMMessageType() {
        return this.mMessageType;
    }

    public void setMMessageType(int mMessageType) {
        this.mMessageType = mMessageType;
    }

    public String getMFriendUsername() {
        return this.mFriendUsername;
    }

    public void setMFriendUsername(String mFriendUsername) {
        this.mFriendUsername = mFriendUsername;
    }

    public String getMFriendNickname() {
        return this.mFriendNickname;
    }

    public void setMFriendNickname(String mFriendNickname) {
        this.mFriendNickname = mFriendNickname;
    }

    public String getMMeUsername() {
        return this.mMeUsername;
    }

    public void setMMeUsername(String mMeUsername) {
        this.mMeUsername = mMeUsername;
    }

    public String getMMeNickname() {
        return this.mMeNickname;
    }

    public void setMMeNickname(String mMeNickname) {
        this.mMeNickname = mMeNickname;
    }

    public String getMDatetime() {
        return this.mDatetime;
    }

    public void setMDatetime(String mDatetime) {
        this.mDatetime = mDatetime;
    }

    public boolean getMIsMeSend() {
        return this.mIsMeSend;
    }

    public void setMIsMeSend(boolean mIsMeSend) {
        this.mIsMeSend = mIsMeSend;
    }

    public String getMFilePath() {
        return this.mFilePath;
    }

    public void setMFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public int getMFileLoadState() {
        return this.mFileLoadState;
    }

    public void setMFileLoadState(int mFileLoadState) {
        this.mFileLoadState = mFileLoadState;
    }

    public boolean getMIsMulti() {
        return this.mIsMulti;
    }

    public void setMIsMulti(boolean mIsMulti) {
        this.mIsMulti = mIsMulti;
    }


    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        if (o instanceof ChatMessageDaoBean) {
            return uuid.equals(((ChatMessageDaoBean) o).uuid);
        }
        return false;
    }

    public static String formatDatetime(Date date) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return datetimeFormat.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(mContent);
        dest.writeInt(mMessageType);
        dest.writeString(mFriendUsername);
        dest.writeString(mFriendNickname);
        dest.writeString(mMeUsername);
        dest.writeString(mMeNickname);
        dest.writeString(mDatetime);
        dest.writeByte((byte) (mIsMeSend ? 1 : 0));
        dest.writeString(mFilePath);
        dest.writeInt(mFileLoadState);
        dest.writeByte((byte) (mIsMulti ? 1 : 0));
    }
}
