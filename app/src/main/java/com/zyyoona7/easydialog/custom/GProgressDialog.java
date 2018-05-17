package com.zyyoona7.easydialog.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.zyyoona7.easydialog.R;
import com.zyyoona7.dialog.base.BaseEasyDialog;

public class GProgressDialog extends BaseEasyDialog<GProgressDialog> {

    ImmersionBar mImmersionBar;

    public static GProgressDialog newInstance() {
        return new GProgressDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutRes(R.layout.layout_google_progress)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void initViews(View view) {
        initImmersionBar();
        FrameLayout containerFl = view.findViewById(R.id.fl_google_pd_container);

        containerFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsCancelOnTouchOutside) {
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this, getDialog());
        mImmersionBar.init();
    }
}
