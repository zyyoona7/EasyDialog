package com.zyyoona7.lib.impl;

import android.view.View;

import com.zyyoona7.lib.base.BaseEasyDialog;

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

    public interface ViewListener {

        void initViews(View view);
    }
}
