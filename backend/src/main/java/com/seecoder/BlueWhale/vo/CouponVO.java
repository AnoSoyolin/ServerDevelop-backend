package com.seecoder.BlueWhale.vo;


import com.seecoder.BlueWhale.po.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponVO {
    private Integer id;

    private Integer storeId;

    private Integer userId;

    private Integer groupId;

    private String type;

    private Integer full;

    private Integer reduction;

    private boolean isUsed;

    public Coupon toPO(){
        Coupon coupon=new Coupon();
        coupon.setId(this.id);
        coupon.setStoreId(this.storeId);
        coupon.setUserId(this.userId);
        coupon.setGroupId(this.groupId);
        coupon.setType(this.type);
        coupon.setFull(this.full);
        coupon.setReduction(this.reduction);
        coupon.setUsed(this.isUsed);
        return coupon;
    }
}