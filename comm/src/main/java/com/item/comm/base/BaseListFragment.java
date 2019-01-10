package com.item.comm.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.item.comm.R;
import com.item.comm.constant.LayoutStyleCode;
import com.item.comm.constant.NetRequestCode;
import com.item.comm.help.recycleview.RecyclerViewHelper;

import java.util.List;

import project.utils.PLog;

public abstract class BaseListFragment<A extends BaseQuickAdapter<T, ? extends BaseViewHolder>, T, P extends BasePresenter> extends BaseFragment<P> implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //请求
    protected static final int REQUEST_STATE_REFRESH = NetRequestCode.NET_REQUEST_1;//下拉刷新
    protected static final int REQUEST_STATE_LOADMORE = NetRequestCode.NET_REQUEST_2;//上拉加载更多

    //返回
    private static final int RESPONSE_STATE_START = 10;//开始状态
    private static final int RESPONSE_STATE_ERROR = 11;//错误状态
    private static final int RESPONSE_STATE_CANCEL = 12;//取消状态
    private static final int RESPONSE_STATE_COMPLETED = 13;//完成状态
    private static final int RESPONSE_STATE_PARSEING = 14;//解析数据

    //参数
    ListSetupModel setupModel;
    private int pageIndex = 1;

    protected A baseListAdapter;

    //view
    protected SwipeRefreshLayout mRefreshLayout;//这个用来控制下拉刷新的头部
    protected RecyclerView recyclerview;
    View loadingview;
    View notdataview;
    View errorview;


    @Override
    protected int getLayoutId() {
        return R.layout.comm_fragment_baselist;
    }


    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        setupModel = setupParam();
        pageIndex = setupModel.getPageIndex();
        isLasy = setupModel.isLasy();
        isRefreshAlways = setupModel.isRefreshAlways();
        initView(view);
        initListener();
    }


    protected ListSetupModel setupParam() {
        return new ListSetupModel.Builder().build();
    }

    protected void initView(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        initRefreshLayout();
        initRecyclerView();

        loadingview = activity.getLayoutInflater().inflate(R.layout.comm_loading_view, (ViewGroup) recyclerview.getParent(), false);
        notdataview = activity.getLayoutInflater().inflate(R.layout.comm_empty_view, (ViewGroup) recyclerview.getParent(), false);
        errorview = activity.getLayoutInflater().inflate(R.layout.comm_error_view, (ViewGroup) recyclerview.getParent(), false);
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

    @Override
    protected void requestData() {
        requestListData(REQUEST_STATE_REFRESH);
    }

    protected abstract void requestData(int requestCode, int pageIndex, int pageSize);

    private void initRecyclerView() {
        baseListAdapter = getAdapter();
        baseListAdapter.setOnItemClickListener(this);
        int layoutStyle = setupModel.getLayoutStyle();
        if (layoutStyle == LayoutStyleCode.LAYOUT_STAGGERED) {
            RecyclerViewHelper.initRecyclerViewSV(recyclerview, setupModel.isDivided(), baseListAdapter, setupModel.getSpanCount());
        } else if (layoutStyle == LayoutStyleCode.LAYOUT_GRID) {
            RecyclerViewHelper.initRecyclerViewC(recyclerview, setupModel.isDivided(), baseListAdapter, setupModel.getSpanCount(), R.color.white);
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


}
