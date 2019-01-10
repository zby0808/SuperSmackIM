package myproject.smack.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.item.comm.base.BaseListFragment;
import com.item.comm.base.BasePresenter;
import com.item.comm.base.ListSetupModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import myproject.smack.activity.ChatActivity;
import myproject.smack.adapter.MessageFragmentAdapter;
import myproject.smack.bean.ChatMessageBean;

/**
 * Created by Administrator on 2018/12/4.
 * 消息界面
 */

public class MessageFragment extends BaseListFragment<MessageFragmentAdapter, Object, BasePresenter> {

    //用户聊天记录列表
    private List<List<ChatMessageBean>> mList = new ArrayList<>();

    @Override
    protected ListSetupModel setupParam() {
        return new ListSetupModel.Builder()
                .isDivided(true)
                .isEnableLoadMore(false)
                .isEnableRefresh(false)
                .build();
    }

    @Override
    protected MessageFragmentAdapter getAdapter() {
        MessageFragmentAdapter adapter = new MessageFragmentAdapter();
        return adapter;
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {

    }


    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatMessageEvent(ChatMessageBean message) {
        initListData(message);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ChatMessageBean> listData = (List<ChatMessageBean>) adapter.getItem(position);
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("listData", (Serializable) listData);
        startActivity(intent);
    }

    private void initListData(ChatMessageBean message) {
        if (!isContainsName(message)) {
            ArrayList<ChatMessageBean> list = new ArrayList<>();
            list.add(message);
            mList.add(list);
        }
        baseListAdapter.getData().clear();
        baseListAdapter.getData().addAll(mList);
        baseListAdapter.notifyDataSetChanged();
    }

    /**
     * 发送方名字是否一致，一致放在一起
     *
     * @param message
     * @return
     */
    private boolean isContainsName(ChatMessageBean message) {
        boolean isContain = false;
        for (List<ChatMessageBean> listData : mList) {
            if (listData.get(0).getFriendNickname().equals(message.getFriendNickname())) {
                listData.add(message);
                isContain = true;
                break;
            } else {
                isContain = false;
            }
        }
        return isContain;
    }
}
