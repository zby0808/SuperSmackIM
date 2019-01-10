package com.item.comm.form;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class FieldSpinner extends AppCompatSpinner implements IFormField {

    List<DictField> datas = new ArrayList<>();

    public FieldSpinner(Context context) {
        super(context);
    }

    public FieldSpinner(Context context, int mode) {
        super(context, mode);
    }

    public FieldSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FieldSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public FieldSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }

    //设置数据源  难_1,女_2,check_1
    public FieldSpinner(Context context, String spinnerContent) {
        super(context);
        String defautSelectValue = null;
        if (spinnerContent != null) {
            for (String name_value : spinnerContent.split(",")) {
                final String[] split = name_value.split("_");
                if ("check".equals(split[0])) {
                    defautSelectValue = split[1];
                    continue;
                }
                datas.add(new DictField(split[0], split[1]));
            }

            attachDataSource();
            setVaule(defautSelectValue);
        }
    }

    @Override
    public String getValue() {
        final int selectedItemPosition = getSelectedItemPosition();
        if (selectedItemPosition > -1) {
            return datas.get(selectedItemPosition).getValue();
        } else {
            return null;
        }

    }

    @Override
    public void setVaule(@NonNull String value) {
        if (value == null) return;
        int i = 0;
        for (DictField data : datas) {
            if (value.equals(data.getValue())) {
                setSelection(i);
                break;
            }
            i++;
        }
    }


    public void setData(List<DictField> datas, String defautSelectValue) {
        this.datas = datas;
        attachDataSource();
        setVaule(defautSelectValue);
    }

    public void setData(List<DictField> datas) {
        setData(datas, null);
    }

    private void attachDataSource() {
        ArrayAdapter<DictField> adapter = new ArrayAdapter<DictField>(getContext(), android.R.layout.simple_spinner_item, datas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(adapter);
    }
}
