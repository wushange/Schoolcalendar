package com.wsg.schoolcalendar;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wsg.schoolcalendar.bean.Scheme;

import java.util.List;

public class SchemeAdapter extends BaseQuickAdapter<Scheme, BaseViewHolder> {
    public SchemeAdapter(@Nullable List<Scheme> data) {
        super(R.layout.recycler_view_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Scheme item) {
        helper.setText(R.id.textView1, "类型：" + getType(item.getSchemetype()));
        helper.setText(R.id.textView2, "详情：" + item.getScheme());
        helper.setText(R.id.textView3, "时间：" + item.getSchemetime());
        helper.addOnClickListener(R.id.btn_delete);

    }

    public String getType(int type) {
        String string = "";
        switch (type) {
            case 0:
                string = "课程";
                break;
            case 1:
                string = "事件";
                break;
            case 2:
                string = "会议";
                break;
            default:
        }
        return string;
    }
}
