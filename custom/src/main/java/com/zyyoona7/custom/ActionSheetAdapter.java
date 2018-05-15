package com.zyyoona7.custom;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ActionSheetAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<Integer> mHighlightList;

    public ActionSheetAdapter() {
        super(R.layout.item_action_sheet, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        AppCompatTextView itemTv = helper.getView(R.id.tv_action_sheet_item);
        itemTv.setText(item);
        if (mHighlightList != null) {
            if (mHighlightList.contains(helper.getAdapterPosition())) {
                itemTv.setTextColor(ContextCompat.getColor(itemTv.getContext(), R.color.color_ios_text_red));
            } else {
                itemTv.setTextColor(ContextCompat.getColor(itemTv.getContext(), R.color.color_ios_text_blue));
            }
        }
    }

    public void setHighlightList(List<Integer> highlightList) {
        this.mHighlightList = highlightList;
    }
}
