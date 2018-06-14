package com.zyyoona7.dialog.impl;

import android.view.View;

import com.zyyoona7.dialog.base.BaseEasyDialog;

public class EasyDialog extends BaseEasyDialog<EasyDialog> {

    private ViewListener mViewListener;

    public static EasyDialog newInstance() {
        return new EasyDialog();
    }

    @Override
    protected void initViews(View view) {
        if (mViewListener != null) {
            mViewListener.initViews(view);
        }
    }

    /**
     * 设置初始化 View 监听器
     *
     * @param listener
     * @return
     */
    public EasyDialog setViewListener(ViewListener listener) {
        this.mViewListener = listener;
        return this;
    }

    /**
     * 清除匿名内部类 callback 对外部类的引用，避免可能导致的内存泄漏
     */
    @Override
    public void clearRefOnDestroy() {
        super.clearRefOnDestroy();
        //清除 mViewListener 引用
        setViewListener(null);
    }

    public interface ViewListener {

        void initViews(View view);
    }
}
