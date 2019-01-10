package com.item.comm.base;

import android.support.annotation.IntRange;
import android.support.annotation.Size;

import com.item.comm.constant.LayoutStyleCode;

public class ListSetupModel {
    private int pageSize;
    private int pageIndex;
    private boolean isEnableLoadMore;
    private boolean isEnableRefresh;
    private boolean isDivided;
    private int layoutStyle;
    private int spanCount;
    private boolean isLasy;
    private boolean isRefreshAlways;

    private ListSetupModel(Builder builder) {
        pageSize = builder.pageSize;
        pageIndex = builder.pageIndex;
        isEnableLoadMore = builder.isEnableLoadMore;
        isEnableRefresh = builder.isEnableRefresh;
        isDivided = builder.isDivided;
        layoutStyle = builder.layoutStyle;
        spanCount = builder.spanCount;
        isLasy = builder.isLasy;
        isRefreshAlways = builder.isRefreshAlways;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        //页大小
        private int pageSize = 20;
        //初始化页
        private int pageIndex = 1;
        //是否加载更多
        private boolean isEnableLoadMore = true;
        //是否支持下拉刷新
        private boolean isEnableRefresh = true;
        //是否有分割线
        private boolean isDivided = true;
        //线性还是网格
        @LayoutStyleCode
        private int layoutStyle = LayoutStyleCode.LAYOUT_LINEAR;
        private int spanCount = 3;
        //默认不是懒加载
        private boolean isLasy = false;
        //是否每次可见的时候都刷新一次
        private boolean isRefreshAlways = false;

        public Builder() {
        }


        public Builder pageSize(@Size(min = 20) int val) {
            pageSize = val;
            return this;
        }


        public Builder pageIndex(@Size(min = 1) int val) {
            pageIndex = val;
            return this;
        }

        public Builder isEnableLoadMore(boolean val) {
            isEnableLoadMore = val;
            return this;
        }

        public Builder isEnableRefresh(boolean val) {
            isEnableRefresh = val;
            return this;
        }

        public Builder isDivided(boolean val) {
            isDivided = val;
            return this;
        }

        public Builder layoutStyle(@LayoutStyleCode int val) {
            layoutStyle = val;
            return this;
        }

        public Builder spanCount(@IntRange(from = 2) int val) {
            spanCount = val;
            return this;
        }

        public Builder isLasy(boolean val) {
            isLasy = val;
            return this;
        }

        public Builder isRefreshAlways(boolean val) {
            isRefreshAlways = val;
            return this;
        }

        public ListSetupModel build() {
            return new ListSetupModel(this);
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public boolean isEnableLoadMore() {
        return isEnableLoadMore;
    }

    public boolean isEnableRefresh() {
        return isEnableRefresh;
    }

    public int getLayoutStyle() {
        return layoutStyle;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public boolean isDivided() {
        return isDivided;
    }

    public void setPageSize(@IntRange(from = 1) int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageIndex(@IntRange(from = 1) int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        isEnableLoadMore = enableLoadMore;
    }

    public void setEnableRefresh(boolean enableRefresh) {
        isEnableRefresh = enableRefresh;
    }

    public void setDivided(boolean divided) {
        isDivided = divided;
    }

    public void setLayoutStyle(int layoutStyle) {
        this.layoutStyle = layoutStyle;
    }

    public void setSpanCount(@IntRange(from = 2) int spanCount) {
        this.spanCount = spanCount;
    }

    public boolean isLasy() {
        return isLasy;
    }

    public void setLasy(boolean lasy) {
        isLasy = lasy;
    }

    public boolean isRefreshAlways() {
        return isRefreshAlways;
    }

    public void setRefreshAlways(boolean refreshAlways) {
        isRefreshAlways = refreshAlways;
    }
}
