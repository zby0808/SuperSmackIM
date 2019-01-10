package myproject.smack.listener;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import myproject.presenter.DaoPresenter;


/**
 * Smack普通消息监听处理
 * <p>
 * Created by zby on 2018/12/3.
 */
public class SmackChatManagerListener implements ChatManagerListener {

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {

        chat.addMessageListener(new ChatMessageListener() {

            @Override
            public void processMessage(Chat chat, Message message) {
                //不会收到自己发送过来的消息
                new DaoPresenter().saveReceiveChatMessage(message);
            }
        });
    }
}
