package com.jianjun.jianjuntest.configurer;

import com.jianjun.jianjuntest.common.constant.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jack
 * @date 2019/1/28 11:59
 */
@Configuration
public class RabbitConfig {

    // 创建一个立即消费队列
    @Bean
    public Queue immediateQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(Constants.IMMEDIATE_QUEUE_XDELAY, true);
    }

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(Constants.DELAYED_EXCHANGE_XDELAY, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingNotify() {
        return BindingBuilder.bind(immediateQueue()).to(delayExchange()).with(Constants.DELAY_ROUTING_KEY_XDELAY).noargs();
    }

    // 支付超时延时交换机
    /*public static final String Delay_Exchange_Name = "delay.exchange";

    // 超时订单关闭队列
    public static final String Timeout_Trade_Queue_Name = "close_trade";

    @Bean
    public Queue delayPayQueue() {
        return new Queue(RabbitConfig.Timeout_Trade_Queue_Name, true);
    }

    // 定义广播模式的延时交换机 无需绑定路由
    @Bean
    FanoutExchange delayExchange(){
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        FanoutExchange topicExchange = new FanoutExchange(RabbitConfig.Delay_Exchange_Name, true, false, args);
        topicExchange.setDelayed(true);
        return topicExchange;
    }

    // 绑定延时队列与交换机
    @Bean
    public Binding delayPayBind() {
        return BindingBuilder.bind(delayPayQueue()).to(delayExchange());
    }

    // 定义消息转换器
    @Bean
    Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 定义消息模板用于发布消息，并且设置其消息转换器
    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
    @Bean
    RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue helloQueue(){
        return new Queue("hello");
    }*/
}
