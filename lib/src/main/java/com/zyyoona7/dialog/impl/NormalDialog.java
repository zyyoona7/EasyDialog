package com.zyyoona7.dialog.impl;

import android.support.v7.app.AlertDialog;

import com.zyyoona7.dialog.base.BaseNormalDialog;

public class NormalDialog extends BaseNormalDialog<NormalDialog> {

    private DialogListener mDialogListener;

    public static NormalDialog newInstance() {
        return new NormalDialog();
    }

    @Override
    protected void initDialog(AlertDialog.Builder builder) {
        if (mDialogListener != null) {
            mDialogListener.initDialog(builder);
        }
    }

    /**
     * 设置 Dialog 监听器
     *
     * @param listener
     * @return
     */
    public NormalDialog setDialogListener(DialogListener listener) {
        this.mDialogListener = listener;
        return this;
    }

    public interface DialogListener {

        void initDialog(AlertDialog.Builder builder);

    }
}
