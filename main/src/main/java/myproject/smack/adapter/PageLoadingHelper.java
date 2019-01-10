package myproject.smack.adapter;


import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import myproject.R;
import myproject.smack.myview.LoadingView;

/**
 * 页面加载的时候，页面加载进度条及加载失败后的处理
 *
 * Created by zby on 2018/12/3.
 */
public class PageLoadingHelper {
    private FrameLayout mLoadingFailedLayout;
    private LoadingView mLoadingView;
    private TextView mLoadingFailedText;

    public PageLoadingHelper(View rootView) {

        mLoadingFailedLayout = (FrameLayout) rootView.findViewById(R.id.loading_failed_layout);
        mLoadingView = (LoadingView) rootView.findViewById(R.id.loadingView);
        mLoadingFailedText = (TextView) rootView.findViewById(R.id.errorView);
    }

    public void setOnLoadingClickListener(final View.OnClickListener onClickListener) {

        this.mLoadingFailedText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(onClickListener != null) {
                    mLoadingFailedText.setVisibility(View.GONE);
                    onClickListener.onClick(v);
                }
            }
        });
    }

    public void setLoadingFailedText(String failedText) {

        mLoadingFailedText.setText(failedText);
    }

    /**
     * 开始加载数据
     */
    public void startRefresh() {

        if(mLoadingView.getVisibility() == View.GONE) {
            mLoadingFailedLayout.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.VISIBLE);
            mLoadingView.start();
            mLoadingFailedText.setVisibility(View.GONE);
        }
    }

    /**
     * 加载失败
     */
    public void refreshFailed() {

        if(mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.stop();
            mLoadingView.setVisibility(View.GONE);
            mLoadingFailedText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载成功
     */
    public void refreshSuccess() {

        if(mLoadingFailedLayout.getVisibility() == View.VISIBLE) {
            mLoadingView.stop();
            mLoadingFailedLayout.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.GONE);
            mLoadingFailedText.setVisibility(View.GONE);
        }
    }
}
