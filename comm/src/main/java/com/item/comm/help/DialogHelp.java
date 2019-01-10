package com.item.comm.help;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.item.comm.R;


public class DialogHelp {
    public static MaterialDialog createProgressDialog(AppCompatActivity activity, DialogInterface.OnCancelListener cancelListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                // .title(R.string.comm_loading_wait_msg)
                .content(R.string.comm_loading_wait_msg)
                .cancelable(cancelListener == null)
                .cancelListener(cancelListener)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .build();
        return dialog;
    }
}
