package com.wanmi.sbc.order.api.request.groupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:48 2019/5/24
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponOrderValidRequest {

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
     * null 不验证数量
     */
    private Integer buyCount;
}
