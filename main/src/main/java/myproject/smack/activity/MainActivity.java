package myproject.smack.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.Utils;
import com.item.comm.base.BaseActivity;
import com.item.comm.base.BasePresenter;
import com.item.comm.util.ToastyUtil;
import com.item.comm.util.ToolbarUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import myproject.R;
import myproject.R2;
import myproject.MainXYSpHelper;
import myproject.model.LoginResult;
import myproject.model.User;
import myproject.smack.manager.SmackListenerManager;
import myproject.smack.manager.SmackManager;
import myproject.smack.adapter.MyFragmentPagerAdapter;
import myproject.smack.fragment.ContactsFragment;
import myproject.smack.fragment.MessageFragment;
import myproject.smack.myview.NoScrollViewpager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by zby on 2016/11/29.
 * 主窗口
 */

public class MainActivity extends BaseActivity<BasePresenter> {

    @BindView(R2.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R2.id.id_drawer)
    LinearLayout drawerRootView;
    @BindView(R2.id.view_pager)
    NoScrollViewpager viewPager;
    @BindView(R2.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R2.id.rb_message)
    RadioButton rbMessage;
    @BindView(R2.id.rb_contacts)
    RadioButton rbContacts;

    private ArrayList<Fragment> fragmentList;
    private long exitTime = 0;

    private MessageFragment messageFragment;
    private ContactsFragment contactsFragment;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        loginSuperSmack();
        init();
        initViewPager();
        SmackListenerManager.addGlobalListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmackListenerManager.getInstance().destroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }


    private void init() {
        viewPager.setOffscreenPageLimit(2);//设置缓存页数
        viewPager.setScrollble(false);//设置viewpager不可滑动

        ToolbarUtils.with(this, mToolbar)
                .setSupportBack(false)
                .setTitle("老张超信", true)
                .build();
    }

    @OnClick({R2.id.rb_message, R2.id.rb_contacts})
    public void onClick(View view) {
        if (view.getId() == R.id.rb_message) {
            viewPager.setCurrentItem(0, false);
        } else if (view.getId() == R.id.rb_contacts) {
            viewPager.setCurrentItem(1, false);
        }
    }

    private void initViewPager() {
        messageFragment = new MessageFragment();
        contactsFragment = new ContactsFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(messageFragment);
        fragmentList.add(contactsFragment);
        MyFragmentPagerAdapter pageAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        //ViewPager设置适配器
        viewPager.setAdapter(pageAdapter);
        //ViewPager显示第一个Fragment000
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_message);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_contacts);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        ExitApp();
    }

    public void ExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastyUtil.infoShort("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }

    //登录openfile服务
    private void loginSuperSmack() {
        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
        final String account = helper.getUserName();
        final String passWord = helper.getPassWord();

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
                        ToastyUtil.successShort(loginResult.isSuccess() ? "连接openFile成功" : "连接openFile失败");
                    }
                });
    }
}
