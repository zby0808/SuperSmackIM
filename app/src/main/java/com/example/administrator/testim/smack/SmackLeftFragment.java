package com.example.administrator.testim.smack;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.testim.R;
import com.item.comm.base.BaseFragment;
import com.item.comm.base.BasePresenter;

import butterknife.BindView;

/**
 * Created by zby on 2018/12/7.
 */

public class SmackLeftFragment extends BaseFragment<BasePresenter> {
    @BindView(R.id.tv)
    TextView mMessageTv;
    @Override
    public void doParseData(int requestCode, Object data) {

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smack_contacts;
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        mMessageTv.setText("消息");

    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }
}

