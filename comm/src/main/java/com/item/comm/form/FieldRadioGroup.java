package com.item.comm.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class FieldRadioGroup extends RadioGroup implements IFormField {

    List<DictField> datas = new ArrayList<>();


    public FieldRadioGroup(Context context, String radioButtons) {
        super(context);
        if (radioButtons != null) {
            String defaultCheck = null;
            for (String name_value : radioButtons.split(",")) {
                final String[] split = name_value.split("_");
                if ("check".equals(split[0])) {
                    defaultCheck = split[1];
                    continue;
                }
                datas.add(new DictField(split[0], split[1]));
            }
            inflateView(defaultCheck);
        }

    }

    public FieldRadioGroup(Context context) {
        super(context);
    }

    public FieldRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void inflateView(String defaultCheck) {
        setOrientation(HORIZONTAL);
        final LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        for (DictField dict : datas) {
            final FieldRadioButton fieldRadioButton = new FieldRadioButton(getContext(), dict.getName(), dict.getValue());
            fieldRadioButton.setId(View.generateViewId());
            addView(fieldRadioButton, layoutParams);
        }

        //设置默认值
        if (defaultCheck != null) {
            setVaule(defaultCheck);
        }


    }

    @Override
    public String getValue() {
        View view = findViewById(getCheckedRadioButtonId());
        if (view != null && view instanceof IFormField) {
            return ((IFormField) view).getValue();
        }
        return null;
    }

    @Override
    public void setVaule(String value) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof FieldRadioButton) {
                if (value != null && ((FieldRadioButton) view).getValue().equals(value))
                    ((FieldRadioButton) view).setChecked(true);
            }
        }

    }

    public void setData(List<DictField> datas, String defaultCheck) {
        this.datas = datas;
        inflateView(defaultCheck);
    }
}
