package com.jianjun.jianjuntest.controller;

import com.jianjun.jianjuntest.amqp.XdelaySender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Jack
 * @date 2019/1/28 11:47
 */
@RestController
@RequestMapping("/amqp")
@Api(tags = "amqp-test")
public class AmpbTestController {

    @Resource
    private XdelaySender xdelaySender;

    @GetMapping(value = "/sendDelayMsg")
    @ApiOperation(value = "amqp测试延迟队列消费信息", notes = "amqp测试延迟队列消费信息")
    public String sendDelayMsg(@RequestParam String msg, @RequestParam int time) {
        xdelaySender.send(msg, time);
        return "success";
    }

    @GetMapping(value = "/sendHello")
    @ApiOperation(value = "amqp测试立即消费信息", notes = "amqp测试立即消费信息")
    public String sendHelloMsg(@RequestParam String msg) {
        xdelaySender.sendHello(msg);
        return "success";
    }
}
