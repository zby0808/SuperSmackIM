package myproject.smack.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.Utils;
import com.item.comm.base.BaseActivity;
import com.item.comm.help.recycleview.divider.HorizontalDividerItemDecoration;
import com.item.comm.util.ToastyUtil;
import com.item.comm.util.ToolbarUtils;

import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import myproject.MainXYSpHelper;
import myproject.R;
import myproject.R2;
import myproject.model.User;
import myproject.presenter.PersonTreePresent;
import myproject.smack.manager.SmackManager;
import myproject.smack.myview.tree.Node;
import myproject.smack.myview.tree.SimpleTreeRecyclerAdapter;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2018/12/12.
 * 创建群聊
 */

public class CreateGroupIMActivity extends BaseActivity<PersonTreePresent> implements
        SimpleTreeRecyclerAdapter.OnTreeItemClickListener {
    private static int REQUEST_CODE_ADD = 110;
    private static int REQUEST_CODE_REFRESH = 111;

    @BindView(R2.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;

    private SimpleTreeRecyclerAdapter mAdapter;

    private Node mNode;

    @Override
    protected void onCreateBefore(Bundle savedInstanceState) {
        super.onCreateBefore(savedInstanceState);
        mPresenter.searchDeptAndUserInfo("2", REQUEST_CODE_REFRESH);
    }

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        ToolbarUtils.with(this, mToolbar)
                .setSupportBack(true).setTitle("创建群聊", true)
                .build();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(Utils.getApp())
                .colorResId(R.color.line).build());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_group_select;
    }

    @Override
    protected PersonTreePresent getPresenter() {
        return new PersonTreePresent();
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        List<Node> mData = (List<Node>) data;
        if (requestCode == REQUEST_CODE_REFRESH) {
            if (null == mData || mData.size() == 0) return;
            initSelectTree(mData);
            mAdapter = new SimpleTreeRecyclerAdapter(mRecyclerView, this,
                    mData, 0, R.drawable.main_close, R.drawable.main_open);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnTreeItemClickListener(this);
        } else if (requestCode == REQUEST_CODE_ADD) {
            if (mData == null || mData.size() == 0) {
                ToastyUtil.infoShort("当前组织无下属用户和单位!");
                return;
            }
            mAdapter.addData(0, mData, mNode);
        }

    }

    private void initSelectTree(List<Node> mData) {
        String selectJson = getIntent().getStringExtra("selectJson");
        List<Node> selectNodes = JSON.parseArray(selectJson, Node.class);
        if (TextUtils.isEmpty(selectJson)) return;
        if (null == selectNodes || selectNodes.size() == 0) return;
        for (Node node : mData) {
            for (Node selectNode : selectNodes) {
                if (node.getTaskId() == selectNode.getTaskId() &&
                        node.getId().equals(selectNode.getId())) {
                    node.setChecked(true);
                }
            }
        }
    }

    /**
     * 显示选中数据
     */
    @OnClick(R2.id.btn_sure)
    public void clickShow() {
        final StringBuilder sbName = new StringBuilder();
        final StringBuilder sbProjectId = new StringBuilder();
        final StringBuilder sbTaskId = new StringBuilder();
        final List<Node> allNodes = mAdapter.getAllNodes();
        final List<Node> selectNodes = new ArrayList<>();
        Subscription subscribe = Observable.from(allNodes).filter(new Func1<Node, Boolean>() {
            @Override
            public Boolean call(Node node) {
                return node.isChecked();
            }
        }).subscribe(new Action1<Node>() {
            @Override
            public void call(Node node) {
                selectNodes.add(node);
                if (!node.isHaveChild()) {
                    sbName.append(node.getName() + ",");
                    if (node.getTaskId() != -1) {
                        sbTaskId.append(node.getTaskId() + ",");
                    } else {
                        sbProjectId.append(node.getId() + ",");
                    }
                } else {
                    sbProjectId.append(node.getId() + ",");
                }
            }
        });

        subscribe.unsubscribe();//取消订阅、

//        Map<String, String> map = new HashMap<>();
//        initMapData(map, "projectIds", sbProjectId.toString());
//        initMapData(map, "taskIds", sbTaskId.toString());
//        initMapData(map, "name", sbName.toString());
//        map.put("selectNodes", JSON.toJSONString(selectNodes));
//
//        Intent intent = getIntent().putExtra("mapData", (Serializable) map);
//        setResult(RESULT_OK, intent);
//        finish();

        List<User> users = new ArrayList<>();
        users.add(new User("18274635808", "18274635808"));
//        users.add(new User("18274635809", "18274635809"));
//        users.add(new User("999991", "999991"));
//        users.add(new User("999992", "999992"));
        createMultiChat(users);

    }

    private void initMapData(Map<String, String> map, String mapKey, String mapValue) {
        if (!TextUtils.isEmpty(mapValue)) {
            map.put(mapKey, mapValue.substring(0, mapValue.length() - 1));
        }
    }

    @Override
    public void onClick(Node node, int position) {
        if (node.isAdd()) {
            mNode = node;
            mPresenter.searchDeptAndUserInfo(node.getId() + "", REQUEST_CODE_ADD);
        }

        if (!node.isHaveChild()) {
            if (node.isChecked()) {
                node.setChecked(false);
            } else {
                node.setChecked(true);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 创建多人聊天
     */
    private void createMultiChat(List<User> userList) {
        String meNickName = MainXYSpHelper.getInstance(this).getUserId();
        String chatRoomName = String.format("%s创建的群", meNickName);
        String reason = String.format("%s邀请你入群", meNickName);
        try {
            MultiUserChat multiUserChat = SmackManager.getInstance().createChatRoom(chatRoomName, meNickName, null);
            for (User entity : userList) {
                String jid = entity.getUsername() + "@xydev";
                multiUserChat.invite(jid, reason);//邀请入群
            }
            Intent intent = new Intent(this, GroupChatActivity.class);
            String roomId = multiUserChat.getRoom();
            intent.putExtra("roomId", roomId);
            intent.putExtra("roomName", chatRoomName);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            ToastyUtil.errorShort("创建群失败");
        }
    }
}
