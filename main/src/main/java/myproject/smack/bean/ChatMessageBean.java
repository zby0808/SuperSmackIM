package myproject.smack.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import myproject.smack.enumclass.FileLoadState;

/**
 * 聊天发送的消息
 *
 * Created by zby on 2018/12/3.
 */
public class ChatMessageBean implements Serializable {
    public static final String KEY_FROM_NICKNAME = "fromNickName";
    public static final String KEY_MESSAGE_CONTENT = "messageContent";
    public static final String KEY_MULTI_CHAT_SEND_USER = "multiChatSendUser";
    /**
     *
     */

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

    public ChatMessageBean() {

    }

    public ChatMessageBean(int messageType, boolean isMeSend) {

        mMessageType = messageType;
        mIsMeSend = isMeSend;

        this.uuid = UUID.randomUUID().toString();
        this.mDatetime = formatDatetime(new Date());
    }

    public String getContent() {

        return mContent;
    }

    public void setContent(String content) {

        mContent = content;
    }

    public int getMessageType() {

        return mMessageType;
    }

    public void setMessageType(int messageType) {

        mMessageType = messageType;
    }

    public String getFriendUsername() {

        return mFriendUsername;
    }

    public void setFriendUsername(String friendUsername) {

        mFriendUsername = friendUsername;
    }

    public String getFriendNickname() {

        return mFriendNickname;
    }

    public void setFriendNickname(String friendNickname) {

        mFriendNickname = friendNickname;
    }

    public String getMeUsername() {

        return mMeUsername;
    }

    public void setMeUsername(String meUsername) {

        mMeUsername = meUsername;
    }

    public String getMeNickname() {

        return mMeNickname;
    }

    public void setMeNickname(String meNickname) {

        mMeNickname = meNickname;
    }

    public String getDatetime() {

        return mDatetime;
    }

    public void setDatetime(String datetime) {

        mDatetime = datetime;
    }

    public boolean isMeSend() {

        return mIsMeSend;
    }

    public void setMeSend(boolean meSend) {

        mIsMeSend = meSend;
    }

    public String getFilePath() {

        return mFilePath;
    }

    public void setFilePath(String filePath) {

        mFilePath = filePath;
    }

    public int getFileLoadState() {

        return mFileLoadState;
    }

    public void setFileLoadState(int fileLoadState) {

        mFileLoadState = fileLoadState;
    }

    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {

        this.uuid = uuid;
    }

    public boolean isMulti() {

        return mIsMulti;
    }

    public void setMulti(boolean multi) {

        mIsMulti = multi;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        if (o instanceof ChatMessageBean) {
            return uuid.equals(((ChatMessageBean) o).uuid);
        }
        return false;
    }



    public static String formatDatetime(Date date) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return datetimeFormat.format(date);
    }
}
