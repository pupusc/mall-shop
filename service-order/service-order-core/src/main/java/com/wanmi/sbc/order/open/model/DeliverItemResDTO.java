package com.wanmi.sbc.open.vo;

import lombok.Data;

@Data
public class DeliverItemResDTO {
    /**
     * 清单编号
     */
    private String listNo;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 发货数量
     */
    private Long itemNum;

    private String skuId;

    private String skuNo;

    /**
     * 商品图片
     */
    private String pic;

    /**
     * 规格描述信息
     */
    private String specDetails;

    /**
     * 单位
     */
    private String unit;
}
