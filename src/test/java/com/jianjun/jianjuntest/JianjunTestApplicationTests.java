package com.jianjun.jianjuntest;

import com.alibaba.fastjson.JSONObject;
import com.jianjun.jianjuntest.amqp.XSender;
import com.jianjun.jianjuntest.common.dto.MqTestDTO;
import com.jianjun.jianjuntest.common.util.Common;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JianjunTestApplicationTests {

    @Resource
    private XSender xSender;

    @Test
    public void testDealyQueue() {
        MqTestDTO mqTestDTO = new MqTestDTO(1, Common.generateOrderNo(), "测试延迟消息队列");
        xSender.sendDelay(JSONObject.toJSONString(mqTestDTO), 60 * 1000);
    }

}

