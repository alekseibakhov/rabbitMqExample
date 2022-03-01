package com.example.demo.another;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class Consumer {
    Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    @RabbitListener(queues = "new_queue1")
    public void processMyQueue1(String message) {
        logger.info("Received first from my Queue: " + message);
    }

    @RabbitListener(queues = "new_queue2")
    public void processMyQueue2(String message) {
        logger.info("Received second from my Queue: " + message);
    }

}
