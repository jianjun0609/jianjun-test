package com.jianjun.jianjuntest.common.dto;

import lombok.Data;

/**
 * @author Jack
 * @date 2019/1/29 15:56
 */
@Data
public class MqTestDTO {

    private Integer id;
    private String orderNo;
    private String mgs;

    public MqTestDTO() {
    }

    public MqTestDTO(Integer id, String orderNo, String mgs) {
        this.id = id;
        this.orderNo = orderNo;
        this.mgs = mgs;
    }

    @Override
    public String toString() {
        return "MqTestDTO{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", mgs='" + mgs + '\'' +
                '}';
    }
}
