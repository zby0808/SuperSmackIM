package com.item.comm.form;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.util.AttributeSet;

import com.item.comm.R;
import com.item.comm.util.InputFilterHelp;



public class FieldEditText extends AppCompatEditText implements IFormField {

    public FieldEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public FieldEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public FieldEditText(int editextFilter, Context context) {
        this(context, null);
        setFilter(editextFilter);


    }

    private void init() {
        setTextSize(16);
        setTextColor(ContextCompat.getColor(getContext(), R.color.comm_text_half_blace));
        setBackground(null);
    }

    private void setFilter(int editextFilter) {
        switch (editextFilter) {
            case 1:
                setFilters(new InputFilter[]{InputFilterHelp.getLetterOrDigitInputFilter(), new InputFilter.LengthFilter(18)});
                break;
        }

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
