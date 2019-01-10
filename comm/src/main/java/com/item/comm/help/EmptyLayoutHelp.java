package com.item.comm.help;

import android.view.View;
import android.view.ViewGroup;

import com.classic.common.MultipleStatusView;
import com.item.comm.R;


public class EmptyLayoutHelp {
    public static void showLoading(MultipleStatusView statefulLayout) {
        statefulLayout.showLoading(R.layout.comm_loading_view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public static void showEmpty(MultipleStatusView statefulLayout, View.OnClickListener onClickListener) {
        statefulLayout.showEmpty(R.layout.comm_empty_view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        statefulLayout.setOnRetryClickListener(onClickListener);
    }

    public static void showEmpty(MultipleStatusView statefulLayout) {
        showEmpty(statefulLayout, null);
    }

    public static void showError(MultipleStatusView statefulLayout, View.OnClickListener onClickListener) {
        statefulLayout.showError(R.layout.comm_error_view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        statefulLayout.setOnRetryClickListener(onClickListener);
    }

    public static void showError(MultipleStatusView statefulLayout) {
        showError(statefulLayout, null);
    }

    public static void showContent(MultipleStatusView statefulLayout) {
        statefulLayout.showContent();
    }
}
