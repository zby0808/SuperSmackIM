package myproject.smack.manager;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import java.util.HashMap;

import myproject.smack.listener.GroupInvitationListener;
import myproject.smack.listener.MultiChatMessageListener;
import myproject.smack.listener.SmackChatManagerListener;

/**
 * Smack全局监听器管理
 * <p>
 * Created by zby on 2018/12/3.
 */
public class SmackListenerManager {
    private static volatile SmackListenerManager sSmackListenerManager;
    /**
     * 单聊消息监听管理器
     */
    private SmackChatManagerListener mChatManagerListener;

    /**
     * 群聊消息监听
     */
    private MultiChatMessageListener mMultiChatMessageListener;


    /**
     * 群聊信息
     */
    private HashMap<String, MultiUserChat> mMultiUserChatHashMap = new HashMap<>();

    private SmackListenerManager() {
        mChatManagerListener = new SmackChatManagerListener();
        mMultiChatMessageListener = new MultiChatMessageListener();
    }

    public static SmackListenerManager getInstance() {

        if (sSmackListenerManager == null) {
            synchronized (SmackListenerManager.class) {
                if (sSmackListenerManager == null) {
                    sSmackListenerManager = new SmackListenerManager();
                }
            }
        }
        return sSmackListenerManager;
    }

    public static void addGlobalListener() {

        addMessageListener();
    }

    /**
     * 添加单聊消息全局监听
     */
    static void addMessageListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ChatManager chatManager = SmackManager.getInstance().getChatManager();
                    chatManager.addChatListener(getInstance().mChatManagerListener);
                } catch (Exception e) {
                    Log.e("smackListenerManager", "服务器未连接或其它问题");
                }
            }
        }).start();


    }

    public void destroy() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SmackManager.getInstance().getChatManager().removeChatListener(mChatManagerListener);
                    mChatManagerListener = null;
                } catch (Exception e) {
                    Log.e("smackListenerManager", "服务器未连接或其它问题");
                }
            }
        }).start();
    }

    /**
     * 为指定群聊添加消息监听
     *
     * @param multiUserChat
     */
    public static void addMultiChatMessageListener(MultiUserChat multiUserChat) {

        if (multiUserChat == null) {
            return;
        }
        getInstance().mMultiUserChatHashMap.put(multiUserChat.getRoom(), multiUserChat);
        multiUserChat.addMessageListener(getInstance().mMultiChatMessageListener);
    }


    /**
     * 要请用户进入群聊
     */
    public static void addGroupInvitationListener(FragmentActivity context) {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(
                SmackManager.getInstance().getConnection());
        GroupInvitationListener  groupInvitationListener = new GroupInvitationListener(context);

        manager.addInvitationListener(groupInvitationListener);
    }


}
