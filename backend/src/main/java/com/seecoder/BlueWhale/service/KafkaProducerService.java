package com.seecoder.BlueWhale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "test_topic";

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
    private static final String PRODUCT_TOPIC = "product_topic";
    private static final String COUPON_TOPIC = "coupon_topic";
    private static final String ORDER_TOPIC = "order_topic";

    public void sendProductMessage(String message) {
        kafkaTemplate.send(PRODUCT_TOPIC, message);
    }

    public void sendCouponMessage(String message) {
        kafkaTemplate.send(COUPON_TOPIC, message);
    }

    public void sendOrderMessage(String message) {
        kafkaTemplate.send(ORDER_TOPIC, message);
    }
}