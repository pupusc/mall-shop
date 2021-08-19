package com.wanmi.sbc.order.thirdplatformtrade.model.root;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.thirdplatformtrade.model.entity.LinkedMallGoods;
import com.wanmi.sbc.order.thirdplatformtrade.model.entity.LinkedMallLogisticsDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class LinkedMallTradeLogistics implements Serializable {

    private static final long serialVersionUID = 7703859135448797434L;

    /**
     * 物流id
     */
    private String id;

    /**
     * 平台订单号
     */
    private String tradeId;

    /**
     * 购买用户id
     */
    private String customerId;

    /**
     * linkedmall订单号
     */
    private String lmOrderId;

    /**
     * 物流单号
     */
    private String mailNo;

    /**
     * 物流信息提供方
     */
    private String dataProvider;

    /**
     * 物流信息 标题
     */
    private String dataProviderTitle;

    /**
     * 物流公司名称
     */
    private String logisticsCompanyName;

    /**
     * 物流公司编码
     */
    private String logisticsCompanyCode;

    /**
     * 物流信息
     */
    private List<LinkedMallLogisticsDetail> logisticsDetailList = new ArrayList<>();

    /**
     * 商品列表
     */
    private List<LinkedMallGoods> goods = new ArrayList<>();

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

}
