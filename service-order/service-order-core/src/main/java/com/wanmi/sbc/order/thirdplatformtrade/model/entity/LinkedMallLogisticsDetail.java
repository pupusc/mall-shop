package com.wanmi.sbc.order.thirdplatformtrade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * linkedmall 物流信息
 *
 * @author yuhuiyu
 * Date 2020-8-22 13:01:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkedMallLogisticsDetail {

    /**
     * 物流描述
     */
    private String standerdDesc;

    /**
     * 时间
     */
    private String ocurrTimeStr;
}
