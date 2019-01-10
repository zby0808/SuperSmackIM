package com.item.comm.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.item.comm.R;


public class FieldView extends LinearLayout {
    public static final int TYPE_EDITTEXT = 1;
    public static final int TYPE_TEXTVIEW = 2;
    public static final int TYPE_RADIOGROUP = 3;
    public static final int TYPE_SPINNER = 4;
    public static final int TYPE_HIDDENVIEW = 5;


    public String getmLabel() {
        return mLabel.replace(":", "").replace(" ", "").trim();
    }

    private LinearLayout contentView;
    View dataView = null;
    private Builder builder;

    private String mLabel;
    private int labelColor;
    private float labelWidth;
    private boolean mustInput;
    private boolean fieldDivided;
    private int contentViewType;
    private int editextFilter;
    @DrawableRes
    private int textIcon;
    private int fieldIndex = -1;
    private String fieldName;
    private String initContent;
    private float fieldMinHeight;
    private String edittext_hint;
    private int edittext_line;
    private int edittext_textlength;


    private FieldView(Context context, Builder builder) {
        super(context);
        initBuilder(builder);
    }

    public FieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.comm_FieldView);
        Builder builder = newBuilder(ta.getString(R.styleable.comm_FieldView_comm_label), ta.getString(R.styleable.comm_FieldView_comm_name))
                .initContent(ta.getString(R.styleable.comm_FieldView_comm_init_content))
                .mustInput(ta.getBoolean(R.styleable.comm_FieldView_comm_must, false))
                .fieldDivided(ta.getBoolean(R.styleable.comm_FieldView_comm_divided, false))
                .contentViewType(ta.getInteger(R.styleable.comm_FieldView_comm_contentType, -1))
                .editextFilter(ta.getInteger(R.styleable.comm_FieldView_comm_editextFilter, -1))
                .textIcon(ta.getResourceId(R.styleable.comm_FieldView_comm_textIcon, -1))
                .fieldIndex(ta.getInteger(R.styleable.comm_FieldView_comm_field_index, -1))
                .editHint(ta.getString(R.styleable.comm_FieldView_comm_edittext_hint))
                .editLine(ta.getInt(R.styleable.comm_FieldView_comm_edittext_line, 1))
                .editLength(ta.getInt(R.styleable.comm_FieldView_comm_edittext_textlength, -1));
        ta.recycle();
        initFieldView(builder);
    }

    public void initBuilder(Builder builder) {
        this.builder = builder;
        mLabel = builder.mLabel;
        labelColor = builder.labelColor;
        labelWidth = builder.labelWidth;
        mustInput = builder.mustInput;
        fieldDivided = builder.fieldDivided;
        fieldMinHeight = builder.fieldMinHeight;
        contentViewType = builder.contentViewType;
        editextFilter = builder.editextFilter;
        textIcon = builder.textIcon;
        fieldName = builder.fieldName;
        initContent = builder.initContent;
        if (builder.fieldIndex >= 0 && builder.fieldIndex % 2 != 0) {
            builder.fieldIndex += 1;
        }
        fieldIndex = builder.fieldIndex;
        dataView = builder.dataView;
        edittext_hint = builder.editHint;
        edittext_line = builder.editLine;
        edittext_textlength = builder.editLength;
    }

    public void initFieldView(Builder builder) {
        initBuilder(builder);
        init();
    }

    public static Builder newBuilder(String mLabel, String name) {
        return new Builder(mLabel, name);
    }

    public Builder getBuilder() {
        return builder;
    }

    protected void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setMinimumHeight((int) fieldMinHeight);
        removeAllViews();
        if (contentView != null) contentView.removeAllViews();

        initLabelView(-1);//labelview  0
        initDataView();//dataview    等等  1
        initIconView(-1);  //2
        initMustView(-1);  //3
    }

    private void initIconView(int index) {
        if (textIcon != -1) {
            AppCompatImageView icon = new AppCompatImageView(getContext());
            icon.setImageResource(textIcon);
            final LayoutParams layoutParams = new LayoutParams(SizeUtils.dp2px(18), SizeUtils.dp2px(18));
            layoutParams.leftMargin = SizeUtils.dp2px(5);
            addView(icon, index, layoutParams);
        }

    }

    private void initMustView(int index) {
        AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
        appCompatImageView.setImageResource(R.mipmap.ic_red_star);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = SizeUtils.dp2px(5);
        addView(appCompatImageView, index, layoutParams);
        appCompatImageView.setVisibility(mustInput ? VISIBLE : INVISIBLE);
    }

    private void initDataView() {
        contentView = new LinearLayout(getContext());
        contentView.setOrientation(VERTICAL);
        final LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        layoutParams.leftMargin = SizeUtils.dp2px(5);
        addView(contentView, 1, layoutParams);
        initContentViewByType();
        addDataView();
    }

    private void initLabelView(int index) {
        AppCompatTextView mLabelView = new AppCompatTextView(getContext());
        mLabelView.setTextSize(16);
        mLabelView.setGravity(Gravity.CENTER_VERTICAL);
        mLabelView.setMinimumHeight((int) fieldMinHeight);
        mLabelView.setTextColor(ContextCompat.getColor(getContext(), R.color.comm_grey900));
        mLabelView.setBackgroundColor(ContextCompat.getColor(getContext(), labelColor != -1 ? labelColor : R.color.comm_grey200));
        mLabelView.setText(mLabel.replace(":", "") + ":");
        final LayoutParams layoutParams = new LayoutParams(labelWidth != -1 ? (int) labelWidth : LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        addView(mLabelView, index, layoutParams);
    }

    private void addDataView() {
        if (dataView != null) {
            final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            contentView.addView(dataView, layoutParams);
            if (fieldDivided) {
                View line = new View(getContext());
                line.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.line));
                final LayoutParams lineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, SizeUtils.dp2px(0.5f));
                contentView.addView(line, lineLayoutParams);
            }


        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (child instanceof IFormField) {
            dataView = child;
            builder.dataView = dataView;
            removeView(child);
            addDataView();
        } else {
            throw new RuntimeException("FieldView的个子类必须实现IFormField接口");
        }

    }


    private void initContentViewByType() {
        switch (contentViewType) {
            case TYPE_EDITTEXT:
                FieldEditText temp = new FieldEditText(editextFilter, getContext());
                temp.setHint(edittext_hint);
                temp.setLines(edittext_line);
                if (edittext_textlength != -1) {
                    temp.setMaxEms(edittext_textlength);
                }

                dataView = temp;
                break;
            case TYPE_TEXTVIEW:
                dataView = new FieldTextView(initContent, getContext());
                break;
            case TYPE_RADIOGROUP:
                dataView = new FieldRadioGroup(getContext(), initContent);
                break;
            case TYPE_SPINNER:
                dataView = new FieldSpinner(getContext(), initContent);
                break;
            case TYPE_HIDDENVIEW:
                dataView = new HiddenFieldView(initContent, getContext());
                break;
        }
        builder.dataView = dataView;
    }


    public boolean isMustInput() {
        return mustInput;
    }

    public String getValue() {
        return ((IFormField) dataView).getValue();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fileName) {
        this.fieldName = fileName;
    }

    public void setValue(String value) {
        ((IFormField) dataView).setVaule(value);
    }

    public View getDataView() {
        return dataView;
    }

    public static final class Builder {
        private String mLabel;
        private int labelColor = -1;
        private float labelWidth = -1;
        private boolean mustInput = false;
        private boolean fieldDivided = false;
        public int contentViewType = -1;
        private int editextFilter;
        private int textIcon = -1;
        public int fieldIndex = -1;
        private String fieldName;
        private String initContent;
        private View dataView;
        private float fieldMinHeight = SizeUtils.dp2px(48);

        private String editHint;
        private int editLine;
        private int editLength;

        private Builder(String mLabel, String fieldName) {
            this.mLabel = mLabel;
            this.fieldName = fieldName;
        }

        public Builder editHint(String val) {
            editHint = val;
            return this;
        }

        public Builder editLine(int val) {
            editLine = val;
            return this;
        }

        public Builder editLength(int val) {
            editLength = val;
            return this;
        }

        public Builder labelColor(@ColorRes int val) {
            labelColor = val;
            return this;
        }

        public Builder labelWidth(float val) {
            labelWidth = val;
            return this;
        }


        public Builder mustInput(boolean val) {
            mustInput = val;
            return this;
        }

        public Builder fieldDivided(boolean val) {
            fieldDivided = val;
            return this;
        }

        public Builder contentViewType(int val) {
            contentViewType = val;
            return this;
        }

        public Builder fieldIndex(@Size(min = 0) int val) {
            fieldIndex = val;
            return this;
        }

        public Builder editextFilter(int val) {
            editextFilter = val;
            return this;
        }

        public Builder fieldMinHeight(float val) {
            fieldMinHeight = val;
            return this;
        }

        public Builder textIcon(@DrawableRes int val) {
            textIcon = val;
            return this;
        }

        public Builder initContent(String val) {
            initContent = val;
            return this;
        }

        public Builder dataView(View val) {
            if (!(dataView instanceof IFormField)) {
                throw new RuntimeException("FieldView的个子类必须实现IFormField接口");
            }
            dataView = val;
            return this;
        }

        public FieldView build(Context context) {
            if (contentViewType == -1 && dataView == null) {
                throw new RuntimeException("FieldView的子类有且只有一个");
            }
            return new FieldView(context, this);
        }
    }
}
