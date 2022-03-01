package com.example.demo.another;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    // создаем соединение
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }


    /*AmqpAdmin - это функциональный компонент системы управления Rabbit MQ, используемый
    для создания и удаления очереди, обмена и привязки.*/
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /*Вспомогательный класс, упрощающий синхронный доступ к RabbitMQ (отправка и получение сообщений).
    д
    * */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate =  new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    /* создаём очереди */
    @Bean
    public Queue myQueue1() {
        return new Queue("new_queue1");
    }
    @Bean
    public Queue myQueue2() {
        return new Queue("new_queue2");}

    /*бесключевой обменник*/
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("common_Exchange"); }
    /* проверяет ключи частично */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topic_exchange1");}
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("direct_exchange1");}

    /*Связываем очереди с обменниками*/
    @Bean
    public Binding bindingFanoutExchange1(){
        return BindingBuilder.bind(myQueue1()).to(fanoutExchange());}
    @Bean
    public Binding bindingFanoutExchange2(){
        return BindingBuilder.bind(myQueue2()).to(fanoutExchange());}


    // связываем с обменником со строгим ключом
    @Bean
    public Binding bindingDirectExchange1(){
        return BindingBuilder.bind(myQueue1()).to(directExchange()).with("error");}
    @Bean
    public Binding bindingDirectExchange2(){
        return BindingBuilder.bind(myQueue2()).to(directExchange()).with("info");}
    @Bean
    public Binding bindingTopic(){
        return BindingBuilder.bind(myQueue1()).to(topicExchange()).with("error.*");}
    @Bean
    public Binding bindingTopic1(){
        return BindingBuilder.bind(myQueue2()).to(topicExchange()).with("info.*");}


    /* создаём слушателя очереди и привязываем его к очереди
     * или можем сделать эти настройки в отдельном классе с помощью аннотаций */

/*    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames("new_queue");
        container.setMessageListener(message -> logger.info("Received from my queue: " + new String(message.getBody())));
        return container;
    }*/
}
