package com.wanmi.sbc.order.follow.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单设置请求实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeSettingHttpRequest implements Serializable{

    /**
     * 修改配置项
     */
    List<TradeSettingRequest> tradeSettingRequests;
}
