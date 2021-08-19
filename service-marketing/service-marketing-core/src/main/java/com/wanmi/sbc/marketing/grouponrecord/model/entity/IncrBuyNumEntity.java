package com.wanmi.sbc.marketing.grouponrecord.model.entity;

import lombok.Data;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:45 2019/5/25
 * @Description:
 */
@Data
public class IncrBuyNumEntity {

    /**
     * 拼团活动ID
     */
    private String grouponActivityId;

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * sku编号
     */
    private String goodsInfoId;

    /**
     * 购买数
     */
    private Integer buyNum;

    /**
     * SPU编号
     */
    private String goodsId;

    /**
     * 限购数量
     */
    private Integer limitSellingNum;

}
