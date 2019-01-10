package com.item.comm.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Fracesuit on 2017/7/21.
 */

public class ImageTitleModel implements Comparable<ImageTitleModel>, Serializable {
    private int drawableId;
    private String title;
    private int budgeCount;//提醒的数量
    private int orderIndex;//排序
    private Class clazz;
    private boolean isVisible = true;

    public ImageTitleModel(int drawableId, String title) {
        this(drawableId, title, 0, 0);
    }

    public ImageTitleModel(int drawableId, String title, int budgeCount) {
        this(drawableId, title, 0, budgeCount);
    }

    public ImageTitleModel(int drawableId, String title, int orderIndex, int budgeCount) {
        this.drawableId = drawableId;
        this.title = title;
        this.budgeCount = budgeCount;
        this.orderIndex = orderIndex;
    }

    @Override
    public int compareTo(@NonNull ImageTitleModel o) {
        return this.orderIndex - o.orderIndex;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBudgeCount() {
        return budgeCount;
    }

    public void setBudgeCount(int budgeCount) {
        this.budgeCount = budgeCount;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
