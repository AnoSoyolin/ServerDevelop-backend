package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.po.Coupon;
import com.seecoder.BlueWhale.po.CouponGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    List<Coupon> findCouponByUserId(Integer userId);

    CouponGroup findByGroupId(Integer couponGroupId);
}
