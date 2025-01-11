package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.OrderStatusEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.CouponRepository;
import com.seecoder.BlueWhale.repository.OrderRepository;
import com.seecoder.BlueWhale.repository.ProductRepository;
import com.seecoder.BlueWhale.repository.StoreRepository;
import com.seecoder.BlueWhale.service.OrderService;
import com.seecoder.BlueWhale.serviceImpl.strategy.CalculateStrategy;
import com.seecoder.BlueWhale.serviceImpl.strategy.FillReductionCouponCalculateStrategy;
import com.seecoder.BlueWhale.serviceImpl.strategy.SpecialCouponCalculateStrategy;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seecoder.BlueWhale.serviceImpl.strategy.Context;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CalculateStrategy calculateStrategy;

    private static int orderCreationCount = 0;
    private static int deliverCount = 0;
    @Override
    public OrderVO create(OrderVO orderVO) {
        // 统计订单创建次数
        orderCreationCount++;

        Order order = orderVO.toPO();
        order.setUserId(securityUtil.getCurrentUser().getId());
        order.setStatus(OrderStatusEnum.UNPAID);
        order.setCreateTime(new Date());
        orderRepository.save(order);

        // 打印当前统计次数（可根据需要输出到日志等地方）
        System.out.println("当前订单创建次数: " + orderCreationCount);

        return wrapWithProductId(order.toVO());
    }

    @Override
    public List<OrderVO> getAllOrders() {
        User user=securityUtil.getCurrentUser();
        List<Order> orders = null;
        switch (user.getRole()){
            case CUSTOMER:
                orders = orderRepository.findByUserId(user.getId());
                break;
            case STAFF:
                Integer storeId=user.getStoreId();
                orders = orderRepository.findAll().stream()
                        .filter(x-> storeId.equals(productRepository.findById(x.getProductId()).get().getStoreId()))
                        .collect(Collectors.toList());
                break;
            case MANAGER:
            case CEO:
                orders = orderRepository.findAll();
                break;
        }
        return orders.stream().map(x->wrapWithProductId(x.toVO())).collect(Collectors.toList());
    }

    @Override
    public OrderVO getOrder(Integer id) {
        return wrapWithProductId(orderRepository.findById(id).get().toVO());
    }

    @Override
    public Boolean pay(Integer orderId,Integer couponId){
        User user=securityUtil.getCurrentUser();
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw BlueWhaleException.orderNotExists();
        }
        if(order.getStatus() != OrderStatusEnum.UNPAID){
            throw BlueWhaleException.orderStatusError();
        }
        Product product=productRepository.findById(order.getProductId()).get();

        Double price;
        if (couponId == 0){//说明没有使用优惠券
            price = order.getAmount()*product.getPrice();
        }else {
            Coupon coupon = couponRepository.findById(couponId).orElse(null);
            if (coupon == null){
                throw BlueWhaleException.couponNotExists();
            }
            if (coupon.isUsed()){
                throw BlueWhaleException.couponAlreadyUsed();
            }
            if (Objects.equals(coupon.getType(), "FULL_REDUCTION")){
                Context context = new Context(new FillReductionCouponCalculateStrategy());
                price = context.Calculate(order.getAmount()*product.getPrice(),coupon.getFull(),coupon.getReduction());
                coupon.setUsed(true);
            }else {
                Context context = new Context(new SpecialCouponCalculateStrategy());
                price = context.Calculate(order.getAmount()*product.getPrice(),coupon.getFull(),coupon.getReduction());
                coupon.setUsed(true);
            }
        }

        order.setPaid(price);
        order.setStatus(OrderStatusEnum.UNSEND);
        orderRepository.save(order);
        return true;
    }

    @Override
    public Boolean deliver(Integer orderId) {
        deliverCount++;
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw BlueWhaleException.orderNotExists();
        }
        if(order.getStatus() != OrderStatusEnum.UNSEND){
            throw BlueWhaleException.orderStatusError();
        }
        Product product = productRepository.findById(order.getProductId()).get();
        if (product.getStock()<order.getAmount()){
            throw BlueWhaleException.stockNotEnough();
        }
        order.setStatus(OrderStatusEnum.UNGET);
        orderRepository.save(order);
        product.setSalesAmount(product.getSalesAmount()+order.getAmount());
        product.setStock(product.getStock()-order.getAmount());
        productRepository.save(product);

        System.out.println("发货次数: " + deliverCount);
        return true;
    }

    @Override
    public Boolean get(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw BlueWhaleException.orderNotExists();
        }
        if(order.getStatus() != OrderStatusEnum.UNGET){
            throw BlueWhaleException.orderStatusError();
        }
        order.setStatus(OrderStatusEnum.UNCOMMENT);
        orderRepository.save(order);
        return true;
    }

    @Override
    public Boolean comment(Integer orderId, String comment, Integer rating) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw BlueWhaleException.orderNotExists();
        }
        if(order.getStatus() != OrderStatusEnum.UNCOMMENT){
            throw BlueWhaleException.orderStatusError();
        }
        order.setContent(comment);
        order.setRating(rating);
        order.setStatus(OrderStatusEnum.DONE);
        order.setFinishTime(new Date());
        orderRepository.save(order);

        Product product = productRepository.findById(order.getProductId()).get();
        Double currentProductRating = product.getRating();
        product.setRating((currentProductRating*product.getNumber()+rating)/(product.getNumber()+1));
        product.setNumber(product.getNumber()+1);
        productRepository.save(product);

        Store store = storeRepository.findById(product.getStoreId()).get();
        Double currentStoreRating = store.getRating();
        store.setRating((currentStoreRating*store.getNumber()+rating)/(store.getNumber()+1));
        store.setNumber(store.getNumber()+1);
        storeRepository.save(store);
        return true;
    }

    @Override
    public Double getPrice(Integer orderId, Integer couponId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw BlueWhaleException.orderNotExists();
        }
        if(order.getStatus() != OrderStatusEnum.UNPAID){
            throw BlueWhaleException.orderStatusError();
        }

        Optional<Product> productOptional = productRepository.findById(order.getProductId());
        if (!productOptional.isPresent()){
            throw BlueWhaleException.productNotExists();
        }
        Product product = productOptional.get();

        if (couponId == 0){//说明没有使用优惠券
            return order.getAmount()*product.getPrice();
        }
        Coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null){
            throw BlueWhaleException.couponNotExists();
        }
        if (coupon.isUsed()){
            throw BlueWhaleException.couponAlreadyUsed();
        }
        if (Objects.equals(coupon.getType(), "FULL_REDUCTION")){
            Context context = new Context(new FillReductionCouponCalculateStrategy());
            return context.Calculate(order.getAmount()*product.getPrice(),coupon.getFull(),coupon.getReduction());
        }else {
            Context context = new Context(new SpecialCouponCalculateStrategy());
            return context.Calculate(order.getAmount()*product.getPrice(),coupon.getFull(),coupon.getReduction());
        }
    }

    private OrderVO wrapWithProductId(OrderVO orderVO){
        Integer productId=orderVO.getProductId();
        Product product=productRepository.findById(productId).get();
        orderVO.setProductName(product.getName());
        orderVO.setPrice(product.getPrice());
        orderVO.setStoreId(product.getStoreId());
        return orderVO;
    }
}
