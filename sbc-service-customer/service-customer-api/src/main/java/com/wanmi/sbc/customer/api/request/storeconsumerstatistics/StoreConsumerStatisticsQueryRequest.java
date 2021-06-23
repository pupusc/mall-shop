package com.wanmi.sbc.customer.api.request.storeconsumerstatistics;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>店铺客户消费统计表通用查询请求参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreConsumerStatisticsQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-主键idList
     */
    @ApiModelProperty(value = "批量查询主键idList")
    private List<String> idList;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String customerId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 会员在该店铺下单数
     */
    @ApiModelProperty(value = "会员在该店铺下单数")
    private Integer tradeCount;

    /**
     * 会员在该店铺消费额
     */
    @ApiModelProperty(value = "会员在该店铺消费额")
    private BigDecimal tradePriceCount;

    /**
     * 删除标记 0:未删除 1:已删除
     */
    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;

}