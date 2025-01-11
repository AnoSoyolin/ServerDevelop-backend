package com.seecoder.BlueWhale.po;


import com.seecoder.BlueWhale.vo.CouponVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Coupon {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "group_id")
    private Integer groupId;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "full")
    private Integer full;//满减里面满的那个数额

    @Basic
    @Column(name = "reduction")
    private Integer reduction;//满减里面减的那个数额

    @Basic
    @Column(name = "is_used")
    private boolean isUsed;

    public CouponVO toVO(){
        CouponVO couponVO = new CouponVO();
        couponVO.setId(id);
        couponVO.setStoreId(storeId);
        couponVO.setUserId(userId);
        couponVO.setGroupId(groupId);
        couponVO.setType(type);
        couponVO.setFull(full);
        couponVO.setReduction(reduction);
        couponVO.setUsed(isUsed);
        return couponVO;
    }
}
