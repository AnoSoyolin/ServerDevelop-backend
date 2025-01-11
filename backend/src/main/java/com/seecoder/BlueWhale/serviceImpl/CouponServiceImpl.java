package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.service.CouponService;
import com.seecoder.BlueWhale.vo.CouponGroupVO;
import com.seecoder.BlueWhale.vo.CouponVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {
    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);
    @Autowired
    CouponGroupRepository couponGroupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    @Override
    public CouponGroupVO createCouponGroup(CouponGroupVO couponGroupVO) {
        CouponGroup couponGroup = couponGroupVO.toPO();
        couponGroupRepository.save(couponGroup);
        return couponGroup.toVO();
    }

    @Override
    public List<CouponGroupVO> getAllCouponGroups() {//获得所有优惠卷组
        return couponGroupRepository.findAll().stream().map(CouponGroup::toVO).collect(Collectors.toList());
    }

    @Override
    public CouponGroupVO getGroupByGroupId(Integer id) {
        CouponGroup couponGroup = couponGroupRepository.findById(id).orElse(null);
        if (couponGroup == null) {
            throw BlueWhaleException.couponGroupNotExists();
        }
        return couponGroup.toVO();
    }

    @Override
    public List<CouponVO> getAvailableCoupons(Integer userId, Integer orderId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw BlueWhaleException.userNotExists();
        }
        logger.info("user: " + user);
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw BlueWhaleException.orderNotExists();
        }
        logger.info("order: " + order);
        Integer productId = order.getProductId();
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw BlueWhaleException.productNotExists();
        }
        Integer storeId = product.getStoreId();
        List<Coupon> old_list = couponRepository.findCouponByUserId(userId);
        if (old_list == null) {
            throw BlueWhaleException.couponNotExists();
        }
        List<Coupon> new_list = new ArrayList<Coupon>();
        int count = old_list.size();
        //根据coupon的reduction大小对优惠卷进行排序
        for (int i = 0; i < count; i++) {
            int index = 0;
            for (int j = 0; j < old_list.size(); j++) {
                if (old_list.get(j).getReduction() > old_list.get(index).getReduction()) {
                    index = j;
                }
            }
            Coupon coupon = old_list.get(index);
            if ((Objects.equals(storeId, coupon.getStoreId()) || Objects.equals(coupon.getStoreId(),0) )&& order.getAmount() * product.getPrice() >= coupon.getFull()) {//如果优惠卷的storeId和商品的storeId相同，且订单金额大于等于满减金额
                new_list.add(old_list.get(index));
            }
            old_list.remove(index);
        }

        List<Coupon> new_new_list = new ArrayList<Coupon>();
        int new_count = new_list.size();
        //在coupon的reduction大小相同时，根据coupon的full大小对优惠卷进行排序
        int start = 0;
        int end = 0;
        while (new_count != 0){
            end = 0;
            while (Objects.equals(new_list.get(end).getReduction(), new_list.get(start).getReduction())) {
                end++;
                if(end == new_count){
                    break;
                }
            }
            for (int i = start; i < end; i++) {
                int index = start;
                for (int j = start; j < end - i; j++) {
                    if (new_list.get(j).getFull() > new_list.get(index).getFull()) {
                        index = j;
                    }
                }
                new_new_list.add(new_list.get(index));
                new_list.remove(index);
            }
            new_count = new_count - (end - start);
        }
        return new_new_list.stream().map(Coupon::toVO).collect(Collectors.toList());
    }
    @Override
    public CouponVO receiveCoupon(CouponVO couponVO) {
        Coupon coupon = couponVO.toPO();
        couponRepository.save(coupon);
        CouponGroup couponGroup = couponGroupRepository.findById(coupon.getGroupId()).orElse(null);
        if (couponGroup == null) {
            throw BlueWhaleException.couponGroupNotExists();
        }
        couponGroup.setAmount(couponGroup.getAmount() - 1);//优惠卷组数量减一
        List<Integer> userId = couponGroup.getUserId();
        userId.add(coupon.getUserId());//添加已领取该优惠卷组的用户id
        couponGroup.setUserId(userId);
        couponGroupRepository.save(couponGroup);
        return coupon.toVO();
    }

    @Override
    public List<String> getNameByUserId(String userId) {
        List<String> names = new ArrayList<>();
        String[] userIds = userId.split(",");
        for (String id : userIds) {
            Integer integer = Integer.parseInt(id);
            User user = userRepository.findById(integer).orElse(null);
            if (user == null) {
                throw BlueWhaleException.userNotExists();
            }
            String name = user.getName();
            names.add(name);
        }
        return names;
    }

}
