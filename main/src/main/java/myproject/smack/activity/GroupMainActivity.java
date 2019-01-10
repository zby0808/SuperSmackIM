package myproject.smack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.item.comm.base.BaseListActivity;
import com.item.comm.base.BasePresenter;
import com.item.comm.base.ListSetupModel;
import com.item.comm.util.ToolbarUtils;

import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.RoomInfo;

import java.util.List;

import myproject.R;
import myproject.smack.manager.SmackManager;

/**
 * Created by Administrator on 2018/12/12.
 */

public class GroupMainActivity extends BaseListActivity<GroupMainActivity.GroupMainAdapter, Object, BasePresenter> {

    @Override
    protected ListSetupModel setupParam() {
        return new ListSetupModel.Builder()
                .isEnableRefresh(false)
                .isEnableLoadMore(false)
                .build();
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    protected GroupMainAdapter getAdapter() {
        return new GroupMainAdapter();
    }

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        ToolbarUtils.with(this, toolbar)
                .setSupportBack(true)
                .setTitle("群聊", true)
                .build();

        List<HostedRoom> hostedRoom = SmackManager.getInstance().getHostedRoom();
        List<RoomInfo> joined = SmackManager.getInstance().getJoinedHostedRoom("18274635808@xydev/Smack");
        baseListAdapter.getData().clear();
        baseListAdapter.getData().addAll(hostedRoom);
        baseListAdapter.notifyDataSetChanged();

    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, GroupChatActivity.class);
        HostedRoom room = (HostedRoom) adapter.getItem(position);
        intent.putExtra("roomId", room.getJid());
        intent.putExtra("roomName", room.getName());
        startActivity(intent);

    }

    class GroupMainAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

        public GroupMainAdapter() {
            super(R.layout.fragment_chat_list);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            HostedRoom room = (HostedRoom) item;
            helper.setText(R.id.tv_name, room.getName())
                    .setText(R.id.tv_content, room.getJid());

        }
    }
}
