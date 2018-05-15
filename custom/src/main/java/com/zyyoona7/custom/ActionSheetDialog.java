package com.zyyoona7.custom;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zyyoona7.lib.base.BaseEasyDialog;

import java.util.Arrays;
import java.util.List;

public class ActionSheetDialog extends BaseEasyDialog<ActionSheetDialog> {

    private AppCompatTextView mTitleTv;
    private AppCompatTextView mMsgTv;
    private View mTitleDividerV;
    private ActionSheetAdapter mItemAdapter;
    private AppCompatTextView mCancelTv;

    private String mTitleStr;
    private String mMsgStr;
    private List<String> mItemList;
    private List<Integer> mHighlightList;
    private boolean isCancelHighlight;

    private DialogInterface.OnClickListener mOnClickListener;

    public static ActionSheetDialog newInstance() {
        return new ActionSheetDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGravity(Gravity.BOTTOM)
                .setLayoutRes(R.layout.dialog_action_sheet);
        if (mAnimationStyle <= 0) {
            setAnimationStyle(R.style.SlideY);
        }
    }

    @Override
    protected void initViews(View view) {
        mTitleTv = view.findViewById(R.id.tv_action_sheet_title);
        mMsgTv = view.findViewById(R.id.tv_action_sheet_msg);
        mTitleDividerV = view.findViewById(R.id.v_action_sheet_title_divider);
        RecyclerView itemRv = view.findViewById(R.id.rv_action_sheet_item);
        mCancelTv = view.findViewById(R.id.tv_action_sheet_cancel);

        itemRv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL);
        Drawable dividerDrawable = ContextCompat.getDrawable(mActivity, R.drawable.shape_divider);
        if (dividerDrawable != null) {
            itemDecoration.setDrawable(dividerDrawable);
        }
        itemRv.addItemDecoration(itemDecoration);
        mItemAdapter = new ActionSheetAdapter();
        itemRv.setAdapter(mItemAdapter);

        initData();

        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(null, position);
                }
            }
        });
    }

    private void initData() {
        setTitle();
        setMessage();
        checkShowDivider();
        setItems();
        setHighlightItems();
    }

    public ActionSheetDialog setTitle(@StringRes int titleRes) {
        return setTitle(getString(titleRes));
    }

    public ActionSheetDialog setTitle(String title) {
        this.mTitleStr = title;
        return this;
    }

    private void setTitle() {
        if (TextUtils.isEmpty(mTitleStr)) {
            mTitleTv.setVisibility(View.GONE);
            mTitleTv.setText("");
        } else {
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(mTitleStr);
        }
    }

    public ActionSheetDialog setMessage(@StringRes int msgRes) {
        return setMessage(getString(msgRes));
    }

    public ActionSheetDialog setMessage(String msg) {
        this.mMsgStr = msg;
        return this;
    }

    private void setMessage() {
        if (TextUtils.isEmpty(mMsgStr)) {
            mMsgTv.setVisibility(View.GONE);
            mMsgTv.setText("");
        } else {
            mMsgTv.setVisibility(View.VISIBLE);
            mMsgTv.setText(mMsgStr);
        }
    }

    private void checkShowDivider() {
        if (TextUtils.isEmpty(mTitleTv.getText()) && TextUtils.isEmpty(mMsgTv.getText())) {
            mTitleDividerV.setVisibility(View.GONE);
        } else {
            mTitleDividerV.setVisibility(View.VISIBLE);
        }
    }

    public ActionSheetDialog setItems(String... items) {
        if (items != null) {
            this.mItemList = Arrays.asList(items);
        }
        return this;
    }

    private void setItems() {
        mItemAdapter.setNewData(mItemList);
    }

    public ActionSheetDialog setHighlightItems(Integer... position) {
        if (position != null) {
            mHighlightList = Arrays.asList(position);
        }
        return this;
    }

    public ActionSheetDialog setHighlightItems(boolean isCancelHighlight, Integer... position) {
        this.isCancelHighlight = isCancelHighlight;
        if (position != null) {
            mHighlightList = Arrays.asList(position);
        }
        return this;
    }

    private void setHighlightItems() {
        if (mItemAdapter != null) {
            mItemAdapter.setHighlightList(mHighlightList);
            mItemAdapter.notifyDataSetChanged();
        }

        if (isCancelHighlight) {
            if (mCancelTv != null) {
                mCancelTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_ios_text_red));
            }
        }
    }

    public ActionSheetDialog setItemClickListener(DialogInterface.OnClickListener listener){
        this.mOnClickListener=listener;
        return this;
    }
}
