package myproject.smack.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.Utils;
import com.item.comm.base.BaseFragment;
import com.item.comm.help.recycleview.divider.HorizontalDividerItemDecoration;
import com.item.comm.util.ToastyUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import myproject.R;
import myproject.R2;
import myproject.presenter.PersonTreePresent;
import myproject.smack.activity.ChatActivity;
import myproject.smack.activity.GroupMainActivity;
import myproject.smack.adapter.UnitTreeAdapter;
import myproject.smack.myview.tree.Node;
import myproject.smack.myview.tree.OnTreeNodeClickListener;

/**
 * Created by Administrator on 2018/12/7.
 * 即时通讯联系人
 */

public class SmackRightFragment extends BaseFragment<PersonTreePresent> implements OnTreeNodeClickListener {

    private static final int REQUEST_CODE = 991;
    private static final int REQUEST_CODE_ADD = 990;

    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    private UnitTreeAdapter mAdapter;

    @Override
    protected void requestData() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smack_contacts;
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        mPresenter.searchDeptAndUserInfo("2", REQUEST_CODE);
    }

    @OnClick(R2.id.add_more_people)
    public void onAddMoreClick(View view) {
//        ToastyUtil.infoShort("暂未实现，别点了...");
        startActivity(new Intent(getActivity(), GroupMainActivity.class));
    }


    @Override
    protected PersonTreePresent getPresenter() {
        return new PersonTreePresent();
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        List<Node> mData = (List<Node>) data;
        if (requestCode == REQUEST_CODE) {
            if (mData == null || mData.size() == 0) return;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(Utils.getApp())
                    .colorResId(R.color.line).build());

            mAdapter = new UnitTreeAdapter(mRecyclerView, activity,
                    mData, 0, R.drawable.main_close, R.drawable.main_open);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnTreeNodeClickListener(this);
        } else if (requestCode == REQUEST_CODE_ADD) {
            if (mData == null || mData.size() == 0) {
                ToastyUtil.infoShort("当前组织无下属用户和单位!");
                return;
            }
            mAdapter.addData(0, mData, mNode);
        }
    }

    private Node mNode;

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

    @Override
    public void OnLongClick(Node node, int position) {

        new MaterialDialog.Builder(getActivity())
                .title("老张超信")
                .content("进入聊天界面开始聊天？")
                .positiveText(R.string.comm_permission_confirm)
                .autoDismiss(true)
                .negativeText(R.string.comm_permission_cancel)
                .canceledOnTouchOutside(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getActivity(), ChatActivity.class));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .show();
    }

}
