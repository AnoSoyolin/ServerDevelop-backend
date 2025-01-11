package com.seecoder.BlueWhale;

import com.seecoder.BlueWhale.enums.OrderStatusEnum;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.CouponGroupRepository;
import com.seecoder.BlueWhale.repository.CouponRepository;
import com.seecoder.BlueWhale.service.CouponService;
import com.seecoder.BlueWhale.service.OrderService;
import com.seecoder.BlueWhale.service.ProductService;
import com.seecoder.BlueWhale.service.UserService;
import com.seecoder.BlueWhale.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest
class BlueWhaleApplicationTests {

	@Autowired
	TokenUtil tokenUtil;
	@Autowired
	CouponService couponService;
	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	ProductService productService;
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	CouponGroupRepository couponGroupRepository;

	@Test
	void contextLoads() {
		User user=new User();
		user.setId(1);
		user.setPassword("123456");
		System.out.println(tokenUtil.getToken(user));
	}

	@Test
	@Transactional@Rollback(false)
	void testCouponUsage(){
		//测试优惠卷的领取和使用情况
		User user=new User();
		user.setId(1);
		CouponGroup couponGroup=new CouponGroup();
		couponGroup.setId(1);
		couponGroup.setAmount(10);
		couponGroup.setUserId(new ArrayList<>());
		Coupon coupon=new Coupon();
		coupon.setId(1);
		coupon.setGroupId(1);
		coupon.setUserId(1);
		coupon.setUsed(false);
		Product product=new Product();
		product.setPrice(100.0);
		product.setId(1);
		product.setStoreId(1);
		product.setStock(1);
        product.setSalesAmount(0);
		Order order=new Order();
		order.setId(1);
		order.setAmount(1);
		order.setProductId(1);
		order.setUserId(1);
		order.setStatus(OrderStatusEnum.UNPAID);
		userService.register(user.toVO());
		couponService.createCouponGroup(couponGroup.toVO());
		productService.create(product.toVO());
		orderService.create(order.toVO());
		System.out.println("使用优惠卷前");
		System.out.println("优惠卷是否被使用:"+coupon.isUsed()+
				"\n已领取用户:"+couponGroup.getUserId()+
				"\n剩余数量:"+couponGroup.getAmount());
		couponService.receiveCoupon(coupon.toVO());
		orderService.pay(1,1);
		System.out.println("使用优惠卷后");
		Coupon newCoupon = couponRepository.findById(1).orElse(null);
		CouponGroup newCouponGroup = couponGroupRepository.findById(1).orElse(null);
		System.out.println("优惠卷是否被使用:"+newCoupon.isUsed()+
				"\n已领取用户:"+newCouponGroup.getUserId()+
				"\n剩余数量:"+newCouponGroup.getAmount());
	}

	@Test
	@Transactional@Rollback(false)
	void testOrderPrice(){
		//测试对订单预算价格的计算
		CouponGroup couponGroup=new CouponGroup();
		couponGroup.setId(2);
		couponGroup.setAmount(10);
		couponGroup.setUserId(new ArrayList<>());
		couponService.createCouponGroup(couponGroup.toVO());
		Coupon coupon=new Coupon();
		coupon.setId(2);
		coupon.setGroupId(2);
		coupon.setUserId(1);
        coupon.setUsed(false);
		coupon.setType("FULL_REDUCTION");
		coupon.setFull(100);
		coupon.setReduction(10);
		couponService.receiveCoupon(coupon.toVO());
		Order order=new Order();
		order.setId(2);
		order.setAmount(3);
		order.setProductId(1);
		order.setUserId(1);
		order.setStatus(OrderStatusEnum.UNPAID);
		orderService.create(order.toVO());
		System.out.println("优惠卷类型"+coupon.getType()+"\n满减金额"+coupon.getFull()+"\n减免金额"+coupon.getReduction());
		System.out.println("原价:" + orderService.getPrice(2,0));
		System.out.println("使用优惠卷后:" + orderService.getPrice(2,2));
		coupon.setId(3);
		coupon.setGroupId(2);
		coupon.setUserId(1);
		coupon.setUsed(false);
		coupon.setType("SPECIAL");
		couponService.receiveCoupon(coupon.toVO());
		System.out.println("优惠卷类型"+coupon.getType());
		System.out.println("原价:" + orderService.getPrice(2,0));
		System.out.println("使用优惠卷后:" + orderService.getPrice(2,3));
	}

}
