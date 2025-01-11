package com.seecoder.BlueWhale.serviceImpl.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Context {
    private final CalculateStrategy calculateStrategy;

    @Autowired
    public Context(CalculateStrategy calculateStrategy) {
        this.calculateStrategy = calculateStrategy;
    }


    public Double Calculate(Double price, Integer full, Integer reduction) {
        return calculateStrategy.calculate(price, full, reduction);
    }
}
