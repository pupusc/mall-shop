package com.wanmi.sbc.order.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午4:54 2019/5/24
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponTradeValid {

    /**
     * 购买人用户id
     */
    private String customerId;

    /**
     * 购买的spuId
     */
    private String goodsId;

    /**
     * 单品id
     */
    private String goodsInfoId;

    /**
     * 团编号
     */
    private String grouponNo;

    /**
     * 是否开团购买(true:开团 false:参团 null:非拼团购买)
     */
    private Boolean openGroupon;

    /**
     * 购买数量
     */
    private Integer buyCount;

}
