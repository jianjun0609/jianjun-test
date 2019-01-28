package com.jianjun.jianjuntest.amqp;

import com.jianjun.jianjuntest.common.constant.Constants;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jack
 * @date 2019/1/28 16:49
 */
@Service
public class XdelaySender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String booking, int delayTime) {
        System.out.println("delayTime" + delayTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.rabbitTemplate.convertAndSend(Constants.DELAYED_EXCHANGE_XDELAY, Constants.DELAY_ROUTING_KEY_XDELAY, booking, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDelay(delayTime);
                System.out.println(sdf.format(new Date()) + " Delay sent.");
                return message;
            }
        });
    }
}
