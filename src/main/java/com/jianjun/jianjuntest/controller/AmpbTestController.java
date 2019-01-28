package com.jianjun.jianjuntest.controller;

import com.jianjun.jianjuntest.amqp.XdelaySender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping(value = "/test1")
    @ApiOperation(value = "amqp测试1", notes = "amqp测试1")
    public String test1() {
        xdelaySender.send("111111111111111111", 5);;
        return "success";
    }

}
