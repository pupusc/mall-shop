package com.wanmi.sbc.customer.api.request.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>用户分销排行榜通用查询请求参数</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerRankingQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-会员IDList
     */
    @ApiModelProperty(value = "批量查询-会员IDList")
    private List<String> customerIdList;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;


    /**
     * 排行类型
     */
    @ApiModelProperty(value = "排行类型")
    private String type;

    /**
     * 查询日期
     */
    @ApiModelProperty(value = "查询日期")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate targetDate;

}