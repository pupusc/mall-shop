package com.wanmi.sbc.open.vo;

import lombok.Data;

import java.io.*;

@Data
public class TradeItemReqVO implements Serializable {
    private String skuId;
    /**
     * 购买数量
     */
    private Long num;
}
