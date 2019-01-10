package com.item.comm.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.item.comm.R;
import com.item.comm.util.StringUtils2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.item.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_ALL;
import static com.item.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE;
import static com.item.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE_HIDDLEN;

public class FormLayout extends LinearLayout {
    //label
    //背景
    //字体大小
    //字体颜色
    //宽度

    //value
    //背景
    //字体大小
    //字体颜色
    //宽度


    //上下之间
    //top marge
    // line  是否有线
    //线的颜色
    //最小的高度  高度

    //FieldView
    //是否有底部下划线
    //下划线的颜色
    //

    @IntDef({
            FIELD_TYPE_ALL,
            FIELD_TYPE_VISIBLE,
            FIELD_TYPE_VISIBLE_HIDDLEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionFieldType {
        int FIELD_TYPE_ALL = 0;//所有的view
        int FIELD_TYPE_VISIBLE = 1;//仅仅可见的view
        int FIELD_TYPE_VISIBLE_HIDDLEN = 2;//仅仅可见和隐藏的view
    }

    //form
    private float comm_marginTop;
    private boolean mDivided;
    //field
    private float mLabel_width;
    private int mLabel_color;
    private float comm_field_minheight;
    private boolean comm_field_divided;

    public void addFieldView(FieldView.Builder builder) {
        addView(builder.build(getContext()));
    }

    public void addFieldViews(List<FieldView.Builder> fieldViews) {
        for (FieldView.Builder builder : fieldViews) {
            addFieldView(builder);
        }
    }


    public FormLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.comm_FormView);
            mDivided = ta.getBoolean(R.styleable.comm_FormView_comm_form_divided, false);
            comm_field_divided = ta.getBoolean(R.styleable.comm_FormView_comm_field_divided, false);
            mLabel_width = ta.getDimension(R.styleable.comm_FormView_comm_label_width, -1);
            comm_marginTop = ta.getDimension(R.styleable.comm_FormView_comm_marginTop, -1);
            comm_field_minheight = ta.getDimension(R.styleable.comm_FormView_comm_field_minheight, SizeUtils.dp2px(48));
            mLabel_color = ta.getResourceId(R.styleable.comm_FormView_comm_label_color, -1);
            ta.recycle();
        }
    }

    /**
     * visibleType  为true 说明只获取可见的  为false  获取所有的
     *
     * @return
     */
    public List<FieldView> getFieldViews(@ActionFieldType int visibleType) {
        List<FieldView> formFields = new ArrayList<>();
        for (int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if (view instanceof FieldView) {
                FieldView fieldView = (FieldView) view;
                if (visibleType == ActionFieldType.FIELD_TYPE_ALL) {
                    formFields.add(((FieldView) view));
                } else if (visibleType == ActionFieldType.FIELD_TYPE_VISIBLE && fieldView.getVisibility() == View.VISIBLE) {
                    formFields.add(((FieldView) view));
                } else if (visibleType == ActionFieldType.FIELD_TYPE_VISIBLE_HIDDLEN && (fieldView.getVisibility() == View.VISIBLE || fieldView.getDataView() instanceof IHiddenField)) {
                    formFields.add(((FieldView) view));
                }
            }
        }
        return formFields;
    }

    public FieldView getFieldViewByName(@NonNull String name, @ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            if (name.equalsIgnoreCase(fieldView.getFieldName())) {
                return fieldView;
            }
        }
        return null;
    }

    public <T extends FieldView> T getDataViewByName(@NonNull String name, @ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            if (name.equalsIgnoreCase(fieldView.getFieldName())) {
                return (T) fieldView.getDataView();
            }
        }
        return null;
    }


    /**
     * 检查表单样式
     */
    public boolean checkForm(@ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            if (fieldView.isMustInput()) {
                if (TextUtils.isEmpty(fieldView.getValue())) {
                    Toasty.warning(getContext(), fieldView.getmLabel() + "不得为空", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    public void bindData(Object datas, @ActionFieldType int visibleType) {
        bindData(datas, "", visibleType);
    }


    public void bindData(Object datas, String prefix, @ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            try {
                Field field = datas.getClass().getDeclaredField(fieldView.getFieldName().replace(prefix, ""));
                field.setAccessible(true);
                String value = String.valueOf(field.get(datas));
                value = StringUtils2.isEmpty(value) ? "" : value;
                fieldView.setValue(value);
            } catch (Exception e) {
                e.printStackTrace();
                fieldView.setValue("");
            }
        }
    }

    public void bindData(Map<String, Object> params, @ActionFieldType int visibleType) {
        bindData(params, "", visibleType);
    }

    public void bindData(Map<String, Object> params, String prefix, @ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            try {
                String value = (String) params.get(prefix + fieldView.getFieldName());
                value = StringUtils2.isEmpty(value) ? "" : value;
                fieldView.setValue(value);
            } catch (Exception e) {
                e.printStackTrace();
                fieldView.setValue("");
            }
        }
    }

    public void reset(@ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            try {
                fieldView.setValue("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Map<String, Object> getParams(String prefix, @ActionFieldType int visibleType) {
        Map<String, Object> map = new HashMap<>();
        for (FieldView fieldView : getFieldViews(visibleType)) {
            map.put(fieldView.getFieldName().replace(prefix, ""), fieldView.getValue());
        }
        return map;
    }

    public Map<String, Object> getParams(@ActionFieldType int visibleType) {
        return getParams("", visibleType);
    }

    public <T> T getParams(Class<T> clazz, @ActionFieldType int visibleType, String prefix) {
        JSONObject jsonObject = new JSONObject(getParams(prefix, visibleType));
        return jsonObject.toJavaObject(clazz);
    }

    public <T> T getParams(Class<T> clazz, @ActionFieldType int visibleType) {
        return getParams(clazz, visibleType, "");
    }


    @Override
    public void addView(View child) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    private void addViewInternal(final View child) {
        if (child instanceof FieldView) {
            addFieldItemView((FieldView) child);
        } else {
            throw new IllegalArgumentException("Only FieldView instances can be added to FormLayout");
        }
    }

    private void addFieldItemView(FieldView child) {
        final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (comm_marginTop != -1) {
            layoutParams.topMargin = (int) comm_marginTop;
        }

        FieldView.Builder builder = child.getBuilder()
                .labelColor(mLabel_color)
                .labelWidth(mLabel_width)
                .fieldMinHeight(comm_field_minheight)
                .fieldDivided(comm_field_divided);
        child.initFieldView(builder);

        int childCount = getChildCount();
        int index = childCount < builder.fieldIndex ? -1 : builder.fieldIndex;
        super.addView(child, index, layoutParams);
        if (builder.contentViewType == FieldView.TYPE_HIDDENVIEW) {
            child.setVisibility(GONE);
            return;
        }

        //XinYiLog.e(builder.fieldIndex + "index" + index);
        // super.addView(child, -1, layoutParams);
        // && builder.contentViewType != FieldView.TYPE_HIDDENVIEW

        //这里还有很多问题  顺序问题  分割线问题TYPE_HIDDENVIEW
        if (mDivided) {
            View line = new View(getContext());
            line.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.line));
            final LayoutParams lineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, SizeUtils.dp2px(0.5f));
            //lineLayoutParams.topMargin = SizeUtils.dp2px(1);
            //int index1 = childCount < builder.fieldIndex ? childCount : builder.fieldIndex;
            super.addView(line, index < 0 ? -1 : index + 1, lineLayoutParams);
            //super.addView(line, -1, lineLayoutParams);
        }
    }

    public void setFieldName(@NonNull List<String> names) {
        final List<FieldView> fieldViews = getFieldViews(ActionFieldType.FIELD_TYPE_ALL);
        if (names.size() != fieldViews.size()) {
            throw new RuntimeException("数量不一致");
        }

        for (int i = 0, j = names.size(); i < j; i++) {
            fieldViews.get(i).setFieldName(names.get(i));
        }
    }


}
