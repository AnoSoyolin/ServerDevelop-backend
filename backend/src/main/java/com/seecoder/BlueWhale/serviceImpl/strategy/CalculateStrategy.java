package com.seecoder.BlueWhale.serviceImpl.strategy;

import org.springframework.stereotype.Component;

@Component
public interface CalculateStrategy {
    Double calculate(Double price, Integer full,Integer reduction);
}
