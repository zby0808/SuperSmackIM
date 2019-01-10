package myproject.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.Utils;
import com.item.comm.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.packet.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myproject.MainXYSpHelper;
import myproject.MyApplication;
import myproject.smack.enumclass.MessageType;
import myproject.smack.greendao.ChatMessageDaoBean;
import myproject.smack.greendao.ChatMessageDaoBeanDao;
import myproject.smack.greendao.DaoSession;

/**
 * Created by Administrator on 2018/12/28.
 */

public class DaoPresenter extends BasePresenter {

    //存储接收消息----单聊
    public void saveReceiveChatMessage(Message message) {
        final String content = message.getBody();
        if (content != null) {
            Log.e("TAG", "from:" + message.getFrom() + " to:" + message.getTo() + " message:" + message.getBody());
            ChatMessageDaoBean msg = new ChatMessageDaoBean(MessageType.MESSAGE_TYPE_TEXT.value(), false);
            int index = message.getFrom().indexOf("@");
            String friendName = message.getFrom().substring(0, index);
            msg.setMFriendNickname(friendName);
            msg.setMFriendUsername(friendName);
            msg.setMIsMulti(false);
            if (content.startsWith("{")) {
                JSONObject chatMessages = JSON.parseObject(content, JSONObject.class);
                msg.setMContent(chatMessages.getString(ChatMessageDaoBean.KEY_MESSAGE_CONTENT));
                msg.setMMeNickname(chatMessages.getString(ChatMessageDaoBean.KEY_FROM_NICKNAME));
                msg.setMMeUsername(chatMessages.getString(ChatMessageDaoBean.KEY_FROM_NICKNAME));
            } else {
                msg.setMContent(content);
                MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
                msg.setMMeUsername(helper.getUserId());
                msg.setMMeNickname(helper.getUserId());
            }

            ChatMessageDaoBeanDao beanDao = MyApplication.mDaoSession.getChatMessageDaoBeanDao();
            beanDao.insert(msg);
            Log.e("缓存接收消息", "缓存数据库成功");
            EventBus.getDefault().post(msg);//通知更新
        }
    }

    /**
     * 存储发送消息---------单聊(包括发语音和图片)
     *
     * @param friendNickName 接收方姓名
     * @param friendUserName 接收方id
     * @param content        发送内容
     * @param filePath       发送文件名
     * @param isMeSend       是否我发送的（接收的时候）
     * @param messageType    消息类型
     */
    public ChatMessageDaoBean saveSendChatMessage(String friendNickName, String friendUserName, String content,
                                                  String filePath, boolean isMeSend, int messageType) {
        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
        ChatMessageDaoBean msg = new ChatMessageDaoBean(messageType, isMeSend);
        msg.setMFriendNickname(friendNickName);
        msg.setMFriendUsername(friendUserName);
        msg.setMMeUsername(helper.getUserId());
        msg.setMMeNickname(helper.getUserName());
        msg.setMIsMulti(false);
        msg.setMContent(content);
        msg.setMFilePath(filePath);
        ChatMessageDaoBeanDao beanDao = MyApplication.mDaoSession.getChatMessageDaoBeanDao();
        beanDao.insert(msg);
        Log.e("缓存发送消息", "缓存数据库成功");

        EventBus.getDefault().post(msg);//通知更新

        return msg;
    }


    //获取所有单聊消息
    public Map<String, List<ChatMessageDaoBean>> loadChatMessageList() {
        ChatMessageDaoBeanDao cacheDao = MyApplication.mDaoSession.getChatMessageDaoBeanDao();
        //将接收方相同的数据放到一起，map key为接收方
        Map<String, List<ChatMessageDaoBean>> map = new HashMap<>();
        List<ChatMessageDaoBean> listBean = cacheDao.queryBuilder()
                .where(ChatMessageDaoBeanDao.Properties.MIsMulti.eq(false))
                .list();//获取所有信息

        for (ChatMessageDaoBean bean : listBean) {
            if (map.containsKey(bean.getMFriendUsername())) {
                map.get(bean.getMFriendUsername()).add(bean);
            } else {
                List<ChatMessageDaoBean> list = new ArrayList<>();
                list.add(bean);
                map.put(bean.getMFriendUsername(), list);
            }
        }
        return map;
    }


    public void deleteAllMessage() {
        ChatMessageDaoBeanDao cacheDao = MyApplication.mDaoSession.getChatMessageDaoBeanDao();
        cacheDao.deleteAll();
    }
}
