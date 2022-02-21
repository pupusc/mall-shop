package com.wanmi.sbc.open.vo;

import lombok.Data;

import java.io.*;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-02-15 18:02:00
 */
@Data
public class OrderCreateReqVO implements Serializable {
    /**
     * 樊登读书用户id
     */
    private String fddsUserId;
    /**
     * 备注信息
     */
    private String buyerRemark;
    /**
     * 收货地址
     */
    private String consigneeAddress;
    /**
     * 收货人信息
     */
    private ConsigneeReqVO consignee;
    /**
     * 发货清单
     */
    private List<TradeItemReqVO> tradeItems;
}
