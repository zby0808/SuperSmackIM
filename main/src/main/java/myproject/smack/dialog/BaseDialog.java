package myproject.smack.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import myproject.R;


/**
 * Created by zby on 2018/4/23.
 */

public abstract class BaseDialog {
    public Dialog mDialog;
    public Context context;
    private Window window;

    /**
     * @param context 注：此context对象必须是当前dialog所在的activity context,不能用application中context
     */
    public BaseDialog(Context context) {
        this.context = context;
    }

    public abstract void initView();

    public abstract <T> void initData(T object);

    /**
     * 显示对话框
     *
     * @param menuView 自定义dialog view
     * @param isCancel 点击屏幕是否可取消
     */
    public void openDialog(View menuView, boolean isCancel) {
        mDialog = new Dialog(context, R.style.public_dialog);
        menuView.measure(0, 0);
        mDialog.setContentView(menuView);//加载布局
//        mDialog.setCancelable(false);//点击屏幕和返回键都不会消失
        mDialog.setCanceledOnTouchOutside(isCancel);//设置点击外部不可取消，点击返回可以取消
        setDialogSize(menuView);
        mDialog.show();
    }

    /**
     * 显示对话框
     *
     * @param menuView 自定义dialog view
     *                 默认点击屏幕可以取消
     */
    public void openDialog(View menuView) {
        mDialog = new Dialog(context, R.style.public_dialog);
        menuView.measure(0, 0);
        mDialog.setContentView(menuView);//加载布局
        mDialog.setCanceledOnTouchOutside(true);
        setDialogSize(menuView);
    }

    public void show() {
        if (mDialog != null) mDialog.show();
    }

    /**
     * 获取dialog在窗口的高度
     */
    public void setDialogSize(View menuView) {
        window = mDialog.getWindow();
        // window.setGravity(Gravity.TOP);//位置
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

        lp.height = menuView.getMeasuredHeight();
        lp.width = menuView.getMeasuredWidth();
        window.setAttributes(lp);
    }

    public void cancelDialog() {
        mDialog.cancel();
    }
}
