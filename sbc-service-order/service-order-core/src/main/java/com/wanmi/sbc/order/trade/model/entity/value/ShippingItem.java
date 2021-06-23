package com.wanmi.sbc.order.trade.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 发货清单
 * @author wumeng[OF2627]
 *         company qianmi.com
 *         Date 2017-04-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingItem {
    /**
     * 清单编号
     */
    private String listNo;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 货号
     */
    private String bn;

    /**
     * 发货数量
     */
    @NotNull
    @Min(1L)
    private Long itemNum;

    /**
     * 商品单号
     */
    private String oid;

    private String spuId;

    @NotNull
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

    /**
     * 供应商商品编码
     */
    private String providerSkuNo;

}
