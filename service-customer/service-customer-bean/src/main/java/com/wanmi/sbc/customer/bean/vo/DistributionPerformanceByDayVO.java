package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>分销业绩日统计记录</p>
 * Created by of628-wenzhi on 2019-04-17-16:01.
 */
@Data
@ApiModel
public class DistributionPerformanceByDayVO implements Serializable {

    private static final long serialVersionUID = -8529431759658800472L;

    /**
     * 分销员id
     */
    @ApiModelProperty("分销员id")
    private String distributionId;

    /**
     * 会员id
     */
    @ApiModelProperty("会员id")
    private String customerId;

    /**
     * 销售额
     */
    @ApiModelProperty("销售额")
    private BigDecimal saleAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);

    /**
     * 预估收益
     */
    @ApiModelProperty("预估收益")
    private BigDecimal commission = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);

    /**
     * 日期 (yyyy-MM-dd)
     */
    @ApiModelProperty("日期 (yyyy-MM-dd)")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate targetDate;

}
