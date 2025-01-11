package com.seecoder.BlueWhale;

import com.seecoder.BlueWhale.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlueWhaleApplication implements CommandLineRunner {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public static void main(String[] args) {
        SpringApplication.run(BlueWhaleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        kafkaProducerService.sendMessage("Test message");
    }
}