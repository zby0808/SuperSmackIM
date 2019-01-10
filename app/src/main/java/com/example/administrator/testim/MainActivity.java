package com.example.administrator.testim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.item.comm.base.BaseActivity;
import com.item.comm.base.BasePresenter;
import com.item.comm.util.ToastyUtil;
import com.item.comm.util.ToolbarUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import myproject.smack.fragment.SmackIMFragment;


public class MainActivity extends BaseActivity<BasePresenter> {

    @BindView(R.id.tool_bar_root)
    RelativeLayout mBarContainer;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.id_drawer)
    LinearLayout drawerRootView;
    @BindView(R.id.view_pager)
    NoScrollViewpager viewPager;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.rb_message)
    RadioButton rbMessage;
    @BindView(R.id.rb_contacts)
    RadioButton rbContacts;

    private ArrayList<Fragment> fragmentList;
    private long exitTime = 0;

    private SmackIMFragment smackIMFragment;
    private ContactsFragment contactsFragment;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        mBarContainer.setVisibility(View.GONE);
        init();
        initViewPager();
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

    @OnClick({R.id.rb_message, R.id.rb_contacts})
    public void onClick(View view) {
        if (view.getId() == R.id.rb_message) {
            viewPager.setCurrentItem(0, false);
        } else if (view.getId() == R.id.rb_contacts) {
            viewPager.setCurrentItem(1, false);
        }
    }

    private void initViewPager() {
        smackIMFragment = new SmackIMFragment();
        Bundle bundle = new Bundle();
        bundle.putString("spConfig", Constants.SP_FILENAME);
        smackIMFragment.setArguments(bundle);
        contactsFragment = new ContactsFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(smackIMFragment);
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
}
