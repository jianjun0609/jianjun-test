package com.jianjun.jianjuntest.amqp;

import com.jianjun.jianjuntest.common.constant.Constants;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Jack
 * @date 2019/1/28 16:52
 */
@Component
@EnableRabbit
@Configuration
public class XdelayReceiver {

    @RabbitListener(queues = Constants.IMMEDIATE_QUEUE_XDELAY)
    public void get(String booking) {
        System.out.println("----------------Receive" + booking);
    }
}
