package myproject.smack.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import myproject.R;


/**
 * Created by bangyong.zhang on 2018/6/4.
 * 通用提示dialog
 */

public class ConfirmDialog extends BaseDialog implements View.OnClickListener {
    private TextView titleView, contentView, cancelView, sureView;
    private ImageView cancelBtn;

    /**
     * @param context 注：此context对象必须是当前dialog所在的activity context,不能用application中context
     */
    public ConfirmDialog(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        View menuView = View.inflate(context, R.layout.dialog_public_confirm, null);
        titleView = menuView.findViewById(R.id.tv_dialog_title);
        contentView = menuView.findViewById(R.id.tv_dialog_content);
        cancelView = menuView.findViewById(R.id.tv_dialog_cancel);
        sureView = menuView.findViewById(R.id.tv_dialog_sure);
        cancelBtn = menuView.findViewById(R.id.btn_cancel);

        cancelView.setOnClickListener(this);
        sureView.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        openDialog(menuView);
    }


    /**
     * @param title   提示标题
     * @param content 提示内容
     * @param cancel  关闭按钮
     * @param sure    确认按钮
     */
    public void setDialogData(String title, String content, String cancel, String sure) {
        if (!TextUtils.isEmpty(title)) titleView.setText(title);
        if (!TextUtils.isEmpty(content)) contentView.setText(content);
        if (!TextUtils.isEmpty(cancel)) cancelView.setText(cancel);
        if (!TextUtils.isEmpty(sure)) sureView.setText(sure);
    }

    /**
     * 是否显示关闭按钮，默认隐藏
     *
     * @param isShow
     */
    public void showCancelBtn(boolean isShow) {
        cancelBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_dialog_cancel) {
            if (listener != null) {
                listener.cancel();
                cancelDialog();
            }

        } else if (view.getId() == R.id.tv_dialog_sure) {
            if (listener != null) {
                listener.sure();
                cancelDialog();
            }

        } else if (view.getId() == R.id.btn_cancel) {
            mDialog.cancel();
        }
    }

    ConfirmDialogListener listener;

    public void setConfirmDialogListener(ConfirmDialogListener listener) {
        this.listener = listener;
    }

    public interface ConfirmDialogListener {
        void cancel();//取消

        void sure();//确认
    }

    @Override
    public <T> void initData(T object) {

    }
}
