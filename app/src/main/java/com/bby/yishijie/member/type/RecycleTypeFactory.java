package com.bby.yishijie.member.type;

import android.view.View;

import com.bby.yishijie.member.holder.BaseRecyleViewHolder;
import com.bby.yishijie.member.model.IntegralMall;
import com.bby.yishijie.member.model.IntegralMallItem;
import com.bby.yishijie.member.model.WithdrawRecord;


/**
 * Created by yq05481 on 2016/12/30.
 */

public interface RecycleTypeFactory {
    int type(IntegralMall integralMall);
    int type(IntegralMallItem integralMallItem);
    int type(WithdrawRecord withdrawRecord);

    BaseRecyleViewHolder createViewHolder(int type, View itemView);
}
