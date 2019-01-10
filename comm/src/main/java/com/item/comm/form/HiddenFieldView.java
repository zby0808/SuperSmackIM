package com.item.comm.form;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class HiddenFieldView extends AppCompatTextView implements IFormField, IHiddenField {
    public HiddenFieldView(String initContent, Context context) {
        this(context, null);
        setText(initContent);
    }

    public HiddenFieldView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public HiddenFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        hidden();
    }


    @Override
    public String getValue() {
        return getText().toString();
    }

    @Override
    public void setVaule(String value) {
        setText(value);
    }


    @Override
    public void hidden() {
        setVisibility(GONE);
    }
}
