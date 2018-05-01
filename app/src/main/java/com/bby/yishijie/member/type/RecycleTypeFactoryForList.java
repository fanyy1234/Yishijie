package com.bby.yishijie.member.type;

import android.view.View;

import com.bby.yishijie.R;
import com.bby.yishijie.member.holder.BaseRecyleViewHolder;
import com.bby.yishijie.member.holder.IntegralMallHeaderViewHolder;
import com.bby.yishijie.member.holder.IntegralMallItemViewHolder;
import com.bby.yishijie.member.holder.WithdrawRecordViewHolder;
import com.bby.yishijie.member.model.IntegralMall;
import com.bby.yishijie.member.model.IntegralMallItem;
import com.bby.yishijie.member.model.WithdrawRecord;


/**
 * Created by yq05481 on 2016/12/30.
 */

public class RecycleTypeFactoryForList implements RecycleTypeFactory {
    private final int HEADER_JIFENSHANGCHENG = R.layout.header_integral_mall;
    private final int ITEM_JIFENSHANGCHENG = R.layout.item_integral_mall;
    private final int ITEM_WITHDRAW = R.layout.item_withdraw_record;

    @Override
    public int type(IntegralMall integralMall) {
        return HEADER_JIFENSHANGCHENG;
    }
    @Override
    public int type(IntegralMallItem integralMallItem) {
        return ITEM_JIFENSHANGCHENG;
    }
    @Override
    public int type(WithdrawRecord withdrawRecord) {
        return ITEM_WITHDRAW;
    }


    @Override
    public BaseRecyleViewHolder createViewHolder(int type, View itemView) {

        if(HEADER_JIFENSHANGCHENG == type){
            return new IntegralMallHeaderViewHolder(itemView);
        }
        else if (ITEM_JIFENSHANGCHENG == type){
            return new IntegralMallItemViewHolder(itemView);
        }
        else if (ITEM_WITHDRAW == type){
            return new WithdrawRecordViewHolder(itemView);
        }

        return null;
    }
}
