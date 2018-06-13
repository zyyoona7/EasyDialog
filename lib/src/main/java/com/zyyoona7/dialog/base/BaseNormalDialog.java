package com.zyyoona7.dialog.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * 普通 Dialog 用法的 DialogFragment
 *
 * @param <T>
 */
public abstract class BaseNormalDialog<T extends BaseNormalDialog> extends BaseDialog<T> {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            initDialog(builder);
            return builder.create();
        } else {
            return super.onCreateDialog(savedInstanceState);
        }
    }

    /**
     * 初始化 Dialog 数据
     *
     * @param builder
     */
    protected abstract void initDialog(AlertDialog.Builder builder);

}
