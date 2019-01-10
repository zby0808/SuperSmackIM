package myproject.smack.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.item.comm.base.BaseFragment;
import com.item.comm.base.BasePresenter;
import com.item.comm.util.ToastyUtil;

import butterknife.BindView;
import butterknife.OnClick;
import myproject.MainXYSpHelper;
import myproject.R;
import myproject.R2;
import myproject.model.LoginResult;
import myproject.model.User;
import myproject.smack.activity.CreateGroupIMActivity;
import myproject.smack.manager.SmackListenerManager;
import myproject.smack.manager.SmackManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by zby on 2018/12/7.
 * * 即时通讯fragment
 */
public class SmackIMFragment extends BaseFragment<BasePresenter> {
    private static final String FRAGMENT_LEFT = SmackLeftFragment.class.getName();
    private static final String FRAGMENT_RIGHT = SmackRightFragment.class.getName();

    @BindView(R2.id.tool_bar)
    Toolbar toolbar;
    @BindView(R2.id.container_left)
    Button mLeftBtn;
    @BindView(R2.id.container_right)
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
        return R.layout.fragment_smack;
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        initToolbar();
        initTabInfo();
        loginSuperSmack();
        mLeftBtn.performClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SmackManager.getInstance().logout();
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_scan) {
                    getActivity().finish();
                } else if (item.getItemId() == R.id.action_more) {
                    startActivity(new Intent(getActivity(), CreateGroupIMActivity.class));
                }
                return true;
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @OnClick({R2.id.container_left, R2.id.container_right})
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

    //登录openfile服务
    private void loginSuperSmack() {
        String spConfig = getArguments().getString("spConfig");
        SharedPreferences sp = getActivity().getSharedPreferences(spConfig, Context.MODE_PRIVATE);

        final String account = sp.getString("userName", "");
        final String passWord = sp.getString("password", "");

        MainXYSpHelper helper = MainXYSpHelper.getInstance(getContext());
        helper.saveUserId(account);
        helper.saveUserName(account);
        helper.savePassWord(passWord);

        Observable.just(new User(account, passWord))
                .subscribeOn(Schedulers.io())//指定下面的flatMap线程
                .flatMap(new Func1<User, Observable<LoginResult>>() {
                    @Override
                    public Observable<LoginResult> call(User user) {
                        LoginResult loginResult = SmackManager.getInstance().login(account, passWord);
                        return Observable.just(loginResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//给下面的subscribe设定线程
                .subscribe(new Action1<LoginResult>() {
                    @Override
                    public void call(LoginResult loginResult) {

                        SmackListenerManager.addGlobalListener();//单聊消息监听
                        SmackListenerManager.addGroupInvitationListener(getActivity());//群邀请监听

                        ToastyUtil.successShort(loginResult.isSuccess() ? "连接openFile成功"
                                : "连接openFile失败");
                    }
                });
    }

}
