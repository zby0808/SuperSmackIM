package myproject.smack.bean;

import myproject.smack.greendao.ChatMessageDaoBean;

/**
 * Created by Administrator on 2018/12/4.
 */

public class ChatMessageEvent {
    private ChatMessageDaoBean message;

    public ChatMessageDaoBean getMessage() {
        return message;
    }

    public void setMessage(ChatMessageDaoBean message) {
        this.message = message;
    }
}
