package myproject.smack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.item.comm.util.ToastyUtil;
import com.item.comm.util.ToolbarUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.Occupant;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import myproject.MainXYSpHelper;
import myproject.R;
import myproject.R2;
import myproject.smack.adapter.ChatAdapter;
import myproject.smack.bean.ChatMessageBean;
import myproject.smack.greendao.ChatMessageDaoBean;
import myproject.smack.keyboard.ChatKeyboard;
import myproject.smack.keyboard.KeyBoardMoreFunType;
import myproject.smack.listener.RoomUserStatusListener;
import myproject.smack.manager.SmackListenerManager;
import myproject.smack.manager.SmackManager;
import myproject.smack.recyclerview.CommonRecyclerView;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/12/12.
 */

public class GroupChatActivity extends IMBaseActivity implements ChatKeyboard.KeyboardOperateListener
        , PresenceListener {

    @BindView(R2.id.chat_content)
    CommonRecyclerView mChatMessageRecyclerView;//聊天内容列表

    @BindView(R2.id.tool_bar)
    Toolbar mToolBar;

    @BindView(R2.id.ckb_chat_board)
    ChatKeyboard mChatKeyboard;// 聊天输入控件

    /**
     * 多人聊天对象
     */
    private MultiUserChat mMultiUserChat;
    private ChatAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_layout);
        ButterKnife.bind(this);

        String roomId = getIntent().getStringExtra("roomId");
        String roomName = getIntent().getStringExtra("roomName");
        initToolBarView(roomName, roomId);
        EventBus.getDefault().register(this);

        initRecycleView();

        join(roomId, MainXYSpHelper.getInstance(this).getUserId());
        try {
            List<Affiliate> admins = mMultiUserChat.getAdmins();//群管理员
            List<Occupant> participants = mMultiUserChat.getParticipants();//在线用户
            List<String> occupants = mMultiUserChat.getOccupants();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initToolBarView(final String roomName, final String roomId) {
        ToolbarUtils.with(this, mToolBar)
                .setSupportBack(true)
                .setTitle(roomName, true)
                .setInflateMenu(R.menu.menu_group_more)
                .build();
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mMultiUserChat != null) {
                    Intent intent = new Intent(GroupChatActivity.this, GroupInfoActivity.class);
                    intent.putExtra("roomId", roomId);
                    intent.putExtra("roomName", roomName);
                    startActivity(intent);
                } else {
                    ToastyUtil.errorShort("获取群信息失败");
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveRoom();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveChatMessageEvent(ChatMessageDaoBean message) {
        if (getIntent().getStringExtra("roomId").equals(message.getMFriendUsername())
                && message.getMIsMulti()) {
            mAdapter.add(message);
            mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }


    private void initRecycleView() {
        final List<ChatMessageDaoBean> listData = new ArrayList<>();
        mChatKeyboard.setKeyboardOperateListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mChatMessageRecyclerView.setLayoutManager(mLayoutManager);
        mChatMessageRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mChatKeyboard.hideKeyBoardView();
                return false;
            }
        });

        mAdapter = new ChatAdapter(mActivity, listData);
        mChatMessageRecyclerView.setAdapter(mAdapter);
        mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void send(String message) {

        if (TextUtils.isEmpty(message)) {
            return;
        }
        Observable.just(message)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String message) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put(ChatMessageDaoBean.KEY_MESSAGE_CONTENT, message);
                            MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
                            json.put(ChatMessageDaoBean.KEY_MULTI_CHAT_SEND_USER, helper.getUserId());
                            mMultiUserChat.sendMessage(json.toString());
                        } catch (Exception e) {
                            Logger.e(e, "发送消息失败");
                        }
                    }
                });
    }

    @Override
    public void sendVoice(File audioFile) {

    }

    @Override
    public void functionClick(KeyBoardMoreFunType funType) {

    }

    /**
     * 加入一个群聊聊天室
     *
     * @param jid      聊天室ip 格式为>>群组名称@conference.ip
     * @param nickName 用户在聊天室中的昵称
     * @return
     */
    public MultiUserChat join(String jid, String nickName) {
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口
            mMultiUserChat = MultiUserChatManager.getInstanceFor(SmackManager.getInstance().getConnection())
                    .getMultiUserChat(jid);
            // 聊天室服务将会决定要接受的历史记录数量
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxChars(0);
            // 用户加入聊天室
            mMultiUserChat.join(nickName, null, history, SmackConfiguration.getDefaultPacketReplyTimeout());
            SmackListenerManager.addMultiChatMessageListener(mMultiUserChat);//消息监听
            mMultiUserChat.addParticipantListener(this);
            mMultiUserChat.addUserStatusListener(new RoomUserStatusListener());
            return mMultiUserChat;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 退出群聊
     */
    public void leaveRoom() {
        //退出群
        try {
            mMultiUserChat.leave();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processPresence(Presence presence) {
        //监听用户上线和离线
        boolean isAvailable = presence.getType().toString().equals(Presence.Type.available.toString());
        String from = presence.getFrom();
        int index = from.lastIndexOf("/");
        final String peopleId = from.substring(index + 1, from.length());
        final String value = isAvailable ? "加入了群聊" : "退出了群聊";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastyUtil.infoShort(peopleId + value);
            }
        });
    }
}
