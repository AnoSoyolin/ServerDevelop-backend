package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.vo.CouponGroupVO;
import com.seecoder.BlueWhale.vo.CouponVO;

import java.util.List;

public interface CouponService {
    CouponGroupVO createCouponGroup(CouponGroupVO couponGroupVO);
    List<CouponGroupVO> getAllCouponGroups();
    CouponGroupVO getGroupByGroupId(Integer id);
    List<CouponVO> getAvailableCoupons(Integer userId, Integer orderId);
    CouponVO receiveCoupon(CouponVO couponVO);
    List<String> getNameByUserId(String userId);
}
