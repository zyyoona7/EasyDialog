package com.zyyoona7.dialog.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;

import com.zyyoona7.dialog.dialog.OutsideRealDialog;

/**
 * 可以改变 dim 颜色并支持 dim 淡入淡出动画的的Dialog
 *
 * @author zyyoona7
 * @version v1.0.0
 * @since 2018/10/10.
 */
public abstract class BaseDimDialog<T extends BaseDimDialog> extends BaseEasyDialog<T> {

    private static final int DEFAULT_DIM_DURATION = 200;
    protected static final String KEY_DIM_COLOR_AMOUNT = "keyDimColorAmount";
    protected static final String KEY_DIM_COLOR_RES = "keyDimColorRes";
    protected static final String KEY_DIM_COLOR = "keyDimColor";

    @ColorInt
    private int mDimColor = -1;
    @ColorRes
    private int mDimColorRes = 0;
    private float mDimColorAmount = 0.9f;
    private GradientDrawable mDimDrawable;
    private ObjectAnimator mDimObjectAnimator;
    private int mDimAnimDuration = DEFAULT_DIM_DURATION;

    private boolean mIsAnimatorReverse = false;

    private boolean mIsFirstIn = true;

    public BaseDimDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDimColorRes = savedInstanceState.getInt(KEY_DIM_COLOR_RES, 0);
            mDimColor = savedInstanceState.getInt(KEY_DIM_COLOR, -1);
            mDimColorAmount = savedInstanceState.getFloat(KEY_DIM_COLOR_AMOUNT, 0.9f);
        }
        this.mIsFirstIn = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_DIM_COLOR_RES, mDimColorRes);
        outState.putInt(KEY_DIM_COLOR, mDimColor);
        outState.putFloat(KEY_DIM_COLOR_AMOUNT, mDimColorAmount);
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new OutsideRealDialog(getContext(), getTheme());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        interceptTouchOutside();
        interceptBackEvent();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 拦截touch outside 事件
     */
    private void interceptTouchOutside() {
        if (mIsCancelOnTouchOutside && getDialog() != null && getDialog() instanceof OutsideRealDialog) {
            ((OutsideRealDialog) getDialog()).setOnTouchOutsideListener(
                    new OutsideRealDialog.OnTouchOutsideListener() {
                        @Override
                        public boolean onTouchOutside(OutsideRealDialog dialog, MotionEvent event) {
                            dismissWithAnim();
                            BaseDimDialog.this.onTouchOutside();
                            ((OutsideRealDialog) getDialog()).setOnTouchOutsideListener(null);
                            return true;
                        }
                    });
        }
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
                            dismissWithAnimBackPress();
                            //如果消费事件，dismiss后移除监听器
                            getDialog().setOnKeyListener(null);
                        }
                        return consume;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 带 dim 动画的 dismiss 方法 并执行 onBackPress方法
     */
    public void dismissWithAnimBackPress() {
        dismissWithAnim(true);
    }

    /**
     * 带 dim 动画的 dismiss 方法
     */
    public void dismissWithAnim() {
        dismissWithAnim(false);
    }

    /**
     * 带 dim 动画的 dismiss 方法
     *
     * @param withBackPress 是否执行onBackPress方法
     */
    protected void dismissWithAnim(boolean withBackPress) {
        if (withBackPress) {
            onBackPress();
        }
        if (mDimObjectAnimator != null) {
            mIsAnimatorReverse = true;
            mDimObjectAnimator.reverse();
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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mIsCancelOnTouchOutside && getDialog() != null && getDialog() instanceof OutsideRealDialog) {
            ((OutsideRealDialog) getDialog()).setOnTouchOutsideListener(null);
        }
        if (getDialog() != null) {
            getDialog().setOnKeyListener(null);
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
        mDimDrawable = new GradientDrawable();
        if (!onDrawableInit(mDimDrawable)) {
            mDimDrawable.setColor(color);
        }
        mDimDrawable.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(mDimDrawable);
        initDimAnimator(dimAmount);
        if (mDimObjectAnimator.isRunning()) {
            mDimObjectAnimator.cancel();
        }
        mIsAnimatorReverse = false;
        if (mIsFirstIn) {
            mDimObjectAnimator.start();
            mIsFirstIn = false;
        } else {
            mDimDrawable.setAlpha((int) (mDimColorAmount * 255));
        }
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
                public void onAnimationEnd(Animator animation) {
                    if (mIsAnimatorReverse) {
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
     * 获取淡入淡出动画持续时间
     *
     * @return 淡入淡出动画持续时间
     */
    public int getDimAnimDuration() {
        return mDimAnimDuration;
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

    /**
     * 触摸outside回调
     */
    protected void onTouchOutside() {

    }

    /**
     * drawable init
     *
     * @param dimDrawable GradientDrawable
     * @return 是否自行设置Drawable
     */
    protected boolean onDrawableInit(GradientDrawable dimDrawable) {
        return false;
    }
}
