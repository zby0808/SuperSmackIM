package com.example.administrator.testim.smack;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.example.administrator.testim.R;
import com.item.comm.base.BaseFragment;
import com.item.comm.base.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by zby on 2018/12/7.
 * 即时通讯fragment
 */
public class MessageFragment extends BaseFragment<BasePresenter> {
    private static final String FRAGMENT_LEFT = SmackLeftFragment.class.getName();
    private static final String FRAGMENT_RIGHT = SmackRightFragment.class.getName();

    @BindView(R.id.container_left)
    Button mLeftBtn;
    @BindView(R.id.container_right)
    Button mRightBtn;
    BaseFragment mBaseFragment;

    @Override
    public void doParseData(int requestCode, Object data) {

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smack_message;
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        initTabInfo();
        mLeftBtn.performClick();
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @OnClick({R.id.container_left, R.id.container_right})
    public void onClick(View view) {
        if (view.getId() == R.id.container_left) {
            switchTo(mLeftBtn, mRightBtn, FRAGMENT_LEFT, null);
        } else if (view.getId() == R.id.container_right) {
            switchTo(mRightBtn, mLeftBtn, FRAGMENT_RIGHT, null);
        }
    }

    private void switchTo(Button enabledBtn, Button unEnabledBtn, String tab, Bundle bundle) {
        //button 状态切换
        if (enabledBtn.isEnabled()) {
            unEnabledBtn.setEnabled(true);
            enabledBtn.setEnabled(false);
        }
        //初始化管理Fragment的类
        FragmentManager fm = getFragmentManager();
        if (fm == null) return;
        FragmentTransaction ft = fm.beginTransaction();

        //从FragmentManager里寻找类名为tab的Fragment
        BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(tab);
        if (fragment == null) {
            fragment = (BaseFragment) Fragment.instantiate(getActivity(), tab);
            fragment.setArguments(bundle);
            ft.add(R.id.id_content, fragment, tab);
        } else {
            ft.show(fragment);
        }
        //隐藏现在正显示的Fragment
        if (mBaseFragment != null) ft.hide(mBaseFragment);

        //记录最后点击的Fragment
        mBaseFragment = fragment;
        ft.commitAllowingStateLoss();
    }


    private void initTabInfo() {
        FragmentManager fm = getFragmentManager();
        if (fm == null) return;

        FragmentTransaction ft = fm.beginTransaction();

        BaseFragment fragmentLeft = (BaseFragment) fm.findFragmentByTag(FRAGMENT_LEFT);
        if (fragmentLeft != null) ft.hide(fragmentLeft);

        BaseFragment fragmentRight = (BaseFragment) fm.findFragmentByTag(FRAGMENT_RIGHT);
        if (fragmentRight != null) ft.hide(fragmentRight);

        ft.commit();
    }
}
