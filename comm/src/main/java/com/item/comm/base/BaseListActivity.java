package com.item.comm.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.item.comm.R;
import com.item.comm.constant.LayoutStyleCode;
import com.item.comm.constant.NetRequestCode;
import com.item.comm.help.recycleview.RecyclerViewHelper;
import java.util.List;

import butterknife.ButterKnife;
import project.utils.PLog;

public abstract class BaseListActivity<A extends BaseQuickAdapter<T, ? extends BaseViewHolder>, T, P extends BasePresenter> extends BaseActivity<P> implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //请求
    protected static final int REQUEST_STATE_REFRESH = NetRequestCode.NET_REQUEST_1;//下拉刷新
    protected static final int REQUEST_STATE_LOADMORE = NetRequestCode.NET_REQUEST_2;//上拉加载更多

    //返回
    private static final int RESPONSE_STATE_START = 10;//开始状态
    private static final int RESPONSE_STATE_ERROR = 11;//错误状态
    private static final int RESPONSE_STATE_CANCEL = 12;//取消状态
    private static final int RESPONSE_STATE_COMPLETED = 13;//完成状态
    private static final int RESPONSE_STATE_PARSEING = 14;//解析数据

    ListSetupModel setupModel;
    private int pageIndex = 1;
    //是否是第一次加载数据
    private boolean isFirstLoadData = true;

    protected Toolbar toolbar;

    SwipeRefreshLayout mRefreshLayout;//这个用来控制下拉刷新的头部

    RecyclerView recyclerview;

    public RelativeLayout mRootView;


    protected A baseListAdapter;

    View loadingview;
    View notdataview;
    View errorview;


    @Override
    protected int getLayoutId() {
        return R.layout.comm_activity_baselist;
    }

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        setupModel = setupParam();
        pageIndex = setupModel.getPageIndex();
        initView();
        initListener();
        if (isFirstLoadData) {
            isFirstLoadData = false;
            requestListData(REQUEST_STATE_REFRESH);
        }
    }


    protected ListSetupModel setupParam() {
        return new ListSetupModel.Builder().build();
    }

    protected void initView() {
        recyclerview = ButterKnife.findById(this, R.id.recyclerview);
        mRefreshLayout = ButterKnife.findById(this, R.id.swiperefreshlayout);
        mRootView = ButterKnife.findById(this, R.id.rl_baselist_root);
        toolbar = ButterKnife.findById(this, R.id.tool_bar);
        initRefreshLayout();
        initRecyclerView();

        loadingview = this.getLayoutInflater().inflate(R.layout.comm_loading_view, (ViewGroup) recyclerview.getParent(), false);
        notdataview = this.getLayoutInflater().inflate(R.layout.comm_empty_view, (ViewGroup) recyclerview.getParent(), false);
        errorview = this.getLayoutInflater().inflate(R.layout.comm_error_view, (ViewGroup) recyclerview.getParent(), false);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.comm_pink, R.color.comm_green, R.color.comm_amber,
                R.color.comm_red, R.color.comm_blue, R.color.comm_yellow);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnabled(setupModel.isEnableRefresh());
    }

    protected void initListener() {
        notdataview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestListData(REQUEST_STATE_REFRESH);
            }
        });

        errorview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestListData(REQUEST_STATE_REFRESH);
            }
        });
    }

    protected abstract A getAdapter();


    protected void requestListData(int requestCode) {
        if (requestCode == REQUEST_STATE_REFRESH) {
            pageIndex = setupModel.getPageIndex();
        }
        requestData(requestCode, pageIndex, setupModel.getPageSize());
    }

    protected abstract void requestData(int requestCode, int pageIndex, int pageSize);


    private void initRecyclerView() {
        baseListAdapter = getAdapter();
        baseListAdapter.setOnItemClickListener(this);
        int layoutStyle = setupModel.getLayoutStyle();
        if (layoutStyle == LayoutStyleCode.LAYOUT_STAGGERED) {
            RecyclerViewHelper.initRecyclerViewSV(recyclerview, setupModel.isDivided(), baseListAdapter, setupModel.getSpanCount());
        } else if (layoutStyle == LayoutStyleCode.LAYOUT_GRID) {
            RecyclerViewHelper.initRecyclerViewG(recyclerview, setupModel.isDivided(), baseListAdapter, setupModel.getSpanCount());
        } else {
            RecyclerViewHelper.initRecyclerViewV(recyclerview, setupModel.isDivided(), baseListAdapter);
        }
        baseListAdapter.setEnableLoadMore(setupModel.isEnableLoadMore());
        baseListAdapter.setOnLoadMoreListener(baseListAdapter.isLoadMoreEnable() ? this : null, recyclerview);
    }


    @Override
    public void doOnStart(int requestCode) {
        refreshui(requestCode, RESPONSE_STATE_START);
        PLog.d("doOnStart");
    }

    @Override
    public void doParseData(@NetRequestCode int requestCode, Object data) {
        refreshui(requestCode, RESPONSE_STATE_PARSEING);
        baseListAdapter.addData((List<T>) data);
        PLog.d("doParseData");
    }

    @Override
    public void doOnError(@NetRequestCode int requestCode, String msg, Throwable e) {
        refreshui(requestCode, RESPONSE_STATE_ERROR);
        super.doOnError(requestCode, msg, e);
    }

    @Override
    public void doOnCompleted(int requestCode) {
        refreshui(requestCode, RESPONSE_STATE_COMPLETED);
        PLog.d("doOnCompleted");
    }

    @Override
    public void doOnCancel(int requestCode) {
        refreshui(requestCode, RESPONSE_STATE_CANCEL);
        PLog.d("doOnCancel");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
    }


    //返回的
    //break;
    private void refreshui(int requestcode, int state) {
        if (requestcode == REQUEST_STATE_REFRESH) {
            switch (state) {
                case RESPONSE_STATE_START:
                    if (mRefreshLayout.isEnabled()) {
                        mRefreshLayout.setRefreshing(true);//这句话和你手动下拉起一个作用，都是显示一个下拉的按钮，但是手动下拉是会触发事件，这个只是一个ui更新

                    } else {
                        baseListAdapter.setEmptyView(loadingview);
                    }
                    break;
                case RESPONSE_STATE_ERROR:
                    mRefreshLayout.setRefreshing(false);
                    baseListAdapter.setEmptyView(errorview);
                    break;
                case RESPONSE_STATE_CANCEL:
                    mRefreshLayout.setRefreshing(false);
                    break;

                case RESPONSE_STATE_COMPLETED:
                    pageIndex++;
                    int size = baseListAdapter.getData().size();
                    if (size == 0) {
                        baseListAdapter.setEmptyView(notdataview);
                    }
                    break;
                case RESPONSE_STATE_PARSEING:
                    baseListAdapter.setNewData(null);
                    break;
            }
        } else if (requestcode == REQUEST_STATE_LOADMORE) {
            switch (state) {
                case RESPONSE_STATE_START:
                    break;
                case RESPONSE_STATE_ERROR:
                    baseListAdapter.loadMoreFail();
                    break;
                case RESPONSE_STATE_CANCEL:
                    baseListAdapter.loadMoreComplete();
                    break;
                case RESPONSE_STATE_COMPLETED:
                    pageIndex++;
                    baseListAdapter.loadMoreComplete();
                    break;
                case RESPONSE_STATE_PARSEING:
                    break;
            }

        }

    }

    @Override
    public void onRefresh() {
        requestListData(REQUEST_STATE_REFRESH);
    }

    @Override
    public void onLoadMoreRequested() {
        if (baseListAdapter.getData().size() < (pageIndex - setupModel.getPageIndex()) * setupModel.getPageSize()) {
            baseListAdapter.loadMoreEnd();
        } else {
            requestListData(REQUEST_STATE_LOADMORE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirstLoadData && setupModel.isRefreshAlways()) {//刷新
            isFirstLoadData = false;
            requestListData(REQUEST_STATE_REFRESH);
        }
    }

    @Override
    protected void onStop() {
        if (setupModel.isRefreshAlways()) {
            isFirstLoadData = true;
        }
        super.onStop();
    }
}
