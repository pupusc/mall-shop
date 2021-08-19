package com.wanmi.sbc.marketing.api.response.distributionrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>根据分该分销员的业绩</p>
 * @author baijz
 * @date 2019-03-08 10:28:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DistributionRecordByInviteeIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 昨日销售额
     */
    @ApiModelProperty(value = "昨日销售额")
    private BigDecimal yesterdaySales;

    /**
     * 昨日预估收益
     */
    @ApiModelProperty(value = "昨日预估收益")
    private BigDecimal yesterdayEstimatedReturn;

    /**
     * 本月销售额
     */
    @ApiModelProperty(value = "本月销售额")
    private BigDecimal monthSales;

    /**
     * 本月预估收益
     */
    @ApiModelProperty(value = "本月预估收益")
    private BigDecimal monthEstimatedReturn;
}
