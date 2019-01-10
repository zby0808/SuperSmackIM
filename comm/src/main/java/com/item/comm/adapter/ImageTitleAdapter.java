package com.item.comm.adapter;


import android.graphics.Color;
import android.view.Gravity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.item.comm.R;
import com.item.comm.model.ImageTitleModel;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;



public class ImageTitleAdapter<T extends ImageTitleModel> extends BaseQuickAdapter<T, BaseViewHolder> {

    public void addAllDataOnlyVisible(List<T> list) {
        getData().clear();
        ArrayList<T> objects = new ArrayList<>();
        for (T t : list) {
            if (t.isVisible()) objects.add(t);
        }
        addData(objects);
    }

    public ImageTitleAdapter() {
        super(R.layout.comm_item_image_title);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageTitleModel item) {
        helper.setText(R.id.tv_icon_name, item.getTitle());
        helper.setImageResource(R.id.img_icon, item.getDrawableId());
        QBadgeView qBadgeView = new QBadgeView(mContext);
        Badge badge = qBadgeView.bindTarget(helper.getView(R.id.rootView));
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(12, true);
        badge.setBadgePadding(4, true);
        badge.setBadgeBackgroundColor(Color.RED);
        badge.setBadgeNumber(item.getBudgeCount());
    }
}
