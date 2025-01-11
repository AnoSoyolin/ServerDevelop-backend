package com.seecoder.BlueWhale.vo;


import com.seecoder.BlueWhale.po.CouponGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CouponGroupVO {
    Integer id;

    Integer amount;

    Integer storeId;

    List<Integer> userId;

    String type;

    Integer full;

    Integer reduction;

    public CouponGroup toPO(){
        CouponGroup couponGroup=new CouponGroup();
        couponGroup.setId(this.id);
        couponGroup.setAmount(this.amount);
        couponGroup.setStoreId(this.storeId);
        couponGroup.setUserId(this.userId);
        couponGroup.setType(this.type);
        couponGroup.setFull(this.full);
        couponGroup.setReduction(this.reduction);
        return couponGroup;
    }
}