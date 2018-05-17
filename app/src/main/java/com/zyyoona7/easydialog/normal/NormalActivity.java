package com.zyyoona7.easydialog.normal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.zyyoona7.easydialog.R;
import com.zyyoona7.dialog.impl.NormalDialog;

public class NormalActivity extends AppCompatActivity {

    private static final String[] SINGLE_ITEMS = new String[]{"一大大", "二大大", "三大大", "四大爷"};

    // TODO: 2018/5/11 加载弹窗、底部弹窗、单选、多选、确认弹窗
    private AppCompatRadioButton mTopRb;
    private AppCompatRadioButton mCenterRb;
    private AppCompatRadioButton mBottomRb;

    private AppCompatCheckBox mOtherFullCb;

    private AppCompatCheckBox mConfirmTitleCb;
    private AppCompatRadioButton mConfirmOneRb;
    private AppCompatRadioButton mConfirmTwoRb;
    private AppCompatRadioButton mConfirmThreeRb;
    private AppCompatButton mConfirmBtn;
    private NormalDialog.DialogListener mConfirmDialogListener;
    private static final String TAG_CONFIRM_DIALOG = "confirmDialog";

    private AppCompatButton mSingleBtn;
    private NormalDialog.DialogListener mSingleDialogListener;
    private static final String TAG_SINGLE_DIALOG = "singleDialog";

    public static void start(Context context) {
        Intent starter = new Intent(context, NormalActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        initViews();

        initEvents();

        if (savedInstanceState != null) {
            //修复屏幕旋转时各种回调等数据为null
            //http://www.yrom.net/blog/2014/11/02/dialogfragment-retaining-listener-after-screen-rotation/
            NormalDialog confirmDialog = (NormalDialog) getSupportFragmentManager().findFragmentByTag(TAG_CONFIRM_DIALOG);
            if (confirmDialog != null) {
                confirmDialog.setDialogListener(mConfirmDialogListener == null ? mConfirmDialogListener = createConfirmListener() : mConfirmDialogListener);
            }

            NormalDialog singleDialog = (NormalDialog) getSupportFragmentManager().findFragmentByTag(TAG_SINGLE_DIALOG);
            if (singleDialog != null) {
                singleDialog.setDialogListener(mSingleDialogListener == null ? mSingleDialogListener = createSingleListener() : mSingleDialogListener);
            }
        }
    }

    private void initViews() {
        mTopRb = findViewById(R.id.rb_base_top);
        mCenterRb = findViewById(R.id.rb_base_center);
        mBottomRb = findViewById(R.id.rb_base_bottom);

        mOtherFullCb = findViewById(R.id.cb_other_full_width);

        mConfirmTitleCb = findViewById(R.id.cb_confirm_title);
        mConfirmOneRb = findViewById(R.id.rb_confirm_one);
        mConfirmTwoRb = findViewById(R.id.rb_confirm_two);
        mConfirmThreeRb = findViewById(R.id.rb_confirm_three);
        mConfirmBtn = findViewById(R.id.btn_confirm);

        mSingleBtn = findViewById(R.id.btn_single);
    }

    private void initEvents() {
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirm();
            }
        });

        mSingleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingle();
            }
        });
    }

    private int getBaseGravity() {
        if (mTopRb.isChecked()) {
            return Gravity.TOP;
        } else if (mCenterRb.isChecked()) {
            return Gravity.CENTER;
        } else {
            return Gravity.BOTTOM;
        }
    }

    private void showConfirm() {
        mConfirmDialogListener = createConfirmListener();
        NormalDialog.newInstance()
                .setFragmentTag(TAG_CONFIRM_DIALOG)
                .setGravity(getBaseGravity())
                .setWidth(mOtherFullCb.isChecked() ? ViewGroup.LayoutParams.MATCH_PARENT : 0)
                .setDialogListener(mConfirmDialogListener)
                .show(getSupportFragmentManager());
    }

    private NormalDialog.DialogListener createConfirmListener() {
        return new NormalDialog.DialogListener() {
            @Override
            public void initDialog(AlertDialog.Builder builder) {
                if (mConfirmTitleCb.isChecked()) {
                    builder.setTitle("确认退出");
                }
                builder.setMessage("确定退出吗？");
                if (mConfirmThreeRb.isChecked()) {
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ToastUtils.showShort("确定");
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ToastUtils.showShort("取消");
                        }
                    });

                    builder.setNeutralButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ToastUtils.showShort("知道了");
                        }
                    });
                }

                if (mConfirmTwoRb.isChecked()) {
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ToastUtils.showShort("确定");

                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ToastUtils.showShort("取消");
                        }
                    });
                }

                if (mConfirmOneRb.isChecked()) {
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ToastUtils.showShort("确定");
                        }
                    });
                }
            }
        };
    }

    private void showSingle() {
        mSingleDialogListener = createSingleListener();
        NormalDialog.newInstance()
                .setFragmentTag(TAG_SINGLE_DIALOG)
                .setGravity(getBaseGravity())
                .setWidth(mOtherFullCb.isChecked() ? ViewGroup.LayoutParams.MATCH_PARENT : 0)
                .setDialogListener(mSingleDialogListener)
                .show(getSupportFragmentManager());
    }

    private NormalDialog.DialogListener createSingleListener() {
        return new NormalDialog.DialogListener() {
            @Override
            public void initDialog(AlertDialog.Builder builder) {
                builder.setItems(SINGLE_ITEMS,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ToastUtils.showShort(SINGLE_ITEMS[which]);
                            }
                        });
            }
        };
    }

}
