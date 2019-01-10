package myproject.smack.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Map;

import myproject.presenter.DaoPresenter;
import myproject.smack.activity.ChatActivity;
import myproject.smack.adapter.MessageFragmentAdapter;
import myproject.smack.greendao.ChatMessageDaoBean;

/**
 * Created by zby on 2018/12/7.
 * 即时通讯消息
 */

public class SmackLeftFragment extends BaseListFragment<MessageFragmentAdapter, Object, BasePresenter> {
    //用户聊天记录列表
    private List<List<ChatMessageDaoBean>> mList = new ArrayList<>();

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
        if (mList.size() == 0) {
        }
    }


    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatMessageEvent(ChatMessageDaoBean message) {
        if (!message.getMIsMulti()) {
            initListData(message);
        }
    }


    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreateViewAfter(view, savedInstanceState);
        loadDbListBean();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ChatMessageDaoBean> listData = (List<ChatMessageDaoBean>) adapter.getItem(position);
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("listData", (Serializable) listData);
        startActivity(intent);
    }

    private void loadDbListBean() {
        Map<String, List<ChatMessageDaoBean>> mapList = new DaoPresenter().loadChatMessageList();
        if (mapList.size() == 0) return;
        for (List<ChatMessageDaoBean> list : mapList.values()) {
            mList.add(list);
        }
        baseListAdapter.getData().clear();
        baseListAdapter.getData().addAll(mList);
        baseListAdapter.notifyDataSetChanged();
    }

    private void initListData(ChatMessageDaoBean message) {
        if (!isContainsName(message)) {
            ArrayList<ChatMessageDaoBean> list = new ArrayList<>();
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
    private boolean isContainsName(ChatMessageDaoBean message) {
        boolean isContain = false;
        for (List<ChatMessageDaoBean> listData : mList) {
            if (listData.get(0).getMFriendNickname().equals(message.getMFriendNickname())) {
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

