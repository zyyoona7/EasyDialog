package com.zyyoona7.dialog.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;

/**
 * 可以改变 dim 颜色并支持 dim 淡入淡出动画的的Dialog
 *
 * @author zyyoona7
 * @version v1.0.0
 * @since 2018/10/10.
 */
public abstract class BaseDimDialog<T extends BaseDimDialog> extends BaseEasyDialog<T> {

    private static final int DEFAULT_DIM_DURATION = 200;

    @ColorInt
    private int mDimColor = -1;
    @ColorRes
    private int mDimColorRes = 0;
    private float mDimColorAmount = 0.9f;
    private Drawable mDimDrawable;
    private ObjectAnimator mDimObjectAnimator;
    private int mDimAnimDuration = DEFAULT_DIM_DURATION;

    public BaseDimDialog() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        interceptBackEvent();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 拦截返回按钮事件
     */
    private void interceptBackEvent() {
        if (isInterceptBackEvent() && getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        //自己业务处理，用true拦截
                        boolean consume = isInterceptBackEvent();
                        if (consume) {
                            onBackPress();
                            if (mDimObjectAnimator != null) {
                                mDimObjectAnimator.reverse();
                            }
                        }
                        return consume;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void initWindowLayoutParams(Window window, WindowManager.LayoutParams layoutParams) {
        super.initWindowLayoutParams(window, layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            int dimColor = getDimColor();
            if (dimColor == -1) {
                return;
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView().getRootView();
            clearDim(rootView);
            applyDim(rootView, dimColor, mDimColorAmount);
        }
    }

    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //清除资源
            if (mDimObjectAnimator != null && mDimObjectAnimator.isRunning()) {
                mDimObjectAnimator.cancel();
            }
            ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView().getRootView();
            clearDim(rootView);
            mDimObjectAnimator = null;
            mDimDrawable = null;
        }
        super.onDestroy();
    }

    /**
     * 设置 dim
     *
     * @param parent    ViewGroup
     * @param color     dim color
     * @param dimAmount dim 透明度
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void applyDim(@NonNull ViewGroup parent, int color, float dimAmount) {
        mDimDrawable = new ColorDrawable(color);
        mDimDrawable.setBounds(0, 0, parent.getWidth(), parent.getHeight());

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(mDimDrawable);
        initDimAnimator(dimAmount);
        if (mDimObjectAnimator.isRunning()) {
            mDimObjectAnimator.cancel();
        }
        mDimObjectAnimator.start();
    }

    /**
     * 初始化dim动画
     *
     * @param dimAmount dim 透明度
     */
    private void initDimAnimator(float dimAmount) {
        if (mDimObjectAnimator == null) {
            mDimObjectAnimator = ObjectAnimator.ofInt(mDimDrawable, "alpha",
                    0, (int) (255 * dimAmount))
                    .setDuration(mDimAnimDuration);
            mDimObjectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    if (isReverse) {
                        mDimObjectAnimator.removeAllListeners();
                        dismiss();
                    }
                }
            });
        }
    }

    /**
     * 清除dim
     *
     * @param parent viewGroup
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    /**
     * 获取设置的 dim color
     *
     * @return dim color 未设置 返回-1
     */
    private int getDimColor() {
        return mDimColor != -1 ? mDimColor : mDimColorRes != 0 ? ContextCompat.getColor(mActivity, mDimColorRes) : -1;
    }

    /**
     * 设置 dim color
     *
     * @param dimColor dim color
     * @return this
     */
    public T setDimColor(@ColorInt int dimColor) {
        this.mDimColor = dimColor;
        return self();
    }

    /**
     * 设置dim color 资源
     *
     * @param dimColorRes dimColor 资源
     * @return this
     */
    public T setDimColorRes(@ColorRes int dimColorRes) {
        this.mDimColorRes = dimColorRes;
        return self();
    }

    /**
     * 设置暗淡效果透明度
     *
     * @param dimAmount 暗淡的透明度
     * @return this
     */
    @Override
    public T setDimAmount(@FloatRange(from = 0.0f, to = 1.0f) float dimAmount) {
        this.mDimColorAmount = dimAmount;
        return super.setDimAmount(dimAmount);
    }

    /**
     * 设置淡入淡出动画持续时间
     *
     * @param dimAnimDuration dim动画持续时间
     * @return this
     */
    public T setDimAnimDuration(int dimAnimDuration) {
        this.mDimAnimDuration = dimAnimDuration;
        return self();
    }


    /**
     * 是否拦截返回按钮事件
     *
     * @return 是否拦截返回按钮事件
     */
    protected boolean isInterceptBackEvent() {
        return true;
    }

    /**
     * 返回键事件回调
     */
    protected void onBackPress() {

    }
}