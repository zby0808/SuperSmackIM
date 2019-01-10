package com.item.comm.form;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.blankj.utilcode.util.SizeUtils;
import com.item.comm.R;


public class FieldTextView extends AppCompatTextView implements IFormField {
    public FieldTextView(String initContent, Context context) {
        this(context, null);
        setText(initContent);
    }

    public FieldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public FieldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTextSize(16);
        setGravity(Gravity.CENTER_VERTICAL);
        setTextColor(ContextCompat.getColor(getContext(), R.color.comm_text_half_blace));
        setCompoundDrawablePadding(SizeUtils.dp2px(5));
        setPadding(SizeUtils.dp2px(2), 0, 0, 0);
    }


    @Override
    public String getValue() {
        return getText().toString();
    }

    @Override
    public void setVaule(String value) {
        setText(value);
    }
}
