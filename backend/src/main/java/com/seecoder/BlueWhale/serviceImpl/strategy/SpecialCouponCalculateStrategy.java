package com.seecoder.BlueWhale.serviceImpl.strategy;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @Author: DingXiaoyu
 * @Date: 22:35 2023/12/19
 * “蓝鲸券”使用规则：
 * 0-100元区间打九五折；
 * 100-200元区间打九折；
 * 200-300元区间打八五折；
 * 300-400元区间打八折；
 * 400-500元区间打七五折；
 * 500元以上区间打七折。
*/

@Component
@Primary
public class SpecialCouponCalculateStrategy implements CalculateStrategy {

    @Override
    public Double calculate(Double price, Integer full, Integer reduction) {
        if (price >= 0 && price <= 100) {
            return price * 0.95;
        } else if (price > 100 && price <= 200) {
            return price * 0.9;
        } else if (price > 200 && price <= 300) {
            return price * 0.85;
        } else if (price > 300 && price <= 400) {
            return price * 0.8;
        } else if (price > 400 && price <= 500) {
            return price * 0.75;
        } else {
            return price * 0.7;
        }
    }
}
