package com.jianjun.jianjuntest.amqp;

import com.jianjun.jianjuntest.common.constant.Constants;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jack
 * @date 2019/1/28 16:49
 */
@Service
public class XSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 延迟队列发送
     * @param msg 需要发送的消息
     * @param delayTime 延迟的时间 单位毫秒
     */
    public void sendDelay(String msg, int delayTime) {
        System.out.println("delayTime" + delayTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.rabbitTemplate.convertAndSend(Constants.DELAYED_EXCHANGE_XDELAY, Constants.DELAY_ROUTING_KEY_XDELAY, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDelay(delayTime);
                System.out.println(sdf.format(new Date()) + " Delay sent.");
                return message;
            }
        });
    }

    /**
     * 非延迟发送消息
     * @param msg 需要发送的消息
     */
    public void sendHello(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rabbitTemplate.convertAndSend(Constants.HELLO_QUEUE, msg);
    }
}
