package com.seecoder.BlueWhale.serviceImpl.strategy;

import org.springframework.stereotype.Component;

@Component
public class FillReductionCouponCalculateStrategy implements CalculateStrategy{


    @Override
    public Double calculate(Double price, Integer full,Integer reductiono) {
        if(price>=full){
            return price-reductiono;
        }
        return price;
    }
}
