package com.example.administrator.testim;


import android.os.Bundle;
import android.view.View;

import com.item.comm.base.BaseFragment;
import com.item.comm.base.BasePresenter;

public class ContactsFragment extends BaseFragment<BasePresenter> {


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

    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }
}
