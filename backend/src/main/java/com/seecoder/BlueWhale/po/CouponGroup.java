package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.vo.CouponGroupVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CouponGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "amount")
    private Integer amount;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @ElementCollection
    @Basic
    @Column(name = "user_id")
    private List<Integer> userId;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "full")
    private Integer full;

    @Basic
    @Column(name = "reduction")
    private Integer reduction;

    public CouponGroupVO toVO(){
        CouponGroupVO couponGroupVO = new CouponGroupVO();
        couponGroupVO.setId(id);
        couponGroupVO.setAmount(amount);
        couponGroupVO.setStoreId(storeId);
        couponGroupVO.setUserId(userId);
        couponGroupVO.setType(type);
        couponGroupVO.setFull(full);
        couponGroupVO.setReduction(reduction);
        return couponGroupVO;
    }

}
