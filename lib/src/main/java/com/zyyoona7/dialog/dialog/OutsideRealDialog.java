package com.zyyoona7.dialog.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 可以拦截touch outside 事件的Dialog
 *
 * @author zyyoona7
 * @version v1.0.0
 * @since 2018/10/17.
 */
public class OutsideRealDialog extends AppCompatDialog {

    private boolean mCancelOnTouchOutside = false;
    private OnTouchOutsideListener mOnTouchOutsideListener;

    public OutsideRealDialog(Context context) {
        super(context);
    }

    public OutsideRealDialog(Context context, int theme) {
        super(context, theme);
    }

    protected OutsideRealDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        this.mCancelOnTouchOutside = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mCancelOnTouchOutside && event.getAction() == MotionEvent.ACTION_DOWN
                && isOutOfBounds(getContext(), event) && getWindow() != null &&
                getWindow().peekDecorView() != null && mOnTouchOutsideListener != null) {
            //touch outside
            if (mOnTouchOutsideListener.onTouchOutside(this, event)) {
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        if (getWindow() == null) {
            return false;
        }
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    public void setOnTouchOutsideListener(OnTouchOutsideListener listener) {
        this.mOnTouchOutsideListener = listener;
    }

    public interface OnTouchOutsideListener {

        boolean onTouchOutside(OutsideRealDialog dialog, MotionEvent event);
    }
}
