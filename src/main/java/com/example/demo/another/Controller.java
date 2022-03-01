package com.example.demo.another;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class Controller {

    /*Задает базовый набор операций AMQP. Обеспечивает синхронные методы отправки и получения.
    Методы convertAndSend(Object) и receiveAndConvert() позволяют отправлять и получать объекты POJO.
    Ожидается, что реализации будут делегированы экземпляру org.springframework.amqp.support.converter.MessageConverter
    для выполнения преобразования в и из типа полезной нагрузки AMQP byte[].*/
    private final RabbitTemplate rabbitTemplate;

    Logger logger = LoggerFactory.getLogger(RabbitConfig.class);


    public Controller(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/edit")
    public String edit(@RequestBody String message) {
        logger.info("edit to my Queue");
        rabbitTemplate.setExchange("common_Exchange");
        rabbitTemplate.convertAndSend(message);

        return "success default exchange";
    }

    @PostMapping("/edit1")
    public String directExchange(@RequestBody Map<String, String> map) {
        logger.info("edit to my Queue");
        rabbitTemplate.setExchange("direct_exchange1");
        rabbitTemplate.convertAndSend(map.get("key"), map.get("message"));

        return "success DirectExchange";
    }

    @PostMapping("/edit2")
    public String topicExchange(@RequestBody Map<String, String > map){
        logger.info("edit to my Queue");
        rabbitTemplate.setExchange("topic_exchange1");
        rabbitTemplate.convertAndSend(map.get("key"), map.get("message"));
        return "success TopicExchange";
    }
}
