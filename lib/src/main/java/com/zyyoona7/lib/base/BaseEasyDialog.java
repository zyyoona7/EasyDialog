package com.zyyoona7.lib.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * 和 Fragment 用法类似的 DialogFragment
 *
 * @param <T>
 */
public abstract class BaseEasyDialog<T extends BaseEasyDialog> extends BaseDialog<T> {

    @LayoutRes
    private int mLayoutRes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(mLayoutRes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    /**
     * 初始化 Views
     *
     * @param view
     */
    protected abstract void initViews(View view);

    /**
     * 设置布局id
     *
     * @param layoutRes
     * @return
     */
    public T setLayoutRes(@LayoutRes int layoutRes) {
        this.mLayoutRes = layoutRes;
        return self();
    }

}
