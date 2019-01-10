package myproject.smack.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.StringUtils;
import com.item.comm.base.BaseActivity;
import com.item.comm.base.BasePresenter;
import com.item.comm.help.recycleview.RecyclerViewHelper;
import com.item.comm.util.ToastyUtil;
import com.item.comm.util.ToolbarUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.Occupant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import myproject.MainXYSpHelper;
import myproject.R;
import myproject.R2;
import myproject.smack.adapter.GroupInfoAdapter;
import myproject.smack.manager.SmackManager;

/**
 * Created by Administrator on 2018/12/13.
 * 群聊信息窗口
 */

public class GroupInfoActivity extends BaseActivity<BasePresenter> {

    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.tool_bar)
    Toolbar toolbar;
    @BindView(R2.id.stv_name)
    SuperTextView mNameView;

    private MultiUserChat mMultiUserChat;
    private GroupInfoAdapter mAdapter;
    private List<Occupant> mListData = new ArrayList<>();

    @Override
    public void doParseData(int requestCode, Object data) {

    }

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        initData();
        initView();
    }

    @OnClick(R2.id.btn_delete)
    public void deleteGroupClick() {
        try {
            mMultiUserChat.destroy("解散群", null);
        } catch (Exception e) {
            e.printStackTrace();
            ToastyUtil.errorShort("解散群失败或其它原因");
        }
    }

    @OnClick(R2.id.stv_add)
    public void addClick() {
        String roomId = getIntent().getStringExtra("roomId");
        try {
            mMultiUserChat.invite( "18274635808@xydev", "一起聊天啊");
        } catch (SmackException.NotConnectedException e) {
            Log.e("----","添加成员出错");
            e.printStackTrace();
        }
    }

    @OnClick(R2.id.stv_remove)
    public void removeClick() {
        try {
            mMultiUserChat.kickParticipant("18274635808", "看着不爽");
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initData() {
        String roomId = getIntent().getStringExtra("roomId");
        mMultiUserChat = MultiUserChatManager.getInstanceFor(SmackManager.getInstance().getConnection())
                .getMultiUserChat(roomId);
        try {
            mListData = mMultiUserChat.getParticipants();//获取参与者用户，不包括当前登录用户
        } catch (Exception e) {
            e.printStackTrace();
        }

        //在线用户是否包含自己，包含则添加到列表
        Occupant occupant = mMultiUserChat.getOccupant(roomId + "/" + MainXYSpHelper.getInstance(this).getUserId());
        if (occupant != null) {
            mListData.add(0, occupant);
        }

    }

    private void initView() {
        ToolbarUtils.with(this, toolbar)
                .setSupportBack(true)
                .setTitle("聊天信息", true)
                .build();
        mNameView.setRightString(MainXYSpHelper.getInstance(this).getUserName());

        mAdapter = new GroupInfoAdapter();
        RecyclerViewHelper.initRecyclerViewG(mRecyclerView, false, mAdapter, 5);

        mAdapter.getData().clear();
        mAdapter.getData().addAll(mListData);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_info;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }
}
