package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.po.Coupon;
import com.seecoder.BlueWhale.po.CouponGroup;
import com.seecoder.BlueWhale.service.CouponService;
import com.seecoder.BlueWhale.vo.CouponGroupVO;
import com.seecoder.BlueWhale.vo.CouponVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    @Autowired
    CouponService couponService;

    @PostMapping("/createGroup")//创建优惠卷组
    public ResultVO<CouponGroup> create(@RequestBody CouponGroupVO couponGroupVO){
        return ResultVO.buildSuccess(couponService.createCouponGroup(couponGroupVO).toPO());
    }

    @GetMapping
    //获得所有优惠卷组
    public ResultVO<List<CouponGroupVO>> getAllGroups(){
        return ResultVO.buildSuccess(couponService.getAllCouponGroups());
    }

    @GetMapping("/{groupId}")//通过优惠卷组id获取对应优惠卷组
    public ResultVO<CouponGroupVO> getGroupByGroupId(@PathVariable(value = "groupId") Integer id) {
        CouponGroupVO couponGroupVO = couponService.getGroupByGroupId(id);
        return ResultVO.buildSuccess(couponGroupVO);
    }

    @GetMapping("/user")//通过用户id获取该用户得所有优惠卷
    public ResultVO<List<CouponVO>> getAvailableCoupons(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "orderId") Integer orderId){
        return ResultVO.buildSuccess(couponService.getAvailableCoupons(userId,orderId));
    }

    @PostMapping("/receive")//领取优惠卷
    public ResultVO<Coupon> receiveCoupon(@RequestBody CouponVO couponVO){
        return ResultVO.buildSuccess(couponService.receiveCoupon(couponVO).toPO());
    }

    @GetMapping("/getUserName")//通过用户id获取用户名
    public ResultVO<List<String>> getNameByUserId(@RequestParam(value = "userId") String userId){
        return ResultVO.buildSuccess(couponService.getNameByUserId(userId));
    }
}
