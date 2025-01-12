package com.seecoder.BlueWhale.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test_topic", groupId = "test_group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }

    @KafkaListener(topics = "order_topic", groupId = "order_group")
    public void consumeOrderMessage(String message) {
        System.out.println("Consumed order message: " + message);
        // Process order message logic
        // ...
    }

    @KafkaListener(topics = "product_topic", groupId = "product_group")
    public void consumeProductMessage(String message) {
        System.out.println("Consumed product message: " + message);
        // Process product message logic
        // ...
    }

    @KafkaListener(topics = "coupon_topic", groupId = "coupon_group")
    public void consumeCouponMessage(String message) {
        System.out.println("Consumed coupon message: " + message);
        // Process coupon message logic
        // ...
    }
}