package com.item.comm.form;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.item.comm.R;


public class FieldRadioButton extends AppCompatRadioButton implements IFormField {
    String value;

    public FieldRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public FieldRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FieldRadioButton(Context context, String text, String value) {
        this(context, null);
        setText(text);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setVaule(String value) {
        this.value = value;
    }
}
