package myproject.smack.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myproject.MainXYSpHelper;
import myproject.smack.enumclass.MessageType;
import myproject.smack.greendao.ChatMessageDaoBean;

/**
 * 多人聊天消息监听
 */
public class MultiChatMessageListener implements MessageListener {
    private static final String PATTERN = "[a-zA-Z0-9_]+@";

    @Override
    public void processMessage(Message message) {

        //不会收到自己发送过来的消息
        Logger.d(message.toString());
        String from = message.getFrom();//消息发送人，格式:老张创建的群@conference.127.0.0.1/老张   --> 老张发送的
        String to = message.getTo();//消息接收人(当前登陆用户)，格式:老张@127.0.0.1/Smack
        Matcher matcherTo = Pattern.compile(PATTERN).matcher(to);
        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());

        if (matcherTo.find()) {
            try {
                String[] fromUsers = from.split("/");
                String friendUserName = fromUsers[0];//创建的群@conference.127.0.0.1
                String friendNickName = fromUsers[1];//发送人的昵称，用于聊天窗口中显示

                String content = message.getBody();

                ChatMessageDaoBean chatMessage = new ChatMessageDaoBean(MessageType.MESSAGE_TYPE_TEXT.value(), false);
                chatMessage.setMFriendUsername(friendUserName);
                chatMessage.setMFriendNickname(friendNickName);
                chatMessage.setMMeUsername(helper.getUserId());
                chatMessage.setMMeNickname(helper.getUserId());
                chatMessage.setMIsMulti(true);
                if (content.startsWith("{")) {
                    final JSONObject chatMessages = JSON.parseObject(content, JSONObject.class);
                    chatMessage.setMContent(chatMessages.getString(ChatMessageDaoBean.KEY_MESSAGE_CONTENT));

                    String sendUser = chatMessages.getString(ChatMessageDaoBean.KEY_MULTI_CHAT_SEND_USER);
                    chatMessage.setMIsMeSend(helper.getUserId().equals(sendUser));
                } else {
                    chatMessage.setMContent(content);
                }

                EventBus.getDefault().post(chatMessage);
            } catch (Exception e) {
                Logger.e(e, "发送的消息格式不正确");
            }
        } else {
            Logger.e("发送人格式不正确");
        }
    }
}
